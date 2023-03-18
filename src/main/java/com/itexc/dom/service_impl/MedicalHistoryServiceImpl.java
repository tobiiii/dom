package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.MedicalHistoryDto;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.MedicalHistory;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.MedicalHistoryView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.MedicalHistoryRepository;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.MedicalHistoryService;
import com.itexc.dom.sevice.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    MedicalHistoryRepository medicalHistoryRepository;

    @Override
    public MedicalHistoryView create(MedicalHistoryDto medicalHistory) throws Throwable {
        MedicalHistory newMedicalHistory = new MedicalHistory();
        Doctor doctor = doctorService.findById(medicalHistory.getDoctor());
        Patient patient = patientService.findById(medicalHistory.getPatient());
        newMedicalHistory.setDoctor(doctor);
        newMedicalHistory.setPatient(patient);
        newMedicalHistory.setDiagnosis(medicalHistory.getDiagnosis());
        newMedicalHistory.setNotes(medicalHistory.getNotes());
        newMedicalHistory.setTreatment(medicalHistory.getTreatment());
        return new MedicalHistoryView((MedicalHistory) medicalHistoryRepository.save(newMedicalHistory));
    }

    @Override
    public MedicalHistory update(Long medicalHistoryId, MedicalHistoryDto medicalHistory) throws Throwable {
        MedicalHistory medHis = findById(medicalHistoryId);
        medHis.setDiagnosis(medicalHistory.getDiagnosis());
        medHis.setNotes(medicalHistory.getNotes());
        medHis.setTreatment(medicalHistory.getTreatment());
        return (MedicalHistory) medicalHistoryRepository.save(medHis);
    }

    @Override
    public List<MedicalHistoryView> findAll() {
        List<MedicalHistory> medHis = medicalHistoryRepository.findAllOrderByIdDesc();
        return medHis.stream().map(MedicalHistoryView::new).collect(Collectors.toList());

    }

    @Override
    public List<MedicalHistoryView> findAllByPatient(Long patientId) throws Throwable {
        Patient patient = patientService.findById(patientId);
        List<MedicalHistory> medHis = medicalHistoryRepository.findAllByPatient(patient);
        return medHis.stream().map(MedicalHistoryView::new).collect(Collectors.toList());
    }

    @Override
    public MedicalHistoryView getDetails(Long id) throws Throwable {
        return new MedicalHistoryView(findById(id));
    }

    @Override
    public void delete(Long medicalHistoryId) throws Throwable {
        MedicalHistory medicalHistory = findById(medicalHistoryId);
        medicalHistoryRepository.delete(medicalHistory);
    }
    @Override
    public MedicalHistory findById(Long medicalHistoryId) throws Throwable {
        return (MedicalHistory) medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_MEDICAL_HISTORY));

    }
}
