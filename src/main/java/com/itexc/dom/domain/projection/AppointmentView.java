package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Appointment;
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

    private Date date;

    private DocSessionView session;

    private String reason;

    private String status;

    public AppointmentView(Appointment appointment) {
        this.id = appointment.getId();
        this.doctor = new DoctorView(appointment.getDoctor());
        this.patient = new PatientView(appointment.getPatient());
        this.date = appointment.getDate();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.session = new DocSessionView(appointment.getSession(),true);
    }
}
