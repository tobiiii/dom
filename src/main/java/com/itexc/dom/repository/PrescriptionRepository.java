package com.itexc.dom.repository;

import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.Prescription;
import com.itexc.dom.domain.projection.PrescriptionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository <T extends Prescription> extends JpaRepository<T, Long> {

    @Query("select p from Prescription p ORDER BY p.id ASC ")
    List<PrescriptionView> findAllOrderByIdDesc ();

    @Query("select a from Prescription a where a.patient = :patient")
    List<Prescription> findAllByPatient(Patient patient);

}
