package com.example.ai_recruiter.service;

import com.example.ai_recruiter.convertoDTO.Candidate.Request;
import com.example.ai_recruiter.convertoDTO.Candidate.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CandidateService {
    public Response addCandidate(Request candidateRequest, MultipartFile file);
    public List<Response> getCandidates();

    Map<String, Long> getCountByDate();
}
