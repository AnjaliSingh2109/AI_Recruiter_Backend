package com.example.ai_recruiter.convertoDTO.JobDesc;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Request{
//    private Long jobId;
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

}
