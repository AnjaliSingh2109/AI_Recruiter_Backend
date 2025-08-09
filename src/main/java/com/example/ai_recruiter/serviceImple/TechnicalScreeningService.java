package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.entity.JobDesc;
import com.example.ai_recruiter.repository.JobRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicalScreeningService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobRepository jobRepository;

    public List<String> generateQuestionsFromCVAndJD(MultipartFile file, String jobId) throws Exception {
        String cvText = extractTextFromPdf(file);

        JobDesc job = jobRepository.findByJobId(jobId)

                .orElseThrow(() -> new RuntimeException("Job not found"));

        String prompt = String.format("""
            You are a technical interviewer. Based on the following Job Description and Candidate CV, generate 30 technical interview questions. 
            Do NOT include HR/behavioral questions.The questions should be according to the year of experience and at least 15 medium to hard questions and some questions should be based on projects and work experience.It should have just technical questions,no headings and no sub headings and only 30 questions.

            Job Description:
            %s

            Candidate CV:
            %s
            """, job, cvText);

        return callGeminiForQuestions(prompt);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            return new PDFTextStripper().getText(document);
        }
    }

    private List<String> callGeminiForQuestions(String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        ObjectNode contentNode = objectMapper.createObjectNode();
        contentNode.put("role", "user");
        contentNode.putObject("parts").put("text", prompt);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.putArray("contents").add(contentNode);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());

        if (json.has("candidates")) {
            String fullText = json.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            return Arrays.stream(fullText.split("\\n"))
                    .map(String::trim)
                    .filter(q -> !q.isEmpty())
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Gemini API did not return questions. Response: " + response.body());
        }
    }
}
