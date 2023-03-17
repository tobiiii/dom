package com.itexc.dom.domain.projection;

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

    private Doctor doctor;

    private Patient patient;

    private Date dateAndTime;

    private String reason;

}
