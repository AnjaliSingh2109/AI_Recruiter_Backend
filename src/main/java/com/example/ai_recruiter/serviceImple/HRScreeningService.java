package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.entity.HRScreening;
import com.example.ai_recruiter.repository.HRScreeningRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HRScreeningService {

    private final HRScreeningRepository screeningRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public List<String> generateQuestionsFromCV(MultipartFile file) {
        try {
            String text = extractTextFromPDF(file);
            List<String> questions = callGeminiForQuestions(text);

            HRScreening screening = new HRScreening();
            screening.setFileName(file.getOriginalFilename());
            screening.setQuestions(questions);

            screeningRepository.save(screening);
            return questions;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate questions from CV.");
        }
    }

    private String extractTextFromPDF(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private List<String> callGeminiForQuestions(String cvText) throws Exception {
        String payload = """
    {
      "contents": [{
        "parts": [{
          "text": "Generate HR screening questions in brief for the candidate based on the following CV for background verification and ask simple questions about background like name,school,college,year of passing,skills,technologies and tools and only generate questions no headings and sub-headings:\\n%s"
        }]
      }],
      "generationConfig": {
        "temperature": 0.7
      }
    }
    """.formatted(cvText);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(geminiApiUrl + "?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode json = mapper.readTree(response.body());

        // Logging for debugging (optional)
        System.out.println("Gemini Raw Response:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

        JsonNode candidatesNode = json.path("candidates");
        if (!candidatesNode.isArray() || candidatesNode.isEmpty()) {
            throw new RuntimeException("Invalid Gemini response: missing 'candidates'");
        }

        JsonNode firstCandidate = candidatesNode.get(0);
        if (firstCandidate == null) {
            throw new RuntimeException("No candidate found in Gemini response");
        }

        JsonNode parts = firstCandidate.path("content").path("parts");
        if (!parts.isArray() || parts.isEmpty()) {
            throw new RuntimeException("Invalid Gemini response: missing 'parts'");
        }

        String questionsText = parts.get(0).path("text").asText();
        return Arrays.stream(questionsText.split("\n"))
                .filter(q -> !q.isBlank())
                .map(String::trim)
                .toList();
    }
}
