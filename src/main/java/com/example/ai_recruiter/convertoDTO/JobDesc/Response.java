package com.example.ai_recruiter.convertoDTO.JobDesc;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class Response {
    private Long jobId;
    private String role;
    private String job_Id;
    private int experience;
    private String jobType;
    private String jobLocation;
    private String salary;
    private String responsibilities;
    private String educationalRequirements;
    private String technologiesTools;
    private int noOfOpenings;
    private String postedBy;
    private LocalDateTime createdAt;
    private String generatedDescription;
}
