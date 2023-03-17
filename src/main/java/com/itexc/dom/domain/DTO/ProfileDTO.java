package com.itexc.dom.domain.DTO;

import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.projection.PrivilegeView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    @NotNull(message = "Identifiant {REQUIRED}")
    private Long id;

    @NotNull(message = "Code {REQUIRED}")
    @NotBlank(message = "Code {REQUIRED}")
    @NotEmpty(message = "Code {REQUIRED}")
    private String code;

    @NotNull(message = "Nom {REQUIRED}")
    @NotBlank(message = "Nom {REQUIRED}")
    @NotEmpty(message = "Nom {REQUIRED}")
    private String name;

    @NotNull(message = "Couleur {REQUIRED}")
    @NotBlank(message = "Couleur {REQUIRED}")
    @NotEmpty(message = "Couleur {REQUIRED}")
    private String color;

    private Date created;
    private Date updated;

    @NotNull(message = "Privilèges {REQUIRED}")
    @Size(min = 1, message = "Privilèges {REQUIRED}")
    private List<PrivilegeView> privileges;


    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.code = profile.getCode();
        this.name = profile.getName();
        this.created = profile.getCreated();
        this.updated = profile.getUpdated();
    }
}