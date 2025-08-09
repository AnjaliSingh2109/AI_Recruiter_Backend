package com.example.ai_recruiter.controller;

import com.example.ai_recruiter.convertoDTO.Candidate.Request;
import com.example.ai_recruiter.convertoDTO.Candidate.Response;
import com.example.ai_recruiter.entity.CVFeedback;
import com.example.ai_recruiter.repository.CVFeedbackRepository;
import com.example.ai_recruiter.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CVFeedbackRepository cvFeedbackRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> addCandidate(
            @ModelAttribute Request candidateRequest,
            @RequestParam("file") MultipartFile file) {
        Response response = candidateService.addCandidate(candidateRequest, file);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<Response>> getAllCandidates() {
        List<Response> candidates = candidateService.getCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/count-by-date")
    public ResponseEntity<Map<String, Long>> getCountByDate() {
        return ResponseEntity.ok(candidateService.getCountByDate());
    }

    // CandidateController.java
    @GetMapping("/score/{candidateId}")
    public ResponseEntity<Double> getScore(@PathVariable Long candidateId) {
        return cvFeedbackRepository.findByCandidateId(candidateId)
                .map(feedback -> ResponseEntity.ok(feedback.getScore()))
                .orElse(ResponseEntity.notFound().build());
    }

}
