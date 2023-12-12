package com.taskmanagement.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthRequestDto {

    @NotEmpty(message = "login must not be empty")
    private String login;

    @NotEmpty(message = "password must not be empty")
    private String password;
}
