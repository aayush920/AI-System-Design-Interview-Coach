package com.aicoach.aisystemdesigncoachbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "INTEGER", nullable = false)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String missingPoints;

    @Column(columnDefinition = "TEXT")
    private String nextSteps;

    @OneToOne
    @JoinColumn(name = "question_answer_id")
    private QuestionAnswer questionAnswer;
}
