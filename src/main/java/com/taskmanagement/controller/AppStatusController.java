package com.taskmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppStatusController {

    @GetMapping("isAlive")
    public ResponseEntity<?> isAlive() {
        return ResponseEntity.ok().build();
    }
}
