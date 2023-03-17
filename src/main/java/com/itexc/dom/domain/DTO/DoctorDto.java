package com.itexc.dom.domain.DTO;

import com.itexc.dom.validation.ValidEmail;
import com.itexc.dom.validation.ValidName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {

    @ValidName
    private String firstName;

    @ValidName
    private String lastName;

    @ValidEmail
    private String emailAddress;

    @NotNull(message = "Profil {REQUIRED}")
    private Long profile;

    @NotBlank
    private String specialization;

}
