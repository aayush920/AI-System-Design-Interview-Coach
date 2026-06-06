package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.config.GeminiConfig;
import com.aicoach.aisystemdesigncoachbackend.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiService(GeminiConfig geminiConfig, ObjectMapper objectMapper){
        this.geminiConfig=geminiConfig;
        this.restClient= RestClient.create();
        this.objectMapper=objectMapper;
    }

    /**
     * This fn takes a question and answer provided by the user, builds a gemini prompt on top of these and returns a structured GeminiResponse to the user
     *
     * @param question - question selected by the user
     * @param answer - answer provided by the user
     * @return - extracted Gemini response for the prompt given
     */
    public EvaluationResult evaluate(String question, String answer){

        try{
            String prompt= buildPrompt(question,answer);


            GeminiRequest request=
                    new GeminiRequest(
                            List.of(
                                    new GeminiRequestContent(
                                            List.of(
                                                    new GeminiPart(prompt)
                                            )
                                    )
                            )
                    );


            String geminiUrl=
                    geminiConfig.getApiUrl()
                            +"?key="
                            +geminiConfig.getApiKey();

            GeminiResponse response =
                    restClient.post()
                            .uri(geminiUrl)
                            .body(request)
                            .retrieve()
                            .body(GeminiResponse.class);

            validateResponse(response);

            String json=
                    response.getCandidates()
                            .getFirst()
                            .getContent()
                            .getParts()
                            .getFirst()
                            .getText();

            return objectMapper.readValue(json, EvaluationResult.class);

        } catch(Exception e){
            throw new RuntimeException(
                    "Failed to evaluate answer using Gemini",
                    e
            );
        }

    }

    private void validateResponse(GeminiResponse response) {

        if (response == null) {
            throw new RuntimeException("Gemini response is null");
        }

        if (response.getCandidates() == null
                || response.getCandidates().isEmpty()) {
            throw new RuntimeException("No candidates returned by Gemini");
        }

        if (response.getCandidates().getFirst().getContent() == null) {
            throw new RuntimeException("Candidate content is missing");
        }

        if (response.getCandidates().getFirst().getContent().getParts() == null
                || response.getCandidates().getFirst().getContent().getParts().isEmpty()) {
            throw new RuntimeException("Candidate parts are missing");
        }
    }

    private String buildPrompt(String question, String answer) {

        return """
            You are a senior system design interviewer.

            Question:
            %s

            Candidate Answer:
            %s

            Evaluate the answer and return only valid JSON.
            No Markdown.
            No explanation.
            Only JSON

            Return:
            {
                "score": 0,
                "strengths": [],
                "missingPoints": [],
                "nextSteps":[]
            }

            Keep the response under 200 words.
            """.formatted(question, answer);
    }
}
