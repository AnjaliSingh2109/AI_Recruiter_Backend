package com.example.ai_recruiter.convertoDTO.LoginandSignup;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String role;
    private String token;
}
