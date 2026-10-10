package com.aicoach.aisystemdesigncoachbackend.controller;

import com.aicoach.aisystemdesigncoachbackend.dto.*;
import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import com.aicoach.aisystemdesigncoachbackend.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService){
        this.interviewService=interviewService;
    }

    @PostMapping("/createInterview")
    public InterviewSummaryResponse createInterview(@RequestBody CreateInterviewRequest request){
        return interviewService.createInterview(request.getTopic());
    }

    @PostMapping("/{id}/questions")
    public QuestionAnswerResponse addQuestionAnswer(@PathVariable Long id, @RequestBody CreateQuestionAnswerRequest request){
        return interviewService.addQuestionAnswer(
                id,
                request.getQuestion(),
                request.getAnswer()
        );
    }
    
    @GetMapping("/")
    public List<InterviewSummaryResponse> getAllInterviews(){
        return interviewService.getAllInterviews();
    }
    
    @GetMapping("/{id}")
    public InterviewDetailResponse getInterviewDetails(@PathVariable Long id){
        return interviewService.getInterviewDetails(id);
    }

    @GetMapping("/{id}/history")
    public InterviewHistoryResponse getInterviewHistory(@PathVariable Long id){
        return interviewService.getInterviewHistory(id);
    }

    @PostMapping("/{id}/generateQuestion")
    public QuestionAnswerResponse getInterviewQuestion(@PathVariable Long id){
        return interviewService.getInterviewQuestion(id);
    }

    @PatchMapping("/questions/{questionAnswerId}/answer")
    public QuestionAnswerResponse updateQuestionAnswer(
            @PathVariable Long questionAnswerId,
            @RequestBody UpdateAnswerRequest request
    ){
        return interviewService.updateQuestionAnswer(questionAnswerId, request.getAnswer());
    }


}
