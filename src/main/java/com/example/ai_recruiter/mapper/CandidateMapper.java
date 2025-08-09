package com.example.ai_recruiter.mapper;

import com.example.ai_recruiter.convertoDTO.Candidate.Request;
import com.example.ai_recruiter.convertoDTO.Candidate.Response;
import com.example.ai_recruiter.entity.Candidate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandidateMapper {
    public static Candidate toEntity(Request request, String base64CV) {
        Candidate entity = new Candidate();
        entity.setFullName(request.getFullName());
        entity.setEmail(request.getEmail());
        entity.setJob_Id(request.getJob_Id());
        entity.setCvBase64(base64CV);
        entity.setCreatedAt(LocalDateTime.now()); // Set creation time
        return entity;
    }

    public static Response toResponse(Candidate entity) {
        Response response = new Response();
        response.setFullName(entity.getFullName());
        response.setEmail(entity.getEmail());
        response.setJob_Id(entity.getJob_Id());
        response.setCvUrl(entity.getCvBase64());
        response.setCreatedAt(entity.getCreatedAt());
        response.setPhone_number(entity.getPhone_number());
        response.setCvBase64(entity.getCvBase64());
        response.setScore(entity.getScore());
        return response;
    }
}
