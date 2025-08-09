package com.example.ai_recruiter.controller;

import com.example.ai_recruiter.convertoDTO.JobDesc.Request;
import com.example.ai_recruiter.convertoDTO.JobDesc.Response;
import com.example.ai_recruiter.serviceImple.GeminiService;
import com.example.ai_recruiter.serviceImple.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ai_recruiter.service.JobService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private GeminiService geminiService;
    @Autowired
    private S3Service s3Service;

    @PostMapping("/job-desc")
    public ResponseEntity<Response> createJobDesc(@RequestBody Request jobRequest) {
        Response response = jobService.saveJobDesc(jobRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, world!";
    }

    @GetMapping("/job-desc")
    public ResponseEntity<List<Response>> getAllJobDescs() {
        List<Response> responses = jobService.getAllJobDescs();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/job-desc/{id}")
    public ResponseEntity<Response> getJobDescById(@PathVariable Long id) {
        Response response = jobService.getJobDescById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/job-desc/{id}")
    public ResponseEntity<Void> deleteJobDesc(@PathVariable Long id) {
        jobService.deleteJobDesc(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateJobDescription(@RequestBody Request req) {
        String prompt = """
            Write a professional and detailed job description for the following and no need to give apply link as we have already space for it:

            Role: %s
            Location: %s
            Type: %s
            Salary: %s
            Responsibilities: %s
            Educational Requirements: %s
            Technologies/Tools: %s
            Number of Openings: %d
            Experience:%d
            Job Id:%s
            """
                .formatted(
                        req.getRole(),
                        req.getJobLocation(),
                        req.getJobType(),
                        req.getSalary(),
                        req.getResponsibilities(),
                        req.getEducationalRequirements(),
                        req.getTechnologiesTools(),
                        req.getNoOfOpenings(),
                        req.getExperience(),
                        req.getJob_Id()
                );

        String aiResponse = geminiService.generateJD(prompt,req);
        return ResponseEntity.ok(aiResponse);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = s3Service.uploadFile(file);
            return ResponseEntity.ok("Uploaded Successfully. File name: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-date")
    public ResponseEntity<Map<String, Long>> getCountByDate() {
        return ResponseEntity.ok(jobService.getCountByDate());
    }

}
