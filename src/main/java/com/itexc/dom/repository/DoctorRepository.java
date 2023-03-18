package com.itexc.dom.repository;

import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.projection.DoctorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DoctorRepository<T extends Doctor> extends JpaRepository<T, Long> {

    @Query("select d from Doctor d ORDER BY d.id ASC ")
    List<DoctorView> findAllOrderByIdDesc ();

}
