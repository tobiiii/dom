package com.itexc.dom.domain.DTO;

import com.itexc.dom.validation.ValidEmail;
import com.itexc.dom.validation.ValidName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    @ValidName
    private String firstName;

    @ValidName
    private String lastName;

    @ValidEmail
    private String emailAddress;

    @NotNull(message = "Profil {REQUIRED}")
    private Long profile;

    @DateTimeFormat
    private Date dateOfBirth;

    @NotBlank
    private String address;


}
