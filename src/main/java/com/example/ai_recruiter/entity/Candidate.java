package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    @Column(unique = true)
    private String email;

    private String Job_Id;
    private Long phone_number;

    // This will store the S3 file name or URL after uploading
    private String cvUrl;
    LocalDateTime createdAt;
    private double score;
    @Column(columnDefinition = "TEXT")
    private String cvBase64;
    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL)
    private CVFeedback cvFeedback;

}
