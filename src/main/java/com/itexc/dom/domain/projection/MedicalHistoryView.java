package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.MedicalHistory;
import com.itexc.dom.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryView {

    private Doctor doctor;

    private Patient patient;

    private String diagnosis;
    private String treatment;
    private String notes;

    public MedicalHistoryView(MedicalHistory medicalHistory) {
        this.doctor = medicalHistory.getDoctor();
        this.patient = medicalHistory.getPatient();
        this.diagnosis = medicalHistory.getDiagnosis();
        this.treatment = medicalHistory.getTreatment();
        this.notes = medicalHistory.getNotes();
    }
}
