package com.example.ai_recruiter.repository;

import com.example.ai_recruiter.convertoDTO.CVFeedback.Response;
import com.example.ai_recruiter.entity.CVFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CVFeedbackRepository extends JpaRepository<CVFeedback,Long> {
    Optional<CVFeedback> findByCandidateId(Long candidateId);
}
