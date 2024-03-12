package com.udabe.cmmn.thread;

import com.udabe.cmmn.base.BaseThread;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;

import java.util.ArrayList;
import java.util.List;

import static com.udabe.service.impl.AnswerServiceImpl.answerRepository;

public class PercentQuesThread extends BaseThread {


    public PercentQuesThread(ResponseDTO responseDTOS) {
        super(responseDTOS);
    }

    @Override
    protected ResultDTO processCommand(ResponseDTO responseDTOS) {
        System.out.println("process PercentQuesThread");

        double score = 0.0;
        float countPass = 0.0F;
        List<Long> answerId = new ArrayList<>();
        try {
            double value = Double.parseDouble(responseDTOS.getSelectedOption());
            double answer = Double.parseDouble(responseDTOS.getConditions());
            boolean isPass = compare(value, answer, responseDTOS.getSettingConditions());
            if (isPass) {
                score = responseDTOS.getPoint();
//                answerRepository.updateAnswer(responseDTOS.getAnswerId());
                answerId.add(responseDTOS.getAnswerId());
                countPass++;
            }
            System.out.println(Thread.currentThread().getName());
        } catch (Exception e) {
            score = 0.0;
        }
        return new ResultDTO(score, countPass, answerId);
    }
}


