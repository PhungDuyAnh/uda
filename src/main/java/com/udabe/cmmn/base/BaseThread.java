package com.udabe.cmmn.base;

import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;

import java.util.concurrent.Callable;

public class BaseThread implements  Callable<ResultDTO> {

//    protected long score;

    protected ResponseDTO responseDTOS;

    public BaseThread(ResponseDTO responseDTOS) {
        this.responseDTOS = responseDTOS;
    }

    protected ResultDTO processCommand(ResponseDTO responseDTOS) {

        ResultDTO resultDTO =  new ResultDTO();
        return resultDTO;
    }

    @Override
    public ResultDTO call() throws Exception {
        return processCommand(this.responseDTOS);
    }

    public static boolean compare(double a, double b, long c) {
        return switch ((int) c) {
            case 1 -> a < b;
            case 2 -> a > b;
            case 3 -> a == b;
            case 4 -> a <= b;
            case 5 -> a >= b;
            default -> throw new IllegalArgumentException("giá trị của c sai");
        };
    }
}
