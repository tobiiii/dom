package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Appointment;
import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentView {

    private Long id;
    private DoctorView doctor;

    private PatientView patient;

    private Date dateAndTime;

    private String reason;

    private String status;

    public AppointmentView(Appointment appointment) {
        this.id = appointment.getId();
        this.doctor = new DoctorView(appointment.getDoctor());
        this.patient = new PatientView(appointment.getPatient());
        this.dateAndTime = appointment.getDateAndTime();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
    }
}
