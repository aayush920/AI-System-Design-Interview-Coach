package com.aicoach.aisystemdesigncoachbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="question_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, columnDefinition="TEXT")
    private String question;

    @Column(columnDefinition="TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name="interview_session_id")
    private InterviewSession interviewSession;

    @OneToOne(mappedBy = "questionAnswer")
    private Evaluation evaluation;

}
