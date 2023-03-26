package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.AuthenticationRequest;
import com.itexc.dom.domain.DTO.AuthenticationResponse;
import com.itexc.dom.domain.DTO.ChangePasswordDto;
import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.domain.projection.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    Page<UserView> getAllUsers(Pageable pageable);

    UserView create(UserDto user) throws Throwable;

    User update(Long userId, UserDto user) throws ValidationException;

    User findUserById(Long userId) throws ValidationException;

    UserView getUserById(Long userId) throws Throwable;


    void changePassword(ChangePasswordDto passwordDto) throws ValidationException;

    void delete(Long userId) throws ValidationException;

    User getConnectedUser() throws ValidationException;

    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest,
                                          HttpServletRequest request, HttpServletResponse response) throws Throwable;

    AuthenticationResponse renewSession(String tokenId, HttpServletRequest request) throws Throwable;


    User checkEmail(String email) throws ValidationException;

    boolean isProfileAttributed(Profile profile);

}


