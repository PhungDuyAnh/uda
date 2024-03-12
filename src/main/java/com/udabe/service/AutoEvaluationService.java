package com.udabe.service;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface AutoEvaluationService {


    ResponseEntity<?> automaticEvaluation(Long evaluationVersionId) throws ExecutionException, InterruptedException;
}
