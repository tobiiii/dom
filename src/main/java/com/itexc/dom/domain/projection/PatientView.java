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
    private UserView user;
    private Date created;
    private Date updated;
    private String defaultPass;
    private Date dateOfBirth;
    private String address;


    public PatientView(Patient patient) {
        this.id = patient.getId();
        this.user = new UserView(patient.getUser());
        this.created = patient.getCreated();
        this.updated = patient.getUpdated();
        this.address = patient.getAddress();
        this.dateOfBirth = patient.getDateOfBirth();
    }
}