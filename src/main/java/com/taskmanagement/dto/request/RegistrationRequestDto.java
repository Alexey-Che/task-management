package com.taskmanagement.dto.request;

import com.taskmanagement.entity.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequestDto {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    @NotNull
    private UserRole role;
}
