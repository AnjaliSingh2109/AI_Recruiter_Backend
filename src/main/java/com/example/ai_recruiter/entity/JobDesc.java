package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_descriptions")
@Data
public class JobDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long job_id;
    @NotNull
    @Column(nullable = false)
    String role;
    @Column(name = "job_code", unique = true, nullable = false)
    String jobId;
    Integer experience;
    String job_type;
    String job_location;
    String salary;
    String responsibilities;
    @NotNull
    @Column(nullable = false)
    String educational_requirements;
    @NotNull
    @Column(nullable = false)
    String technologies_tools;
    int no_of_openings;
    String posted_by;
    private LocalDateTime createdAt;

}

