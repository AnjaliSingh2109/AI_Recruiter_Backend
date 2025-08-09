package com.example.ai_recruiter.serviceImple;

import com.example.ai_recruiter.convertoDTO.JobDesc.Request;
import com.example.ai_recruiter.entity.JobDesc;
import com.example.ai_recruiter.repository.JobRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeminiService {

    @Autowired
    private JobRepository jobRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String generateJD(String prompt, Request req) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-goog-api-key", apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());
            String generatedText = json.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();

            // ✅ Save generated JD to DB
            log.info("Jd "+generatedText);
            JobDesc jobDesc = jobRepository.findByJobId(req.getJob_Id())
                    .orElseThrow(() -> new RuntimeException("Job not found with ID: " + req.getJob_Id()));


            String encodedDescription = Base64.getEncoder()
                    .encodeToString(generatedText.getBytes(StandardCharsets.UTF_8));

//            jobDesc.setGeneratedDescription(encodedDescription);
            jobRepository.save(jobDesc);

            return generatedText;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
