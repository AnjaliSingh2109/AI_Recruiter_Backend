package com.example.ai_recruiter.convertoDTO.Candidate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
public class Request {

    private String fullName;
    private String email;
    private String Job_Id;
    private Long phone_number;
}
