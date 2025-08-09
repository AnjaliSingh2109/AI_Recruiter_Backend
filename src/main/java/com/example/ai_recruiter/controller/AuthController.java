package com.example.ai_recruiter.controller;// --- 8. AuthController ---

import com.example.ai_recruiter.convertoDTO.LoginandSignup.CompanySignupRequest;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.LoginRequest;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.LoginResponse;
import com.example.ai_recruiter.convertoDTO.LoginandSignup.UserSignupRequest;
import com.example.ai_recruiter.serviceImple.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/user/signup")
    public String userSignup(@RequestBody UserSignupRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/company/signup")
    public String companySignup(@RequestBody CompanySignupRequest request) {
        return authService.registerCompany(request);
    }

    @PostMapping("/user/login")
    public LoginResponse userLogin(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    @PostMapping("/company/login")
    public LoginResponse companyLogin(@RequestBody LoginRequest request) {
        return authService.loginCompany(request);
    }
}