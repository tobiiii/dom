package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorView {


    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Boolean isLocked;
    private Date created;
    private Date updated;
    private String defaultPass;
    private String specialization;



    public DoctorView(Doctor doctor) {
        this.id = doctor.getId();
        this.firstName = doctor.getFirstName();
        this.lastName = doctor.getLastName();
        this.emailAddress = doctor.getEmailAddress();
        this.created = doctor.getCreated();
        this.updated = doctor.getUpdated();
        this.specialization = doctor.getSpecialization();
    }

}
