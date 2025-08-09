package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalScreening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String jobId;

    @ElementCollection
    @CollectionTable(name = "technical_screening_questions", joinColumns = @JoinColumn(name = "technical_screening_id"))
    @Column(name = "question", length = 2000)
    private List<String> questions;
}
