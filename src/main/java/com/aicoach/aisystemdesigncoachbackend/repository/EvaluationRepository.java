package com.aicoach.aisystemdesigncoachbackend.repository;

import com.aicoach.aisystemdesigncoachbackend.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
}
