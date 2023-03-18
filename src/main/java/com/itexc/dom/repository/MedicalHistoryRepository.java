package com.itexc.dom.repository;

import com.itexc.dom.domain.MedicalHistory;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.MedicalHistoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository <T extends MedicalHistory> extends JpaRepository<T, Long> {

    @Query("select m from MedicalHistory m ORDER BY m.id ASC ")
    List<MedicalHistoryView> findAllOrderByIdDesc ();

    @Query("select m from MedicalHistory m where m.patient = :patient")
    List<MedicalHistory> findAllByPatient(Patient patient);

}
