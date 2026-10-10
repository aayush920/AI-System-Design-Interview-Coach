package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.dto.*;
import com.aicoach.aisystemdesigncoachbackend.entity.Evaluation;
import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import com.aicoach.aisystemdesigncoachbackend.repository.EvaluationRepository;
import com.aicoach.aisystemdesigncoachbackend.repository.InterviewSessionRepository;
import com.aicoach.aisystemdesigncoachbackend.repository.QuestionAnswerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InterviewService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final EvaluationRepository evaluationRepository;

    private final GeminiService geminiService;

    public InterviewService(InterviewSessionRepository interviewSessionRepository, QuestionAnswerRepository questionAnswerRepository, EvaluationRepository evaluationRepository, GeminiService geminiService){
        this.interviewSessionRepository = interviewSessionRepository;
        this.questionAnswerRepository=questionAnswerRepository;
        this.evaluationRepository=evaluationRepository;
        this.geminiService = geminiService;
    }

    public InterviewSummaryResponse createInterview(String topic){
        InterviewSession session= new InterviewSession();
        session.setTopic(topic);
        session.setCreatedAt(LocalDateTime.now());

        //mapping the dto to the entity
        InterviewSession savedSession= interviewSessionRepository.save(session);
        InterviewSummaryResponse response=new InterviewSummaryResponse();
        response.setId(savedSession.getId());
        response.setTopic(savedSession.getTopic());

        return response;
    }

    public QuestionAnswerResponse addQuestionAnswer(Long interviewId, String question, String answer){
        InterviewSession interviewSession=interviewSessionRepository.findById(interviewId).orElseThrow();

        QuestionAnswer questionAnswer= new QuestionAnswer();
        questionAnswer.setAnswer(answer);
        questionAnswer.setQuestion(question);
        questionAnswer.setInterviewSession(interviewSession);

        questionAnswerRepository.save(questionAnswer);

        return new QuestionAnswerResponse(questionAnswer.getId(),questionAnswer.getQuestion(),questionAnswer.getAnswer());
    }

    public QuestionAnswerResponse updateQuestionAnswer(Long questionAnswerId, String answer){
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(questionAnswerId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Question and answer not found with id- " + questionAnswerId
                ));

        questionAnswer.setAnswer(answer);
        QuestionAnswer savedQuestionAnswer = questionAnswerRepository.save(questionAnswer);

        return new QuestionAnswerResponse(
                savedQuestionAnswer.getId(),
                savedQuestionAnswer.getQuestion(),
                savedQuestionAnswer.getAnswer()
        );
    }

    public EvaluationResult evaluateQuestionAnswer(Long questionAnswerId){
        QuestionAnswer questionAnswer=questionAnswerRepository.findById(questionAnswerId).orElseThrow();

        //check for existing evaluation
        Optional<Evaluation> existingEvaluation = evaluationRepository.findByQuestionAnswerId(questionAnswerId);
        if(existingEvaluation.isPresent()){
            System.out.println("Evaluation Already exists");
            Evaluation evaluation =existingEvaluation.get();

            return new EvaluationResult(evaluation.getScore(),evaluation.getStrengths(),evaluation.getMissingPoints(),evaluation.getNextSteps());
        }

        //fetch evaluation from Gemini
        EvaluationResult result= geminiService.evaluateQuestionAnswer(questionAnswer.getQuestion(), questionAnswer.getAnswer());

        Evaluation evaluation= new Evaluation();
        evaluation.setScore(result.getScore());
        evaluation.setQuestionAnswer(questionAnswer);
        evaluation.setStrengths(result.getStrengths());
        evaluation.setMissingPoints(result.getMissingPoints());
        evaluation.setNextSteps(result.getNextSteps());

        questionAnswer.setEvaluation(evaluation);

        evaluationRepository.save(evaluation);

        //map entity to the dto
        return result;
    }

    public List<InterviewSummaryResponse> getAllInterviews(){

        return interviewSessionRepository
                .findAll()
                .stream()
                .map(interviewSession ->
                        new InterviewSummaryResponse(interviewSession.getId(), interviewSession.getTopic()))
                .toList();
    }

    public InterviewDetailResponse getInterviewDetails(Long interviewId){
        return interviewSessionRepository
                .findById(interviewId)
                .map(interviewSession ->
                        new InterviewDetailResponse(
                                interviewSession.getId(),
                                interviewSession.getTopic(),
                                interviewSession.getQuestionsAnswers()
                                        .stream()
                                        .map(questionAnswer ->
                                                new QuestionAnswerResponse(
                                                        questionAnswer.getId(),
                                                        questionAnswer.getQuestion(),
                                                        questionAnswer.getAnswer()
                                                ))
                                        .toList()
                        )
                ).orElseThrow(()-> new NoSuchElementException("Interview not found with id- "+ interviewId));
    }

    public InterviewHistoryResponse getInterviewHistory(Long interviewId){

        InterviewSession interviewSession= interviewSessionRepository
                .findById(interviewId)
                .orElseThrow(()-> new NoSuchElementException("Interview not found with id- "+ interviewId));

        List<EvaluationHistoryItem> evaluationHistoryItemList=interviewSession
                .getQuestionsAnswers()
                .stream()
                .filter(qa -> qa.getEvaluation() != null)
                .map(qa ->
                        new EvaluationHistoryItem(
                                qa.getQuestion(),
                                qa.getEvaluation().getScore(),
                                qa.getEvaluation().getStrengths(),
                                qa.getEvaluation().getMissingPoints(),
                                qa.getEvaluation().getNextSteps()
                        ))
                .toList();

        System.out.println(evaluationHistoryItemList);
        return new InterviewHistoryResponse(interviewId, evaluationHistoryItemList);

    }

    public QuestionAnswerResponse getInterviewQuestion(Long interviewId) {

        //Get the interviewSession from repository
        InterviewSession interviewSession= interviewSessionRepository
                .findById(interviewId)
                .orElseThrow(()-> new NoSuchElementException("Interview not found with id- "+ interviewId));

        String generatedQuestion = geminiService.generateQuestion(interviewSession.getTopic());

        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setQuestion(generatedQuestion);
        questionAnswer.setInterviewSession(interviewSession);
        QuestionAnswer savedQuestionAnswer = questionAnswerRepository.save(questionAnswer);

        return new QuestionAnswerResponse(
                savedQuestionAnswer.getId(),
                savedQuestionAnswer.getQuestion(),
                savedQuestionAnswer.getAnswer()
        );
    }

}
