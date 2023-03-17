package com.itexc.dom.domain;

import com.itexc.dom.domain.CommonEntity;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Prescription extends CommonEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "doctor_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_prescription_doctor"))
    private Doctor doctor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_prescription_patient"))
    private Patient patient;

    @NotBlank
    @Column(nullable = false)
    private String medication;

    @NotNull
    @Column(nullable = false)
    private Integer dosage;

    @NotNull
    @Column(nullable = false)
    private Integer frequency;


    @NotNull
    @Column(nullable = false)
    private Date startDate;

    @NotNull
    @Column(nullable = false)
    private Date endDate;


}
