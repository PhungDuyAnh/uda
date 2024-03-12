package com.udabe.controller;

import com.udabe.cmmn.util.AutoEvaluationUtil;
import com.udabe.service.impl.AutoEvaluationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("${apiPrefix}/auto")
public class AutoEvaluationController {

    private final AutoEvaluationServiceImpl autoEvaluationService;

    @Autowired
    public AutoEvaluationController(AutoEvaluationServiceImpl autoEvaluationService) {
        this.autoEvaluationService = autoEvaluationService;
    }


    @PostMapping("/get-score")
    public ResponseEntity<?> automaticEvaluation(@RequestParam Long evaluationVersionId) throws ExecutionException, InterruptedException {
        return autoEvaluationService.automaticEvaluation(evaluationVersionId);
    }
}
