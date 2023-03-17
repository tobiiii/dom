package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.AuthenticationRequest;
import com.itexc.dom.domain.DTO.AuthenticationResponse;
import com.itexc.dom.domain.DTO.ChangePasswordDto;
import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.Password;
import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.SecurityCustomizationRepository;
import com.itexc.dom.repository.UserRepository;
import com.itexc.dom.security.DomWebAuthenticationDetails;
import com.itexc.dom.security.TokenProvider;
import com.itexc.dom.security.WebSecurityConfig;
import com.itexc.dom.sevice.DBSessionService;
import com.itexc.dom.sevice.ProfileService;
import com.itexc.dom.sevice.UserService;
import com.itexc.dom.utils.ParamsProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.itexc.dom.utils.Utils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DBSessionService dbSessionService;

    @Autowired
    private ParamsProvider paramsProvider;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;



    @Autowired
    SecurityCustomizationRepository securityCustomizationRepository;



    @Override
    public Page<UserView> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserView::new);
    }

    @Transactional
    @Override
    public UserView create(UserDto user) throws Throwable {
        User newUser = new User();
        if (userRepository.existsByEmailAddressIgnoreCase(user.getEmailAddress().toLowerCase())) {
            throw new ValidationException(ERROR_CODE.EMAIL_EXISTS);
        }
        String generatedPassword = Utils.getDefaultPassword(paramsProvider.getGeneratedPasswordLength());
        Profile profile = profileService.findById(user.getProfile());
        newUser.setProfile(profile);
        newUser.setLastName(user.getLastName());
        newUser.setFirstName(user.getFirstName());
        newUser.setEmailAddress(user.getEmailAddress());

        newUser =  userRepository.save(newUser);

        Password password = new Password();
        password.setUser(newUser);
        password.setCredential(webSecurityConfig.passwordEncoder().encode(generatedPassword));
        password.setIsTemporary(true);
        password = securityCustomizationRepository.save(password);
        newUser.setPassword(password);

        UserView result = new UserView(userRepository.save(newUser));
        result.setDefaultPass(generatedPassword);
        return result;
    }

    @Override
    public User update(Long userId, UserDto user) throws ValidationException {
        User userToUpdate = findUserById(userId);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        return userRepository.save(userToUpdate);
    }

    @Override
    public User findUserById(Long userId) throws ValidationException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_USER));
    }

    @Override
    public UserView getUserById(Long userId) throws Throwable {
        return new UserView(findUserById(userId));
    }

    @Override
    public void delete(Long userId) throws ValidationException {
       User userToDelete = findUserById(userId);
        try {
            userRepository.delete(userToDelete);
        } catch (Exception e) {
            throw new ValidationException(ERROR_CODE.REFERENCED_USER);
        }

    }

    @Override
    public User getConnectedUser() throws ValidationException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getUser();
        }

        throw new ValidationException(ERROR_CODE.INVALID_SESSION);
    }


    @Override
    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest,
                                                 HttpServletRequest request, HttpServletResponse response) throws Throwable {
        User user = null;
        try {
            // 1 CHECK email
            user = checkEmail(authenticationRequest.getEmail());
            // 2 CHECK PWD
            checkPassword(authenticationRequest.getPassword(), user);
        } catch (ValidationException e) {
            switch (ERROR_CODE.valueOf(e.getErrorCode())) {
                case INCORRECT_PASSWORD:
                case INCORRECT_EMAIL:
                    throw new ValidationException(ERROR_CODE.INCORRECT_USERNAME_OR_PASSWORD);

                default:
                    throw e;
            }
        }

        Collection<GrantedAuthority> authorities = user.getProfile().getPrivileges().stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
                .collect(Collectors.toSet());

        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword(),
                            authorities));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (final BadCredentialsException e) {
            throw new ValidationException(ERROR_CODE.INCORRECT_USERNAME_OR_PASSWORD);
        }
        //        Map<String, String> claims = new HashMap<>(); //Optional
        final String accessToken = tokenProvider.generateToken(user, null);

        // Start admin session
        dbSessionService.startSession(request, accessToken, user);

        //Get refresh token
        final String idToken = tokenProvider.getIdFromToken(accessToken);

        String redirectTo = user.getPassword().getIsTemporary() ? "RESET_PASSWORD" : "ADMIN";

        Collection<String> privileges = user.getProfile().getPrivileges().stream()
                .map(Privilege::getCode)
                .collect(Collectors.toSet());

        Long sessionDuration = paramsProvider.getTokenDuration() * 60;
        String appBuildInfo = paramsProvider.getAppBuildInfo();

        return new AuthenticationResponse(accessToken, idToken, redirectTo, sessionDuration, appBuildInfo,
                new UserView(user), privileges);
    }


    @Override
    public User checkEmail(String email) throws ValidationException {
        return userRepository.findByEmailAddressIgnoreCase(email)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INCORRECT_EMAIL));
    }
    public void checkPassword(String pwd , User user) throws ValidationException {

        if (!webSecurityConfig.passwordEncoder().matches(pwd, user.getPassword().getCredential())) {
            throw new ValidationException(ERROR_CODE.INCORRECT_PASSWORD);
        } else {
                userRepository.save(user);
        }
    }

    @Override
    public boolean isProfileAttributed(Profile profile) {
        return userRepository.existsByProfile(profile);
    }

    @Override
    public AuthenticationResponse renewSession(String tokenId, HttpServletRequest request)
            throws Throwable {

        final var expiredToken = tokenProvider.getTokenFromRequest(request);
        final var expiredTokenId = tokenProvider.getIdFromExpiredToken(expiredToken);

        if (!expiredTokenId.equals(tokenId)) {
            throw new ValidationException(ERROR_CODE.DIFFERENT_TOKEN);
        }

        //Check existence of token
        var optionalSession = dbSessionService.findBy("token", expiredTokenId);

        //Check session
        dbSessionService.checkSession(optionalSession);
        var dbSession = optionalSession.get();

        //Check allowed duration to refresh token
        Instant deadline = dbSession.getLogoutTime().toInstant().plus(paramsProvider.getRefreshTokenDuration(), ChronoUnit.MINUTES);
        if (deadline.isBefore(Instant.now())) {
            dbSessionService.disconnectByToken(expiredTokenId);
            throw new ValidationException(ERROR_CODE.EXPIRED_TOKEN);
        }

        var user = dbSession.getUser();

        final String accessToken = tokenProvider.generateToken(user, null);

        var userDetails = userDetailsServiceImpl.loadUserByUsername(dbSession.getEmail(), dbSession);
        var authentication = tokenProvider.getAuthenticationToken(accessToken, userDetails);
        var webDetails = new DomWebAuthenticationDetails(request);
        authentication.setDetails(webDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        dbSessionService.refreshToken(expiredTokenId, accessToken);

        String refreshToken = tokenProvider.getIdFromToken(accessToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public  void changePassword(ChangePasswordDto passwordDto) throws ValidationException {
        User user = getConnectedUser();
        if (user == null) {
            throw new ValidationException(ERROR_CODE.INVALID_SESSION);
        }
        //If the old password match the current one
        checkPassword(passwordDto.getOldPassword(), user);
        savePassword(passwordDto.getNewPassword(), user);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <E extends User> void savePassword(String password, E user) {
        user.getPassword().setCredential(webSecurityConfig.passwordEncoder().encode(password));
        user.getPassword().setIsTemporary(false);
        securityCustomizationRepository.save(user.getPassword());
    }


}
