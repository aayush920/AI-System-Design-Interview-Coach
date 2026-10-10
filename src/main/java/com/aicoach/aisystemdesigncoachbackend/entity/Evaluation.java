package com.aicoach.aisystemdesigncoachbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ElementCollection
    @CollectionTable(
            name="evaluation_strengths",
            joinColumns=@JoinColumn(name="evaluation_id")
    )
    private List<String> strengths;

    @ElementCollection
    @CollectionTable(
            name="evaluation_missingPoints",
            joinColumns=@JoinColumn(name="evaluation_id")
    )
    private List<String> missingPoints;

    @ElementCollection
    @CollectionTable(
            name="evaluation_nextSteps",
            joinColumns=@JoinColumn(name="evaluation_id")
    )
    private List<String> nextSteps;

    @OneToOne
    @JoinColumn(name = "question_answer_id")
    private QuestionAnswer questionAnswer;
}
