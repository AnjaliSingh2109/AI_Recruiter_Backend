package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.convertoDTO.CVFeedback.Response;
import com.example.ai_recruiter.entity.CVFeedback;
import com.example.ai_recruiter.entity.JobDesc;
import com.example.ai_recruiter.repository.CVFeedbackRepository;
import com.example.ai_recruiter.repository.JobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Data
public class CVJDMatch {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    CVFeedbackRepository cvFeedbackRepository;

    @Autowired
    private EmailService emailService;

    @Value("${gemini.api.key}")
    private String apiKey;

    public com.example.ai_recruiter.convertoDTO.CVFeedback.Response compareCvWithJd(MultipartFile file, String jobCode,String Email) {
        try {

            String resumeText = extractTextFromFile(file);
            log.info("Match 1");
            Optional<JobDesc> jobOpt = jobRepository.findByJobId(jobCode);
            if (jobOpt.isEmpty()) {
                throw new RuntimeException("Job not found with code: " + jobCode);
            }

            JobDesc jd = jobOpt.get();
            String fullJD = buildFullJD(jd);

            String prompt = String.format("""
                Compare the following candidate resume with the full job description and comparing if the candidate has relevant skills,technologies and tools and experience  and provide:
                - A match score (out of 100)
                - Feedback highlighting strengths and gaps in brief

                Resume:
                %s

                Job Description:
                %s

                Respond in JSON format like:
                {
                  "score": 78,
                  "feedback": "Candidate has relevant tech stack but lacks team leadership."
                }
                """, resumeText, fullJD);
            return callGemini(prompt,Email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("CV-JD comparison failed", e);
        }
    }

    private String buildFullJD(JobDesc jd) {
        return String.format("""
                Role: %s
                Job Type: %s
                Job Location: %s
                Experience: %d years
                Salary: %s
                Responsibilities: %s
                Education: %s
                Technologies & Tools: %s
                Number of Openings: %d
                Posted By: %s
                """,
                jd.getRole(), jd.getJob_type(), jd.getJob_location(),
                jd.getExperience(), jd.getSalary(),
                jd.getResponsibilities(), jd.getEducational_requirements(),
                jd.getTechnologies_tools(), jd.getNo_of_openings(), jd.getPosted_by()
        );

    }

    private Response callGemini(String prompt,String Email) throws Exception {
        log.info("Sending prompt to Gemini...");
        log.info("Prompt:\n{}", prompt);

        String uri = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(payload);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode root = mapper.readTree(response.body());

        if (root.has("error")) {
            String errorMessage = root.path("error").path("message").asText("Unknown error");
            int errorCode = root.path("error").path("code").asInt(-1);
            throw new RuntimeException("Gemini API Error [" + errorCode + "]: " + errorMessage);
        }

        JsonNode candidatesNode = root.path("candidates");
        if (!candidatesNode.isArray() || candidatesNode.size() == 0) {
            throw new RuntimeException("Gemini response has no candidates");
        }

        JsonNode contentNode = candidatesNode.get(0).path("content").path("parts");
        if (!contentNode.isArray() || contentNode.size() == 0) {
            throw new RuntimeException("Gemini response content is missing");
        }

        String textResponse = contentNode.get(0).path("text").asText().trim();
        log.info("Gemini raw response:\n{}", textResponse);


        if (textResponse.startsWith("```") && textResponse.endsWith("```")) {
            textResponse = textResponse.substring(textResponse.indexOf("\n") + 1, textResponse.lastIndexOf("```")).trim();
        }
        log.info("Gemini extracted text:\n{}", textResponse);

        int score = 0;
        String feedback = "Could not parse Gemini response.";

        try {
            JsonNode result = mapper.readTree(textResponse);
            score = result.path("score").asInt();
            feedback = result.path("feedback").asText();
        } catch (JsonProcessingException e) {
            log.error("Failed to parse Gemini JSON response: {}", e.getMessage());
            // Instead of saving raw JSON string, save a generic message or empty string
            score = 0;
            feedback = "Failed to parse feedback from AI response.";
        }


        CVFeedback cvFeedback = new CVFeedback();
        cvFeedback.setScore(score);
        cvFeedback.setFeedback(feedback);
        cvFeedbackRepository.save(cvFeedback);
        String email = Email; // Replace this dynamically if available

        if (score < 75) {
            emailService.sendEmail(
                    email,
                    "Application Status - Not Selected",
                    "Dear Candidate,\n\nThank you for applying. Based on your profile, unfortunately you are not selected at this time.\n\nFeedback:\n" + feedback
            );
        } else {
            emailService.sendEmail(
                    email,
                    "Application Status - Selected",
                    "Congratulations!\n\nYou have been selected based on your resume and skills.\n\nFeedback:\n" + feedback
            );
        }
        return new Response(score, feedback);
    }




    private String extractTextFromFile(MultipartFile file) throws Exception {
        log.info("MATCH 3");
        String contentType = file.getContentType();
        InputStream is = file.getInputStream();

        if (contentType.equals("application/pdf")) {
            try (PDDocument document = PDDocument.load(is)) {
                return new PDFTextStripper().getText(document);
            }
        } else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            try (XWPFDocument doc = new XWPFDocument(is)) {
                return new XWPFWordExtractor(doc).getText();
            }
        } else if (contentType.equals("application/msword")) {
            try (HWPFDocument doc = new HWPFDocument(is)) {
                return new WordExtractor(doc).getText();
            }
        } else {
            throw new IllegalArgumentException("Unsupported file type for reading content.");
        }
    }
}
