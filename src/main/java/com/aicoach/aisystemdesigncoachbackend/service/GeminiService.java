package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.config.GeminiConfig;
import com.aicoach.aisystemdesigncoachbackend.dto.GeminiPart;
import com.aicoach.aisystemdesigncoachbackend.dto.GeminiRequest;
import com.aicoach.aisystemdesigncoachbackend.dto.GeminiRequestContent;
import com.aicoach.aisystemdesigncoachbackend.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final RestClient restClient;

    @Autowired
    public GeminiService(GeminiConfig geminiConfig){
        this.geminiConfig=geminiConfig;
        this.restClient= RestClient.create();
    }

    /**
     * This fn takes a question and answer provided by the user, builds a gemini prompt on top of these and returns a structured GeminiResponse to the user
     *
     * @param question - question selected by the user
     * @param answer - answer provided by the user
     * @return - extracted Gemini response for the prompt given
     */
    public String evaluate(String question, String answer){

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

            return response.getCandidates()
                    .getFirst()
                    .getContent()
                    .getParts()
                    .getFirst()
                    .getText();

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

            Evaluate the answer.

            Return:
            1. Strengths
            2. Missing Points
            3. Score out of 10

            Keep the response under 200 words.
            """.formatted(question, answer);
    }
}
