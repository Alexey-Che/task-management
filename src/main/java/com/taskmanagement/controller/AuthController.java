package com.taskmanagement.controller;

import com.taskmanagement.config.jwt.JwtProvider;
import com.taskmanagement.dto.request.AuthRequestDto;
import com.taskmanagement.dto.request.RegistrationRequestDto;
import com.taskmanagement.dto.response.AuthResponseDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")   //регистрация нового пользователя
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequestDto registrationRequest) {
        var user = new User();
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());
        if (userService.findByLogin(registrationRequest.getLogin()) == null) {
            userService.saveUser(user);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")  //получение jwt токена зарегистрированным пользователем
    public ResponseEntity<?> auth(@RequestBody @Valid AuthRequestDto request) {
        val userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        val token = jwtProvider.createToken(userEntity.getLogin());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
