package com.itexc.dom.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {

    @NotNull(message = "user {REQUIRED}")
    private Long user;

    @NotBlank
    private String specialization;

}
