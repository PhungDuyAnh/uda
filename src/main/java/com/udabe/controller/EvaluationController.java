package com.udabe.controller;

import com.udabe.entity.Evaluation;
import com.udabe.service.EvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/evalution")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> createEvaluation(@RequestBody Evaluation evaluation) {
        return evaluationService.createEvaluation(evaluation);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCriteriaClass3(@PathVariable(value = "id") Long id, @RequestBody Evaluation evaluation) {
        return evaluationService.updateEvaluation(evaluation, id);
    }

}
