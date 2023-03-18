package com.itexc.dom.sevice;

import com.itexc.dom.domain.Prescription;
import com.itexc.dom.domain.DTO.PrescriptionDto;
import com.itexc.dom.domain.projection.PrescriptionView;

import java.util.List;

public interface PrescriptionService {

    PrescriptionView create(PrescriptionDto prescription) throws Throwable;

    Prescription update(Long prescriptionId, PrescriptionDto prescription) throws Throwable;

    List<PrescriptionView> findAll();

    List<PrescriptionView> findAllByPatient(Long patientId) throws Throwable;

    PrescriptionView getDetails(Long id) throws Throwable;

    void delete(Long prescriptionId) throws Throwable;

    Prescription findById(Long PrescriptionId) throws Throwable;
}
