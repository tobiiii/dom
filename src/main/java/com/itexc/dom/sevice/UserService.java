package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserView> getAllUsers(Pageable pageable);

    User create(UserDto user) throws ValidationException;

    User update(Long userId, UserDto user) throws ValidationException;

    User findUserById(Long userId) throws ValidationException;

    UserView getUserById(Long userId) throws Throwable;


    //void changePassword(ChangePasswordDto passwordDto);

    void delete(Long userId) throws ValidationException;

}


