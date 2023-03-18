package com.itexc.dom.repository;

import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.PatientView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository <T extends Patient> extends JpaRepository<T, Long> {

    @Query("select p from Patient p ORDER BY p.id ASC ")
    List<PatientView> findAllOrderByIdDesc ();

}
