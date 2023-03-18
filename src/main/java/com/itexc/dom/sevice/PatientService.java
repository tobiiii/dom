package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.PatientDto;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.projection.PatientView;

import java.util.List;

public interface PatientService {
    PatientView create(PatientDto patient) throws Throwable;

    Patient update(Long patientId, PatientDto patient) throws Throwable;

    List<PatientView> findAll();

    PatientView getDetails(Long id) throws Throwable;

    void delete(Long patientId) throws Throwable;

    Patient findById(Long id) throws Throwable;

}
