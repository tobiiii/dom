package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.ProfileDto;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.ProfileView;

import java.util.List;

public interface ProfileService {

    Profile create(ProfileDto profile) throws Throwable;

    Profile update(Long profileId, ProfileDto profile) throws Throwable;

    List<ProfileView> findAllProfiles();

    ProfileView getDetailsProfile(Long id) throws Throwable;

    void deleteProfile(Long profile) throws Throwable;

    Profile findById(Long id) throws Throwable;

    Profile findByCode(String code, ERROR_CODE error) throws Throwable;

}
