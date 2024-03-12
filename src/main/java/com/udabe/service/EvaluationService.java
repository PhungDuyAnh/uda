package com.udabe.service;

import com.udabe.entity.Evaluation;
import org.springframework.http.ResponseEntity;

public interface EvaluationService {

    ResponseEntity<?> createEvaluation(Evaluation evaluation);

    ResponseEntity<?> updateEvaluation(Evaluation evaluation, Long evalutionId);

}
