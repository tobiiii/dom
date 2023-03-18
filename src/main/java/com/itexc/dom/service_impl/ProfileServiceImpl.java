package com.itexc.dom.service_impl;

import com.itexc.dom.domain.CommonEntity;

import com.itexc.dom.domain.DTO.ProfileDto;
import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.PrivilegeView;
import com.itexc.dom.domain.projection.ProfileView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.ProfileRepository;
import com.itexc.dom.sevice.DBSessionService;
import com.itexc.dom.sevice.PrivilegeService;
import com.itexc.dom.sevice.ProfileService;
import com.itexc.dom.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    protected ProfileRepository profileRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PrivilegeService privilegeService;

    @Autowired
    protected DBSessionService dbSessionService;

    @Override
    public  void deleteProfile(Long id) throws Throwable {
        Profile profileToDelete = findById(id);
        if (userService.isProfileAttributed(profileToDelete))
            throw new ValidationException(ERROR_CODE.ATTRIBUTED_PROFILE);
        profileRepository.delete(profileToDelete);
    }

    @Override
    public  Profile findById(Long id) throws Throwable {
        return (Profile) profileRepository.findById(id)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_PROFILE));
    }

    @Override
    public Profile findByCode(String code, ERROR_CODE error) throws Throwable {
        return (Profile) profileRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException(error));
    }


    @Override
    public  List<ProfileView> findAllProfiles() {
        return profileRepository.findAllOrderByIdDesc();
    }

    @Override
    public ProfileView getDetailsProfile(Long id) throws Throwable {
        Profile profile = findById(id);
        return new ProfileView(profile);
    }

    public  Profile create(ProfileDto profile) throws Throwable {
        checkProfile(profile);
        Profile createdProfile = new Profile();
        createdProfile.setCode(profile.getCode());
        createdProfile.setName(profile.getName());
        createdProfile.setPrivileges(privilegeService.getPrivilegesFromIdList(profile.getPrivileges()));
        profileRepository.save(createdProfile);
        return createdProfile;
    }

    public  Profile update(Long profileId, ProfileDto profile) throws Throwable {
        Profile updatedProfile = findById(profileId);
        checkProfile(profile);
        updatedProfile.setCode(profile.getCode());
        updatedProfile.setName(profile.getName());
        updatedProfile.setPrivileges(privilegeService.getPrivilegesFromIdList(profile.getPrivileges()));
        profileRepository.save(updatedProfile);
        return updatedProfile;
    }


    private void checkProfile(ProfileDto profile) throws ValidationException {
        if (profileRepository.existsByName(profile.getName()))
            throw new ValidationException(ERROR_CODE.NAME_EXISTS);

        if (profileRepository.existsByCode(profile.getCode()))
            throw new ValidationException(ERROR_CODE.CODE_EXISTS);

        if (profile.getPrivileges() == null || profile.getPrivileges().isEmpty())
            throw new ValidationException(ERROR_CODE.EMPTY_PRIVILEGES_LIST);
    }


    private boolean isPrivilegesListChanged(List<Long> newList, Collection<Privilege> oldList) {
        List<Long> oldPrivileges = oldList.stream().map(CommonEntity::getId).collect(Collectors.toList());
        return !(new HashSet<>(oldPrivileges).equals(new HashSet<>(newList)));
    }
}
