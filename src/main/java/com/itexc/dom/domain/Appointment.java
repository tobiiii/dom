package com.itexc.dom.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
public class Appointment extends  CommonEntity{

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "doctor_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_appointment_doctor"))
    private Doctor doctor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "patient_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_appointment_patient"))
    private Patient patient;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "session_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_appointment_session"))
    private DoctorSession session;


    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @NotBlank
    @Column(nullable = false)
    private String reason;

    @NotBlank
    @Column(nullable = false)
    private String status;

}
