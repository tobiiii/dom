package com.itexc.dom.repository;

import com.itexc.dom.domain.DoctorSession;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocSessionRepository extends JpaRepository<DoctorSession, Long> {
    Session findAllById(Long session);
}
