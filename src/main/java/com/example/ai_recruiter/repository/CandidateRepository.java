package com.example.ai_recruiter.repository;

import com.example.ai_recruiter.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    @Query(value = """
        SELECT DATE(created_at) AS date, COUNT(*) AS total
        FROM candidate
        GROUP BY DATE(created_at)
        ORDER BY DATE(created_at)
        """, nativeQuery = true)
    List<Object[]> countByDate();
}
