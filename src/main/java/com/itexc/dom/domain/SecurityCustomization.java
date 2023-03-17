package com.itexc.dom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Data
@EqualsAndHashCode()
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class SecurityCustomization extends CommonEntity{

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    private String credential;

}
