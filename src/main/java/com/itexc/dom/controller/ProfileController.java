package com.itexc.dom.controller;

import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.DTO.ProfileDto;
import com.itexc.dom.domain.projection.ProfileView;
import com.itexc.dom.sevice.ProfileService;
import com.itexc.dom.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
@Validated
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PreAuthorize("hasAnyAuthority(" +
                  "'user.list'," +
                  "'user.add'," +
                  "'user.update'," +
                  "'user.detail'," +
                  "'user.delete'," +
                  "'profile.list'," +
                  "'profile.add'," +
                  "'profile.update'," +
                  "'profile.detail'," +
                  "'profile.delete')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll() {
        List<ProfileView> profiles = profileService.findAllProfiles();
        return JsonResponse.builder().data(profiles).status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('profile.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final ProfileDto profile) throws Throwable {
        Profile createdProfile = profileService.create(profile);
        return new JsonResponse(new ProfileView(createdProfile));
    }

    @PreAuthorize("hasAnyAuthority(" +
                  "'profile.detail'," +
                  "'profile.update')")
    @GetMapping(value = "/detail/{profileId}")
    public JsonResponse detail(
            @NotNull(message = "Profil {REQUIRED}") @PathVariable Long profileId) throws Throwable {
        ProfileDto profile = profileService.getDetailsProfile(profileId);
        return new JsonResponse(profile);
    }

    @PreAuthorize("hasAuthority('profile.update')")
    @PutMapping(value = "/update/{profileId}")
    public JsonResponse update(
            @NotNull(message = "Profil {REQUIRED}") @PathVariable Long profileId,
            @Valid @RequestBody ProfileDto profile) throws Throwable {
        Profile updatedProfile = profileService.update(profileId, profile);
        return new JsonResponse(new ProfileView(updatedProfile));
    }


    @PreAuthorize("hasAuthority('profile.delete')")
    @DeleteMapping(value = "/delete/{profileId}")
    public JsonResponse remove(
            @NotNull(message = "Profil {REQUIRED}") @PathVariable Long profileId) throws Throwable {
        profileService.deleteProfile(profileId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

}
