package com.itexc.dom.controller;

import com.itexc.dom.domain.DTO.ChangePasswordDto;
import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.sevice.UserService;
import com.itexc.dom.utils.JsonResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springdoc.api.annotations.ParameterObject;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('user.list')")
    @GetMapping(value = "/find_all")
    public JsonResponse findAll(@ParameterObject Pageable pageable) {
        Page<UserView> users = userService.getAllUsers(pageable);
        return JsonResponse.builder().data(users)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('user.add')")
    @PostMapping(value = "/add")
    public JsonResponse add(@Valid @RequestBody final UserDto user) throws Throwable {
        UserView newUser = userService.create(user);
        return JsonResponse.builder().data(newUser)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('user.update')")
    @PatchMapping(value = "/update/{userId}")
    public JsonResponse update(
            @NotNull(message = "Utilisateur {REQUIRED}") @PathVariable Long userId,
            @Valid @RequestBody UserDto user) throws Throwable {
        User updatedUser = userService.update(userId, user);
        return JsonResponse.builder().data(new UserView(updatedUser))
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

   @PreAuthorize("hasAnyAuthority(" +
            "'user.update'," +
            "'user.detail'," +
            "'user.delete')")
    @GetMapping(value = "/detail/{userId}")
    public JsonResponse detail(
            @NotNull(message = "Utilisateur {REQUIRED}") @PathVariable("userId") Long userId) throws Throwable {
        UserView user = userService.getUserById(userId);
        return JsonResponse.builder().data(user)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("hasAuthority('user.delete')")
    @DeleteMapping(value = "/delete/{userId}")
    public JsonResponse delete(
            @NotNull(message = "Utilisateur {REQUIRED}") @PathVariable Long userId) throws Throwable {
        userService.delete(userId);
        return JsonResponse.builder().data(null)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping(value = "/admin/change_password")
    public JsonResponse adminChangePassword(@javax.validation.Valid @RequestBody ChangePasswordDto password) throws ValidationException {
        userService.changePassword(password);
        return JsonResponse.builder().data(null)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }


}