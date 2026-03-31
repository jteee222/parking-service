package com.assessment.parkingservice.controller;

import com.assessment.parkingservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        if ("admin".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        }

        throw new RuntimeException("Invalid credentials");
    }
}