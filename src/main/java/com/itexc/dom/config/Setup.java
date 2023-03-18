package com.itexc.dom.config;

import com.itexc.dom.domain.*;
import com.itexc.dom.repository.PrivilegeRepository;
import com.itexc.dom.repository.ProfileRepository;
import com.itexc.dom.repository.SecurityCustomizationRepository;
import com.itexc.dom.repository.UserRepository;
import com.itexc.dom.utils.CSVHelper;
import com.itexc.dom.utils.ParamsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Setup {

    @Value("classpath:db/populate/privilege.csv")
    private Resource priviligeFile;

    @Value("classpath:db/populate/profile_privilege.csv")
    private Resource profilePrivilegeFile;

    @Value("classpath:db/populate/profile.csv")
    private Resource profileFile;

    @Value("classpath:db/populate/user.csv")
    private Resource userFile;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private SecurityCustomizationRepository securityCustomizationRepository;


    @Autowired
    private CSVHelper csvHelper;

    @Autowired
    private ParamsProvider paramsProvider;

    public void init() {
        if (paramsProvider.getPopulateInit()) {
            setupPrivilege();
            setupProfile();
            setupUser();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setupPrivilege() {
        try {
            List<Privilege> list = csvHelper.csvToPrivileges(priviligeFile.getInputStream(), Privilege.class);

            Optional<Privilege> optional;
            for (var entity : list) {
                optional = privilegeRepository.findByCode(entity.getCode());
                if (optional.isEmpty()) {
                    privilegeRepository.save(entity);
                } else if (paramsProvider.getPopulateOverwrite()) {
                    optional.get().setCode(entity.getCode());
                    optional.get().setDescription(entity.getDescription());
                    privilegeRepository.save(optional.get());
                }
            }
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setupProfile() {
        try {
            Map<String, List<Privilege>> map = csvHelper.csvToProfilePrivilege(profilePrivilegeFile.getInputStream());
            List<Profile> list = csvHelper.csvToProfile(profileFile.getInputStream(), map, Profile.class);

            Optional<Profile> optional;
            for (var entity : list) {
                optional = profileRepository.findByCode(entity.getCode());
                if (optional.isEmpty()) {
                    profileRepository.save(entity);
                } else if (!optional.get().getPrivileges().containsAll(entity.getPrivileges())) {
                    optional.get().setPrivileges(entity.getPrivileges());
                    profileRepository.save(optional.get());
                } else if (paramsProvider.getPopulateOverwrite()) {
                    optional.get().setName(entity.getName());
                    optional.get().setPrivileges(entity.getPrivileges());
                    profileRepository.save(optional.get());
                }
            }
        } catch (Throwable e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setupUser() {
        try {
            List<User> list = csvHelper.csvToUser(userFile.getInputStream(), User.class);

            Optional<User> optional;
            for (var entity : list) {
                optional = userRepository.findByEmailAddressIgnoreCase(entity.getEmailAddress());
                if (optional.isEmpty()) {
                    Password password = entity.getPassword();
                    entity.setPassword(null);
                    User user = (User) userRepository.save(entity);
                    password.setUser(user);
                    securityCustomizationRepository.save(password);
                } else if (paramsProvider.getPopulateOverwrite()) {
                    optional.get().setProfile(entity.getProfile());
                    optional.get().setLastName(entity.getLastName());
                    optional.get().setFirstName(entity.getFirstName());
                    optional.get().setEmailAddress(entity.getEmailAddress());
                    optional.get().getPassword().setCredential(entity.getPassword().getCredential());
                    userRepository.saveAndFlush(optional.get());
                }
            }
        } catch (Exception e) {
        }
    }
}
