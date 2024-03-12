package com.udabe.service;

import com.udabe.entity.Answer;
import com.udabe.entity.EvaluationVersion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AnswerService {

    ResponseEntity<?> addAnswer(List<Answer> answer, EvaluationVersion evaluationVersion);

    ResponseEntity<?> updateAnswer(List<Answer> answer);

}
