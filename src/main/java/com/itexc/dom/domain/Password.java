package com.itexc.dom.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Password extends SecurityCustomization {

    private Boolean isTemporary;
}
