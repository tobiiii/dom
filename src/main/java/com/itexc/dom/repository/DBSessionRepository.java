package com.itexc.dom.repository;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.Profile;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DBSessionRepository extends JpaRepository<DBSession, Long> {

    Optional<DBSession> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update DBSession s set s.isActive = false, s.logoutTime = CURRENT_TIMESTAMP " +
           "where s.email = :login and s.isActive = true")
    int closeUserSessions(@Param("login") String login);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update DBSession s set s.isActive = false, s.logoutTime = CURRENT_TIMESTAMP " +
           "where s.user in (select u from User u where u.profile = :profile) and s.isActive = true")
    int closeUserSessionsByProfile(@Param("profile") Profile profile);
}
