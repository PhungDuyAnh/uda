package com.udabe.cmmn.thread;

import com.udabe.cmmn.base.BaseThread;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.udabe.service.impl.AnswerServiceImpl.answerRepository;

public class SingleChoiceQuesThread extends BaseThread {


    public SingleChoiceQuesThread(ResponseDTO responseDTOS) {
        super(responseDTOS);
    }

    @Override
    protected ResultDTO processCommand(ResponseDTO responseDTOS) {
        System.out.println("process SingleChoiceQuesThread");
        double score = responseDTOS.getPoint() * responseDTOS.getWeight() / 100;
        boolean isPass = false;
        float countPass = 0.0F;
        List<Long> answerId = new ArrayList<>();
        if (responseDTOS.getWeight() == 100) {
//            answerRepository.updateAnswer(responseDTOS.getAnswerId());
            answerId.add(responseDTOS.getAnswerId());
            isPass = true;
            countPass++;
        }
        System.out.println(Thread.currentThread().getName());
        return new ResultDTO(score, countPass, answerId);
    }
}
