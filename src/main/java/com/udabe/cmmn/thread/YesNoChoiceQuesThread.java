package com.udabe.cmmn.thread;

import com.udabe.cmmn.base.BaseThread;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;

import java.util.ArrayList;
import java.util.List;

import static com.udabe.service.impl.AnswerServiceImpl.answerRepository;

public class YesNoChoiceQuesThread extends BaseThread {

    public YesNoChoiceQuesThread(ResponseDTO responseDTOS) {
        super(responseDTOS);
    }

    @Override
    protected ResultDTO processCommand(ResponseDTO responseDTOS) {
        System.out.println("process YesNoChoiceQuesThread");
        double score = 0.0;
        float countPass = 0.0F;
        List<Long> answerId = new ArrayList<>();

        boolean isPass = "Có".equalsIgnoreCase(responseDTOS.getSelectedOption());
        if (isPass) {
            score = responseDTOS.getPoint();
//            answerRepository.updateAnswer(responseDTOS.getAnswerId());
            answerId.add(responseDTOS.getAnswerId());
            countPass++;
        }
        System.out.println(Thread.currentThread().getName());
        return new ResultDTO(score, countPass, answerId);
    }

}