package com.example.ai_recruiter.controller;

import com.example.ai_recruiter.serviceImple.HRScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/hr-screening")
public class HRScreeningController {

    @Autowired
    private HRScreeningService hrScreeningService;

    @PostMapping
    public ResponseEntity<List<String>> getHRQuestions(@RequestParam("file") MultipartFile file) {
        List<String> questions = hrScreeningService.generateQuestionsFromCV(file);
        return ResponseEntity.ok(questions);
    }
}
