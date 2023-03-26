package com.itexc.dom.sevice;

import com.itexc.dom.domain.projection.DoctorView;
import com.itexc.dom.domain.DTO.DoctorDto;
import com.itexc.dom.domain.Doctor;

import java.util.List;

public interface DoctorService {

    DoctorView create(DoctorDto doctor) throws Throwable;

    Doctor update(Long doctorId, DoctorDto doctor) throws Throwable;

    List<DoctorView> findAll();

    DoctorView getDetails(Long id) throws Throwable;

    void delete(Long doctorId) throws Throwable;

    Doctor findById(Long id) throws Throwable;

}
