package com.udabe.service.impl;

import com.udabe.cmmn.util.AutoEvaluationUtil;
import com.udabe.service.AutoEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AutoEvaluationServiceImpl implements AutoEvaluationService {

    private final AutoEvaluationUtil autoEvaluationUtil;

    public AutoEvaluationServiceImpl(AutoEvaluationUtil autoEvaluationUtil) {
        this.autoEvaluationUtil = autoEvaluationUtil;
    }

    @Override
    public ResponseEntity<?> automaticEvaluation(Long evaluationVersionId) throws ExecutionException, InterruptedException {
        AutoEvaluationUtil.autoEvaluationHandler(evaluationVersionId);
        return ResponseEntity.ok("Automatic evaluation successfully!");
    }
}
