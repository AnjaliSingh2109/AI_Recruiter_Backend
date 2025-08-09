package com.example.ai_recruiter.repository;

import com.example.ai_recruiter.entity.JobDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<JobDesc,Long> {
    Optional<JobDesc> findByJobId(String jobCode);
    @Query(value = """
        SELECT DATE(created_at) AS date, COUNT(*) AS total
        FROM job_descriptions
        GROUP BY DATE(created_at)
        ORDER BY DATE(created_at)
        """, nativeQuery = true)
    List<Object[]> countByDate();
}
