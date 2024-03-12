package com.udabe.cmmn.util;

import com.udabe.cmmn.config.PoolThreadConfig;
import com.udabe.cmmn.thread.*;
import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.AtumaticGrading.ResultDTO;
import com.udabe.entity.EvaluationVersion;
import com.udabe.repository.AnswerRepository;
import com.udabe.repository.CriteriaDetailRepository;
import com.udabe.repository.EvaluationVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AutoEvaluationUtil {

    private static CriteriaDetailRepository criteriaDetailRepository;
    private static EvaluationVersionRepository evaluationVersionRepository;
    private static AnswerRepository answerRepository;

    public AutoEvaluationUtil(CriteriaDetailRepository criteriaDetailRepository,
                              EvaluationVersionRepository evaluationVersionRepository, AnswerRepository answerRepository) {
        AutoEvaluationUtil.criteriaDetailRepository = criteriaDetailRepository;
        AutoEvaluationUtil.evaluationVersionRepository = evaluationVersionRepository;
        AutoEvaluationUtil.answerRepository = answerRepository;
    }

    public static void autoEvaluationHandler(Long evaluationVersionId) throws ExecutionException, InterruptedException {
        /**
         * 1. Trắc nghiệm có/không?
         * 2. Trắc nghiệm lựa chọn đơn.
         * 3. Nhập liệu theo tỷ lệ %.
         * 4. Nhập liệu số.
         * 5. Nhập liệu phân số.
         */

        // Bảng điểm của bộ chỉ số và câu trả lời của đô thị loại 1,3,4,5
        List<ResponseDTO> responseDTOs = criteriaDetailRepository.findResponseValue(evaluationVersionId);

        // Bảng điểm của bộ chỉ số và câu trả lời của đô thị loại 2
        List<ResponseDTO> type2List = criteriaDetailRepository.findResponseValueType2(evaluationVersionId);

        // Tổng hợp Bảng điểm của bộ chỉ số và câu trả lời của đô thị
        responseDTOs.addAll(type2List);

        // Chuyển đổi dạng list sang map
        Map<Long, List<ResponseDTO>> typeToResponseMap = responseDTOs.parallelStream()
                .collect(Collectors.groupingByConcurrent(ResponseDTO::getType));

        //Tạo danh sách loại câu hỏi
        Long[] typeQues = {1L, 2L, 3L, 4L, 5L};

        // Khởi tạo main thread, điểm, số lượng câu trả lời đạt
        int mainThread = 1;
        double sumScore = 0.0;
        float countPass = 0;
        List<Long> sumAllAnswerId = new ArrayList<>();

        // Khởi tạo main ThreadPoolExecutor
        ThreadPoolExecutor mainPool = PoolThreadConfig.getThreadPoolExecutor();
        final long startTime = System.nanoTime();
        do {
            List<ResponseDTO> responseDTOList = typeToResponseMap.getOrDefault(typeQues[mainThread - 1], new ArrayList<>());
            if (responseDTOList.size() > 0) {
                MainQuesThread mainQuesThread = new MainQuesThread(responseDTOList, mainThread);
                Future<ResultDTO> future = mainPool.submit(mainQuesThread);
                ResultDTO resultDTO = future.get();
                sumScore += resultDTO.getScore();
                countPass += resultDTO.getCountPass();
                sumAllAnswerId.addAll(resultDTO.getAnswerId());
            }
            mainThread++;
        } while (mainThread <= 5);
        mainPool.shutdown();
        while (!mainPool.isTerminated()) {
            // Chờ xử lý hết các request còn chờ trong Queue ...
        }

        answerRepository.updateAllAnswer(sumAllAnswerId);

        System.out.println("finish time "+LocalDateTime.now());
        System.out.println("time excute thread " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " MILLISECONDS");
        float sumPercent = (countPass / responseDTOs.size()) * 100;
        System.out.println("final score : " + sumScore);
        System.out.println("final percent : " + sumPercent);
        EvaluationVersion evaluationVersionFind = evaluationVersionRepository.findById(evaluationVersionId).get();
        String formattedSumScore = String.format("%.1f", sumScore);
        String formattedSumPercent = String.format("%.1f", sumPercent);
        evaluationVersionFind.setSumScore(Float.parseFloat(formattedSumScore));
        evaluationVersionFind.setSumPercent(Float.parseFloat(formattedSumPercent));
        evaluationVersionFind.setStatus("T");

//        evaluationVersionRepository.updateScoreUser((float) sumScore, sumPercent, evaluationVersionId);
        evaluationVersionRepository.save(evaluationVersionFind);
        System.out.println("Finished all threads");
    }
}
