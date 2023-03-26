package com.itexc.dom.domain.DTO;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    @NotNull(message = "doctor {REQUIRED}")
    private Long doctor;

    @NotNull(message = "patient ... ")
    private Long patient;

    @NotNull(message = "session ... ")
    private Long session;

    @DateTimeFormat
    private Date date;

    @NotBlank
    private String reason;


}
