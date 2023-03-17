package com.itexc.dom.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Doctor extends  User{

    @NotBlank
    @Column(nullable = false)
    private String specialization;


}
