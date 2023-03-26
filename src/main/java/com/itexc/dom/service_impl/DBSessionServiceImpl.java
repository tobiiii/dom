package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.repository.DBSessionRepository;
import com.itexc.dom.security.TokenProvider;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.sevice.DBSessionService;
import com.itexc.dom.sevice.UserService;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class DBSessionServiceImpl implements DBSessionService {

    @Autowired
    private DBSessionRepository dbsessionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Transactional
    @Override
    public void disconnectByToken(final String jwtToken) {
        Optional<DBSession> op = dbsessionRepository.findByToken(jwtToken);
        if (op.isPresent()) {
            var dbsession = op.get();
            dbsession.setActive(false);
            dbsession.setLogoutTime(new Date());
            dbsessionRepository.save(dbsession);
        }
    }

    @Transactional
    @Override
    public void disconnectByProfile(Profile profile) {
        dbsessionRepository.closeUserSessionsByProfile(profile);
    }

    @Override
    public Optional<DBSession> findBy(final String field, final String value) {
        if (field == null) {
            return Optional.ofNullable(null);
        }

        switch (field) {
            case "token":
                return dbsessionRepository.findByToken(value);
            default:
                break;
        }

        return Optional.ofNullable(null);
    }

    @Override
    public void refreshToken(final String expiredToken, final String newToken) throws ValidationException {
        Optional<DBSession> currentSession = dbsessionRepository.findByToken(expiredToken);
        if (currentSession.isPresent()) {

            final var idToken = this.jwtTokenUtil.getIdFromToken(newToken);
            currentSession.get().setToken(idToken);

            final var expiration = this.jwtTokenUtil.getExpirationDateFromToken(newToken);
            currentSession.get().setLogoutTime(expiration);
            dbsessionRepository.save(currentSession.get());
        } else {
            log.trace("Invalid Session");
            throw new ValidationException(ERROR_CODE.INVALID_SESSION);
        }

     }

    @Override
    public void disconnectByUser(User user) {
        dbsessionRepository.closeUserSessions(user.getEmailAddress());
    }

    @Override
    public void checkSession(Optional<DBSession> dbSession) throws ValidationException {
        var getSession = dbSession
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INVALID_SESSION));

        if (!getSession.isActive()) {
            throw new ValidationException(ERROR_CODE.INVALID_SESSION);
        }

        if (getSession.isBlocked()) {
            throw new ValidationException(ERROR_CODE.INVALID_SESSION);
        }
    }

    @Override
    public void logout() throws ValidationException {
        User user = userService.getConnectedUser();
        //Close active session
        dbsessionRepository.closeUserSessions(user.getEmailAddress());
    }

    @Override
    public void startSession(HttpServletRequest request, String jwtToken, User user) {
        final var currentLocale = request.getLocale();

        //Close active session
        dbsessionRepository.closeUserSessions(user.getEmailAddress());

        //Create new session
        var dbSession = new DBSession();
        dbSession.setUser(user);
        dbSession.setFullName(user.getFirstName() + " " + user.getLastName());
        dbSession.setIpAddress(
                StringUtils.isNotEmpty(request.getHeader("X-Forwarded-For")) ? request.getHeader("X-Forwarded-For")
                        : request.getRemoteAddr());
        dbSession.setLanguage(currentLocale.getDisplayLanguage());
        dbSession.setActive(true);
        dbSession.setBlocked(false);
        dbSession.setEmail(user.getEmailAddress());
        dbSession.setLogonTime(new Date());
        final var expiration = jwtTokenUtil.getExpirationDateFromToken(jwtToken);
        dbSession.setLogoutTime(expiration);
        final var idFromToken = jwtTokenUtil.getIdFromToken(jwtToken);
        dbSession.setToken(idFromToken);

        try {
            dbsessionRepository.save(dbSession);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}
