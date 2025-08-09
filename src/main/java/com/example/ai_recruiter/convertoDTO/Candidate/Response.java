package com.example.ai_recruiter.convertoDTO.Candidate;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private String fullName;
    private String email;
    private String Job_Id;
    private String cvUrl;
    private LocalDateTime createdAt;
    private Long phone_number;
    private double score;
    private String cvBase64;

}
