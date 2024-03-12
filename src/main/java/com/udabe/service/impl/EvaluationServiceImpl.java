package com.udabe.service.impl;

import com.udabe.cmmn.base.Response;
import com.udabe.entity.Evaluation;
import com.udabe.repository.CriteriaDetailRepository;
import com.udabe.repository.EvaluationRepository;
import com.udabe.service.EvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private static EvaluationRepository evaluationRepository;

    private final CriteriaDetailRepository criteriaDetailRepository;

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository, CriteriaDetailRepository criteriaDetailRepository) {
        this.evaluationRepository = evaluationRepository;
        this.criteriaDetailRepository = criteriaDetailRepository;
    }


    @Override
    public ResponseEntity<?> createEvaluation(Evaluation evaluation) {
        evaluation.setCriteriaDetail(criteriaDetailRepository.findById(evaluation.getCriteriaDetailId()).get());
        Evaluation evaluationSave = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(new Response().setData(evaluationSave).setMessage("Saved!"));
    }


    @Override
    public ResponseEntity<?> updateEvaluation(Evaluation evaluation, Long evalutionId) {
        Optional<Evaluation> evalutionFind = evaluationRepository.findById(evalutionId);
        if (evalutionFind.isPresent()) {
            evaluation.setCriteriaDetail(evalutionFind.get().getCriteriaDetail());
            evaluationRepository.save(evaluation);
            return ResponseEntity.ok("Successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot save Evalution!");
        }
    }

}
