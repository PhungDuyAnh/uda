package com.udabe.service;

import com.udabe.entity.EvaluationVersion;
import org.springframework.http.ResponseEntity;

public interface EvaluationVersionService {

    ResponseEntity<?> sendEvaluationVersion(EvaluationVersion evaluationVersion);

    ResponseEntity<?> checkAnswer();

    ResponseEntity<?> getLastestVersion();

    ResponseEntity<?> getCountPassAnswer(Long evaluationVersionId);
}
