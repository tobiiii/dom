package com.itexc.dom.repository;

import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.projection.PrivilegeView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository<T extends Privilege> extends JpaRepository<T, Long> {

    List<PrivilegeView> findAllPrivilegeView();


    List<Privilege> findByIdIn(List<Long> id);

    Optional<T> findByCode(String code);

    List<T> findByCodeIn(List<String> code);
}
