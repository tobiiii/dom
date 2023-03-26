package com.itexc.dom.repository;

import com.itexc.dom.domain.DocSession;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocSessionRepository extends JpaRepository<DocSession, Long> {
    Session findAllById(Long session);
}
