package com.udabe.cmmn.thread;

import com.udabe.cmmn.base.BaseThread;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;
import org.apache.commons.lang.math.Fraction;

import java.util.ArrayList;
import java.util.List;

import static com.udabe.service.impl.AnswerServiceImpl.answerRepository;

public class FractionalQuesThread extends BaseThread {


    public FractionalQuesThread(ResponseDTO responseDTOS) {
        super(responseDTOS);
    }

    @Override
    protected ResultDTO processCommand(ResponseDTO responseDTOS) {
        System.out.println("process FractionalQuesThread");
        double score = 0.0;
        float countPass = 0.0F;
        List<Long> answerId = new ArrayList<>();
        try {
            double resultValue = convertFractionToDouble(responseDTOS.getSelectedOption());
            double resultAnswer = convertFractionToDouble(responseDTOS.getConditions());
            boolean isPass = compare(resultValue, resultAnswer, responseDTOS.getSettingConditions());
            if (isPass) {
                score = responseDTOS.getPoint();
//               answerRepository.updateAnswer(responseDTOS.getAnswerId());
                answerId.add(responseDTOS.getAnswerId());
                countPass++;
            }
            System.out.println(Thread.currentThread().getName());
        } catch (Exception e) {
            score = 0.0;
        }
        System.out.println(Thread.currentThread().getName());
        return new ResultDTO(score, countPass, answerId);
    }

    public static double convertFractionToDouble(String fraction) {
        // Tách chuỗi thành a và b
        String[] parts = fraction.split("/");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Chuỗi không đúng định dạng 'a/b'");
        }
        try {
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[1]);

            if (b == 0) {
                throw new ArithmeticException("Không thể chia cho 0");
            }
            return a / b;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Không thể chuyển đổi thành số double");
        }
    }
}
