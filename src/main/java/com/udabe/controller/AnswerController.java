package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.Answer;
import com.udabe.entity.EvaluationVersion;
import com.udabe.service.impl.AnswerServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/answer")
public class AnswerController extends BaseCrudController<Answer, Long> {

    private final AnswerServiceImpl answerServiceImpl;

    public AnswerController(AnswerServiceImpl answerServiceImpl) {

        this.answerServiceImpl = answerServiceImpl;
    }


    /**
     * Controller lưu câu trả lời
     */
    @PostMapping("/addAnswer")
//    @PreAuthorize("@usersServiceImpl.getRoles().contains('ROLE_URBAN')")
    public ResponseEntity<?> addAnswer(@RequestBody List<Answer> answer, EvaluationVersion evaluationVersion){

        return answerServiceImpl.addAnswer(answer, evaluationVersion);
    }


    /**
     * Controller sửa câu trả lời
     */
    @PutMapping("/updateAnswer")
    public ResponseEntity<?> updateAnswer(@RequestBody List<Answer> answer){

        return answerServiceImpl.updateAnswer(answer);
    }

}
