package com.taskmanagement.controller;

import com.taskmanagement.dto.request.AuthRequestDto;
import com.taskmanagement.dto.request.RegistrationRequestDto;
import com.taskmanagement.dto.response.AuthResponseDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.entity.jwt.JwtProvider;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")   //регистрация нового пользователя
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequestDto registrationRequest) {
        var newUser = User.builder()
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .role(registrationRequest.getRole())
                .build();
        if (userService.findByEmail(registrationRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exist");
        }
        userService.saveUser(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")  //получение jwt токена зарегистрированным пользователем
    public ResponseEntity<?> auth(@RequestBody @Valid AuthRequestDto request) {
        val user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val token = jwtProvider.createTokenByEmail(user.getEmail());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
