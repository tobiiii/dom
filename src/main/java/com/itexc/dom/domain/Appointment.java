package com.itexc.dom.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Appointment extends  CommonEntity{

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "doctor_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_appointment_doctor"))
    private Doctor doctor;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_appointment_patient"))
    private Patient patient;

    @DateTimeFormat
    @Column(nullable = false)
    private Date dateAndTime;

    @NotBlank
    @Column(nullable = false)
    private String reason;



}
