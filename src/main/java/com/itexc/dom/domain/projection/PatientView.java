package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientView {


    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Boolean isLocked;
    private Date created;
    private Date updated;
    private String defaultPass;
    private Date dateOfBirth;
    private String address;


    public PatientView(Patient patient) {
        this.id = patient.getId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.emailAddress = patient.getEmailAddress();
        this.created = patient.getCreated();
        this.updated = patient.getUpdated();
        this.address = patient.getAddress();
        this.dateOfBirth = patient.getDateOfBirth();
    }
}