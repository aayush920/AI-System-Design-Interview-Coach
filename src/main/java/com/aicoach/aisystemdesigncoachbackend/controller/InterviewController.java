package com.aicoach.aisystemdesigncoachbackend.controller;

import com.aicoach.aisystemdesigncoachbackend.dto.CreateInterviewRequest;
import com.aicoach.aisystemdesigncoachbackend.dto.CreateQuestionAnswerRequest;
import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import com.aicoach.aisystemdesigncoachbackend.service.InterviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService service;

    public InterviewController(InterviewService interviewService){
        this.service=interviewService;
    }

    @PostMapping("/createInterview")
    public InterviewSession createInterview(@RequestBody CreateInterviewRequest request){
        return service.createInterview(request.getTopic());
    }

    @PostMapping("/{id}/questions")
    public QuestionAnswer addQuestionAnswer(@PathVariable Long id, @RequestBody CreateQuestionAnswerRequest request){
        return service.addQuestionAnswer(
                id,
                request.getQuestion(),
                request.getAnswer()
        );
    }


}
