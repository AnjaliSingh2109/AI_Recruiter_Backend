package com.example.ai_recruiter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class HRScreening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @ElementCollection
    @CollectionTable(name = "hrscreening_questions", joinColumns = @JoinColumn(name = "hrscreening_id"))
    @Column(name = "questions", length = 2000) // ⬅️ Add length here!
    private List<String> questions;
}
