package com.itexc.dom.sevice;

import com.itexc.dom.domain.DTO.MedicalHistoryDto;
import com.itexc.dom.domain.MedicalHistory;
import com.itexc.dom.domain.projection.MedicalHistoryView;

import java.util.List;

public interface MedicalHistoryService {
    MedicalHistoryView create(MedicalHistoryDto medicalHistory) throws Throwable;

    MedicalHistory update(Long medicalHistoryId, MedicalHistoryDto medicalHistory) throws Throwable;

    List<MedicalHistoryView> findAll();

    List<MedicalHistoryView> findAllByPatient(Long patientId) throws Throwable;

    MedicalHistoryView getDetails(Long id) throws Throwable;

    void delete(Long medicalHistoryId) throws Throwable;

    MedicalHistory findById(Long medicalHistoryId) throws Throwable;
}
