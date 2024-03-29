package com.itexc.dom.domain.DTO;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {

    @NotNull(message = "doctor {REQUIRED}")
    private Long doctor;

    @NotNull(message = "patient ... ")
    private Long patient;

    @NotBlank
    private String medication;

    @NotNull
    private Integer dosage;

    @NotNull
    private Integer frequency;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

}
