package com.itexc.dom.domain.DTO;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryDto {

    @NotNull(message = "doctor {REQUIRED}")
    private Long doctor;

    @NotNull(message = "patient ... ")
    private Long patient;

    @NotBlank
    private String notes;
    @NotBlank
    private String diagnosis;
    @NotBlank
    private String treatment;

}
