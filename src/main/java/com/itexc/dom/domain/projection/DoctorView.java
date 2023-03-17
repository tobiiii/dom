package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Doctor;
import com.itexc.dom.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorView {


    private Long id;
    private User user;
    private Date created;
    private Date updated;
    private String defaultPass;
    private String specialization;



    public DoctorView(Doctor doctor) {
        this.id = doctor.getId();
        this.user = doctor.getUser();
        this.created = doctor.getCreated();
        this.updated = doctor.getUpdated();
        this.specialization = doctor.getSpecialization();
    }

}
