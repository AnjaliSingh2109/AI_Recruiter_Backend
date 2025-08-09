package com.example.ai_recruiter.convertoDTO.LoginandSignup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// CompanySignupRequest.java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySignupRequest {
    private String companyName;
    private String companyId;
    private String email;
    private String number;
    private String password;
}
