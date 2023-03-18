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
public class AppointmentDto {

    @NotNull(message = "doctor {REQUIRED}")
    private Long doctor;

    @NotNull(message = "patient ... ")
    private Long patient;

    @DateTimeFormat
    private Date dateAndTime;

    @NotBlank
    private String reason;


}
