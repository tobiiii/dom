package com.itexc.dom.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class MedicalHistory {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "doctor_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_medical_history_doctor"))
    private Doctor doctor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_medical_history_patient"))
    private Patient patient;

    @NotBlank
    @Column(nullable = false)
    private String treatment;

    @NotBlank
    @Column(nullable = false)
    private String diagnosis;

    @NotBlank
    @Column(nullable = false)
    private String notes;


}
