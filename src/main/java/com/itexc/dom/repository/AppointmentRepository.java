package com.itexc.dom.repository;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.projection.DoctorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository <T extends Appointment> extends JpaRepository<T, Long> {

    @Query("select a from Appointment a ORDER BY a.id ASC ")
    List<DoctorView> findAllOrderByIdDesc ();

}
