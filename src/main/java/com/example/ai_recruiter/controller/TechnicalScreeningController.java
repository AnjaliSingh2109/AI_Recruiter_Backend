package com.example.ai_recruiter.controller;

import com.example.ai_recruiter.serviceImple.TechnicalScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/technical-screening")
public class TechnicalScreeningController {

    @Autowired
    private TechnicalScreeningService service;

    @PostMapping
    public ResponseEntity<?> getQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobId") String jobId) {
        try {
            List<String> questions = service.generateQuestionsFromCVAndJD(file, jobId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating technical questions: " + e.getMessage());
        }
    }
}
