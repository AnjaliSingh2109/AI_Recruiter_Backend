package com.example.ai_recruiter.convertoDTO.CVFeedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int score;
    private String feedback;
}
