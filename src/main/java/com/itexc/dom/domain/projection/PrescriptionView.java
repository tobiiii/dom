package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Prescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionView {


    private Long id;

    private DoctorView doctor;

    private PatientView patient;

    private String medication;

    private Integer dosage;

    private Integer frequency;

    private Date startDate;

    private Date endDate;

    public PrescriptionView(Prescription prescription) {
        this.id = prescription.getId();
        this.doctor = new DoctorView(prescription.getDoctor());
        this.patient = new PatientView(prescription.getPatient());
        this.medication = prescription.getMedication();
        this.dosage = prescription.getDosage();
        this.frequency = prescription.getFrequency();
        this.startDate = prescription.getStartDate();
        this.endDate = prescription.getEndDate();
    }
}
