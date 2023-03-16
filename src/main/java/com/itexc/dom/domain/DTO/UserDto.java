package com.itexc.dom.domain.DTO;

import com.itexc.dom.domain.User;
import com.itexc.dom.validation.ValidEmail;
import com.itexc.dom.validation.ValidName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @ValidName
    private String firstName;

    @ValidName
    private String lastName;

    @ValidEmail
    private String emailAddress;

}
