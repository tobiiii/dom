package com.itexc.dom.domain.DTO;

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

    @NotNull(message = "user {REQUIRED}")
    private Long user;

    @DateTimeFormat
    private Date dateOfBirth;

    @NotBlank
    private String address;


}
