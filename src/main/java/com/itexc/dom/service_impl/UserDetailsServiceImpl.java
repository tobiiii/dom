package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.User;
import com.itexc.dom.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //    @Transactional
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<User> userOptional = userRepository.findByEmailAddressIgnoreCase(email);
        // check if email is valid
        userOptional.orElseThrow(() -> new UsernameNotFoundException(""));

        return UserDetailsImpl.buildFromUser(userOptional.get());

    }

    //    @Transactional
    public UserDetails loadUserByUsername(final String email, DBSession dbSession)
            throws UsernameNotFoundException {
        final Optional<User> userOptional = userRepository.findByEmailAddressIgnoreCase(email);
        // check if username is valid
        userOptional.orElseThrow(() -> new UsernameNotFoundException(""));

        return UserDetailsImpl.buildFromUser(userOptional.get(), dbSession);

    }
}
