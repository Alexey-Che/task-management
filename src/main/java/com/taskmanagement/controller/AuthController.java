package com.taskmanagement.controller;

import com.taskmanagement.config.jwt.JwtProvider;
import com.taskmanagement.dto.request.AuthRequestDto;
import com.taskmanagement.dto.request.RegistrationRequestDto;
import com.taskmanagement.dto.response.AuthResponseDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")   //регистрация нового пользователя
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequestDto registrationRequest) {
        var newUser = User.builder()
                .login(registrationRequest.getLogin())
                .password(registrationRequest.getPassword())
                .role(registrationRequest.getRole())
                .build();
        if (userService.findByLogin(registrationRequest.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exist");
        }
        userService.saveUser(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")  //получение jwt токена зарегистрированным пользователем
    public ResponseEntity<?> auth(@RequestBody @Valid AuthRequestDto request) {
        val user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        val token = jwtProvider.createToken(user.getLogin());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        val result = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();

        for (val fieldError : result.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errors;
    }
}
