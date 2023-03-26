package com.itexc.dom.repository;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.DoctorSession;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.AppointmentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository <T extends Appointment> extends JpaRepository<T, Long> {

    @Query("select a from Appointment a ORDER BY a.id ASC ")
    List<T> findAllOrderByIdDesc ();


    @Query("select a from Appointment a where a.patient = :patient")
    List<T> findAllByPatient(Patient patient);

    Optional<T> findBySessionAndDateAndDoctor(DoctorSession session, Date date, Doctor doctor);

    Optional<T> findFirstByOrderByDateDesc();

}
