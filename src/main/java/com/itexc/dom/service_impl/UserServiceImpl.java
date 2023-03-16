package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.UserDto;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.UserView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.UserRepository;
import com.itexc.dom.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Page<UserView> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserView::new);
    }

    @Override
    public User create(UserDto user) throws ValidationException {
        User newUser = new User();
        if (userRepository.existsByEmailAddressIgnoreCase(user.getEmailAddress().toLowerCase())) {
            throw new ValidationException(ERROR_CODE.EMAIL_EXISTS);
        }
        newUser.setLastName(user.getLastName());
        newUser.setFirstName(user.getFirstName());
        newUser.setEmailAddress(user.getEmailAddress());
        return userRepository.save(newUser);
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


}
