package com.taskmanagement.dto.request;

import com.taskmanagement.entity.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequestDto {

    @NotEmpty(message = "email must not be empty")
    private String email;

    @NotEmpty(message = "password must not be empty")
    private String password;

    @NotNull(message = "user's role must not be empty")
    private UserRole role;
}
