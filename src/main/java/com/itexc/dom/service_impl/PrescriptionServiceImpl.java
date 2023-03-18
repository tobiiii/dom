package com.itexc.dom.service_impl;

import com.itexc.dom.domain.DTO.PrescriptionDto;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Patient;
import com.itexc.dom.domain.Prescription;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.domain.projection.PrescriptionView;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.repository.PrescriptionRepository;
import com.itexc.dom.sevice.DoctorService;
import com.itexc.dom.sevice.PatientService;
import com.itexc.dom.sevice.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {


    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    PrescriptionRepository prescriptionRepository;


    @Override
    public PrescriptionView create(PrescriptionDto prescription) throws Throwable {
        Prescription newPrescription = new Prescription();
        Doctor doctor = doctorService.findById(prescription.getDoctor());
        Patient patient = patientService.findById(prescription.getPatient());
        newPrescription.setDoctor(doctor);
        newPrescription.setPatient(patient);
        newPrescription.setDosage(prescription.getDosage());
        newPrescription.setMedication(prescription.getMedication());
        newPrescription.setFrequency(prescription.getFrequency());
        newPrescription.setStartDate(prescription.getStartDate());
        newPrescription.setEndDate(prescription.getEndDate());
        return new PrescriptionView((Prescription) prescriptionRepository.save(newPrescription));

    }

    @Override
    public Prescription update(Long prescriptionId, PrescriptionDto prescription) throws Throwable {
        Prescription prescriptionToUpdate = findById(prescriptionId);
        prescriptionToUpdate.setDosage(prescription.getDosage());
        prescriptionToUpdate.setMedication(prescription.getMedication());
        prescriptionToUpdate.setFrequency(prescription.getFrequency());
        prescriptionToUpdate.setStartDate(prescription.getStartDate());
        prescriptionToUpdate.setEndDate(prescription.getEndDate());
        return (Prescription) prescriptionRepository.save(prescriptionToUpdate);
    }

    @Override
    public List<PrescriptionView> findAll() {
        return prescriptionRepository.findAllOrderByIdDesc();
    }

    @Override
    public List<PrescriptionView> findAllByPatient(Long patientId) throws Throwable {
        Patient patient =  patientService.findById(patientId);
        List<Prescription> pres = prescriptionRepository.findAllByPatient(patient);
        return pres.stream().map(PrescriptionView::new).collect(Collectors.toList());
    }

    @Override
    public PrescriptionView getDetails(Long id) throws Throwable {
        return new PrescriptionView(findById(id));
    }

    @Override
    public void delete(Long prescriptionId) throws Throwable {
        Prescription prescription = findById(prescriptionId);
        prescriptionRepository.delete(prescription);
    }

    @Override
    public Prescription findById(Long PrescriptionId) throws Throwable {
        return (Prescription) prescriptionRepository.findById(PrescriptionId)
                .orElseThrow(() -> new ValidationException(ERROR_CODE.INEXISTANT_PRESCREPTION));

    }
}
