package com.example.ai_recruiter.convertoDTO.LoginandSignup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// com.example.ai_recruiter.convertoDTO.LoginandSignup.UserSignupRequest.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequest {
    private String fullName;
    private String email;
    private String number;
    private String password;
}
