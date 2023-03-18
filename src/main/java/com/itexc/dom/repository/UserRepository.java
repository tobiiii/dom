package com.itexc.dom.repository;

import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

     Optional<User> findByEmailAddressIgnoreCase(String email);

     boolean existsByEmailAddressIgnoreCase(String emailAddress);

     Page<User> findAll(Pageable pageable);

     boolean existsByProfile(Profile profile);

}
