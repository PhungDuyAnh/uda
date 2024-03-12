package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.EvaluationVersion;
import com.udabe.service.impl.EvaluationVersionServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/evaluationVersion")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EvaluationVersionController extends BaseCrudController<EvaluationVersion, Long> {

    private final EvaluationVersionServiceImpl evaluationVersionServiceImpl;

    public EvaluationVersionController(EvaluationVersionServiceImpl evaluationVersionServiceImpl) {
        this.evaluationVersionServiceImpl = evaluationVersionServiceImpl;
    }

    /**
     * Controller gửi bản đánh giá
     */
    @PutMapping("/sendEvaluationVersion")
    public ResponseEntity<?> sendEvaluationVersion(@RequestBody EvaluationVersion evaluationVersion){

        return evaluationVersionServiceImpl.sendEvaluationVersion(evaluationVersion);
    }


    /**
     * Controller kiểm tra đủ số câu trả lời (theo id user và theo bộ chỉ số hiện hành)
     */
    @GetMapping("/checkAnswer")
    public ResponseEntity<?> checkAnswer(){

        return evaluationVersionServiceImpl.checkAnswer();
    }


    @GetMapping("/getLastestVersion")
    public ResponseEntity<?> getLastestVersion(){

        return evaluationVersionServiceImpl.getLastestVersion();
    }

    @GetMapping("/getCountPassAnswer")
    public ResponseEntity<?> getCountPassAnswer(@RequestParam(required = true) Long evaluationVersionId){

        return evaluationVersionServiceImpl.getCountPassAnswer(evaluationVersionId);
    }

}
