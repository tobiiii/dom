package com.itexc.dom.domain.projection;

import com.itexc.dom.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileView {

    private Long id;

    private String code;

    private String name;

    private Date created;
    private Date updated;


    public ProfileView(Profile profile) {
        this.id = profile.getId();
        this.code = profile.getCode();
        this.name = profile.getName();
        this.created = profile.getCreated();
        this.updated = profile.getUpdated();
    }

}
