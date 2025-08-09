package com.example.ai_recruiter.service;

import com.example.ai_recruiter.convertoDTO.JobDesc.Request;
import com.example.ai_recruiter.convertoDTO.JobDesc.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface JobService {

    Response saveJobDesc(Request jobRequest);
    List<Response> getAllJobDescs();
    Response getJobDescById(Long jobId);
    void deleteJobDesc(Long jobId);

    Map<String, Long> getCountByDate();
}
