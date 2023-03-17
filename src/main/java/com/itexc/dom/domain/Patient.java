package com.itexc.dom.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Patient extends  User{

    @DateTimeFormat
    @Column(nullable = false)
    private Date dateOfBirth;

    @NotBlank
    @Column(nullable = false)
    private String address;

}
