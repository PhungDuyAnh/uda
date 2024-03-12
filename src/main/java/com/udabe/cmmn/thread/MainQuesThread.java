package com.udabe.cmmn.thread;

import com.udabe.cmmn.base.BaseEntity;
import com.udabe.cmmn.base.BaseThread;
import com.udabe.cmmn.config.PoolThreadConfig;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MainQuesThread implements Callable<ResultDTO> {

    protected List<ResponseDTO> responseDTOList;

    protected int typeQues;

    public MainQuesThread(List<ResponseDTO> responseDTOList, int typeQues) {
        this.responseDTOList = responseDTOList;
        this.typeQues = typeQues;
    }

    @Override
    public ResultDTO call() throws Exception {
        System.out.println("main " + Thread.currentThread().getName());
        return processCommand(this.responseDTOList, this.typeQues);
    }

    protected ResultDTO processCommand(List<ResponseDTO> responseDTOList, int typeQues) throws ExecutionException, InterruptedException {
        ResultDTO resultDTO = new ResultDTO();
        int i = 0;
        double sumScore = 0.0;
        float countPass = 0.0F;
        List<Long> sumAnswerId = new ArrayList<>();
        ThreadPoolExecutor threadPoolExecutor = null;
        switch (typeQues) {
            case 1:
                threadPoolExecutor = PoolThreadConfig.getThreadPoolExecutor();
                while (i < responseDTOList.size()) {
                    YesNoChoiceQuesThread yesNoChoiceQuesThread = new YesNoChoiceQuesThread(responseDTOList.get(i));
                    Future<ResultDTO> future = threadPoolExecutor.submit(yesNoChoiceQuesThread);
                    ResultDTO result = future.get();
                    sumScore += result.getScore();
                    countPass += result.getCountPass();
                    sumAnswerId.addAll(result.getAnswerId());
                    i++;
                }
                threadPoolExecutor.shutdown();
                while (!threadPoolExecutor.isTerminated()) {
                    // Chờ xử lý hết các request còn chờ trong Queue ...
                }
                break;
            case 2:
                threadPoolExecutor = PoolThreadConfig.getThreadPoolExecutor();
                while (i < responseDTOList.size()) {
                    SingleChoiceQuesThread singleChoiceQuesThread = new SingleChoiceQuesThread(responseDTOList.get(i));
                    threadPoolExecutor.submit(singleChoiceQuesThread);
                    Future<ResultDTO> future = threadPoolExecutor.submit(singleChoiceQuesThread);
                    ResultDTO result = future.get();
                    sumScore += result.getScore();
                    countPass += result.getCountPass();
                    sumAnswerId.addAll(result.getAnswerId());
                    i++;
                }
                threadPoolExecutor.shutdown();

                while (!threadPoolExecutor.isTerminated()) {
                    // Chờ xử lý hết các request còn chờ trong Queue ...
                }
                break;
            case 3:
                threadPoolExecutor = PoolThreadConfig.getThreadPoolExecutor();
                while (i < responseDTOList.size()) {
                    PercentQuesThread percentQuesThread = new PercentQuesThread(responseDTOList.get(i));
                    Future<ResultDTO> future = threadPoolExecutor.submit(percentQuesThread);
                    ResultDTO result = future.get();
                    sumScore += result.getScore();
                    countPass += result.getCountPass();
                    sumAnswerId.addAll(result.getAnswerId());
                    i++;
                }
                threadPoolExecutor.shutdown();
                while (!threadPoolExecutor.isTerminated()) {
                    // Chờ xử lý hết các request còn chờ trong Queue ...
                }
                break;
            case 4:
                threadPoolExecutor = PoolThreadConfig.getThreadPoolExecutor();
                while (i < responseDTOList.size()) {
                    NumberQuesThread numberQuesThread = new NumberQuesThread(responseDTOList.get(i));
                    Future<ResultDTO> future = threadPoolExecutor.submit(numberQuesThread);
                    ResultDTO result = future.get();
                    sumScore += result.getScore();
                    countPass += result.getCountPass();
                    sumAnswerId.addAll(result.getAnswerId());
                    i++;
                }
                threadPoolExecutor.shutdown();
                while (!threadPoolExecutor.isTerminated()) {
                    // Chờ xử lý hết các request còn chờ trong Queue ...
                }
                break;
            case 5:
                threadPoolExecutor = PoolThreadConfig.getThreadPoolExecutor();
                while (i < responseDTOList.size()) {
                    FractionalQuesThread fractionalQuesThread = new FractionalQuesThread(responseDTOList.get(i));
                    Future<ResultDTO> future = threadPoolExecutor.submit(fractionalQuesThread);
                    ResultDTO result = future.get();
                    sumScore += result.getScore();
                    countPass += result.getCountPass();
                    sumAnswerId.addAll(result.getAnswerId());
                    i++;
                }
                threadPoolExecutor.shutdown();
                while (!threadPoolExecutor.isTerminated()) {
                    // Chờ xử lý hết các request còn chờ trong Queue ...
                }
                break;
        }
        return new ResultDTO(sumScore, countPass, sumAnswerId);
    }
}
