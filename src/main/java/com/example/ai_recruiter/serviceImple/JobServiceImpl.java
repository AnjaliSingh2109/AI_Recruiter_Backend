package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.convertoDTO.JobDesc.Request;
import com.example.ai_recruiter.convertoDTO.JobDesc.Response;
import com.example.ai_recruiter.entity.JobDesc;
import com.example.ai_recruiter.mapper.JobMapper;  // your existing Mapper class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ai_recruiter.repository.JobRepository;
import com.example.ai_recruiter.service.JobService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobDescRepository;

    @Override
    public Response saveJobDesc(Request jobRequest) {
        JobDesc jobDesc = JobMapper.toEntity(jobRequest);
        JobDesc savedJobDesc = jobDescRepository.save(jobDesc);
        return JobMapper.toResponse(savedJobDesc);
    }

    @Override
    public List<Response> getAllJobDescs() {
        return jobDescRepository.findAll()
                .stream()
                .map(JobMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Response getJobDescById(Long jobId) {
        JobDesc jobDesc = jobDescRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("JobDesc not found with id: " + jobId));
        return JobMapper.toResponse(jobDesc);
    }

    @Override
    public void deleteJobDesc(Long jobId) {
        jobDescRepository.deleteById(jobId);
    }

    public Map<String, Long> getCountByDate() {
        List<Object[]> results = jobDescRepository.countByDate();
        Map<String, Long> countMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            String date = row[0].toString(); // YYYY-MM-DD
            Long count = ((Number) row[1]).longValue();
            countMap.put(date, count);
        }

        return countMap;
    }
}
