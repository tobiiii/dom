package com.itexc.dom.domain.DTO;

import com.itexc.dom.validation.ValidEmail;
import com.itexc.dom.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
