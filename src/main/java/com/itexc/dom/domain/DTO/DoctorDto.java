package com.itexc.dom.domain.DTO;

import com.itexc.dom.validation.ValidEmail;
import com.itexc.dom.validation.ValidName;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @NotBlank
    private String specialization;

}
