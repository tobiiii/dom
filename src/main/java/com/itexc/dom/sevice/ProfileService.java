package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.ProfileDTO;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.projection.ProfileView;

import java.util.List;

public interface ProfileService {

    Profile create(ProfileDTO profile) throws Throwable;

    Profile update(Long profileId, ProfileDTO profile) throws Throwable;

    List<ProfileView> findAllProfiles();

    ProfileDTO getDetailsProfile(Long id) throws Throwable;

    void deleteProfile(Long profile) throws Throwable;

    Profile findById(Long id) throws Throwable;

}
