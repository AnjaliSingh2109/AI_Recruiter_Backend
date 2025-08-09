package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.convertoDTO.Candidate.Request;
import com.example.ai_recruiter.convertoDTO.Candidate.Response;
import com.example.ai_recruiter.entity.CVFeedback;
import com.example.ai_recruiter.entity.Candidate;
import com.example.ai_recruiter.mapper.CandidateMapper;
import com.example.ai_recruiter.repository.CVFeedbackRepository;
import com.example.ai_recruiter.repository.CandidateRepository;
import com.example.ai_recruiter.service.CandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private CVJDMatch cvjdMatch;

    @Autowired
    CVFeedbackRepository cvFeedbackRepository;

    public Response addCandidate(Request candidateRequest, MultipartFile file){
        // Upload CV file to S3
        try {

            // Upload CV file to S3
            String contentType = file.getContentType();
            if (contentType == null ||
                    !(contentType.equals("application/pdf") ||
                            contentType.equals("application/msword") ||
                            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                throw new IllegalArgumentException("Unsupported file type. Please upload a PDF or Word document.");
            }

            String cvUrl = s3Service.uploadFile(file);

            // Set the uploaded CV URL in request DTO

//            candidateRequest.setCvUrl(cvUrl);
            com.example.ai_recruiter.convertoDTO.CVFeedback.Response jd = cvjdMatch.compareCvWithJd(file, candidateRequest.getJob_Id(),candidateRequest.getEmail());
            log.info("Candidate"+cvUrl+jd);
            // Map DTO to Entity and save
            String base64CV = Base64.getEncoder().encodeToString(file.getBytes());


            Candidate candidateEntity = CandidateMapper.toEntity(candidateRequest, base64CV);
            Candidate savedEntity = candidateRepository.save(candidateEntity);
//
//            CVFeedback feedback = new CVFeedback();
//            feedback.setScore(jd.getScore());
//            feedback.setFeedback(jd.getFeedback());
//            feedback.setCandidate(savedEntity); // Assuming you’ve a `@ManyToOne` mapping
//            cvFeedbackRepository.save(feedback);
            CVFeedback feedback = new CVFeedback();
            feedback.setScore(jd.getScore());
            feedback.setFeedback(jd.getFeedback());
            feedback.setCandidate(savedEntity);

// Set the score in candidate
            savedEntity.setScore(jd.getScore());

// Save candidate first (if needed), then feedback
            candidateRepository.save(savedEntity);
            cvFeedbackRepository.save(feedback);
            // Return response DTO
            return CandidateMapper.toResponse(savedEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CV or save candidate", e);
        }
    }

    @Override
    public List<Response> getCandidates() {
        List<Candidate> allCandidates = candidateRepository.findAll();
        return allCandidates.stream()
                .map(CandidateMapper::toResponse)
                .toList();
    }

    public Map<String, Long> getCountByDate() {
        List<Object[]> results = candidateRepository.countByDate();
        Map<String, Long> countMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            String date = row[0].toString(); // YYYY-MM-DD
            Long count = ((Number) row[1]).longValue();
            countMap.put(date, count);
        }

        return countMap;
    }
}
