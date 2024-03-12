package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.util.AutoEvaluationUtil;
import com.udabe.dto.EvaluationVersion.EvaluationVersionStatusDTO;
import com.udabe.dto.EvaluationVersion.StaCountAnswerDTO;
import com.udabe.cmmn.util.Constant;
import com.udabe.entity.CriteriaSet;
import com.udabe.dto.EvaluationVersion.EvaluationVersionDTO;
import com.udabe.entity.EvaluationVersion;
import com.udabe.entity.Users;
import com.udabe.repository.*;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.EvaluationVersionService;

import com.udabe.socket.NotifiServiceImpl;
import com.udabe.socket.Notify;
import com.udabe.socket.NotifyRepository;
import com.udabe.socket.WSService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
public class EvaluationVersionServiceImpl extends BaseCrudService<EvaluationVersion, Long> implements EvaluationVersionService {

    private static EvaluationVersionRepository evaluationVersionRepository;

    private final UsersRepository usersRepository;

    private final CriteriaSetRepository criteriaSetRepository;

    private final AnswerRepository answerRepository;

    private final CriteriaDetailRepository criteriaDetailRepository;

    private final NotifyRepository notifyRepository;

    private final WSService wsService;

    public EvaluationVersionServiceImpl(EvaluationVersionRepository evaluationVersionRepository, UsersRepository usersRepository, CriteriaSetRepository criteriaSetRepository, AnswerRepository answerRepository, CriteriaDetailRepository criteriaDetailRepository, NotifyRepository notifyRepository, WSService wsService) {
        this.evaluationVersionRepository = evaluationVersionRepository;
        this.usersRepository = usersRepository;
        this.criteriaSetRepository = criteriaSetRepository;
        this.answerRepository = answerRepository;
        this.criteriaDetailRepository = criteriaDetailRepository;
        this.notifyRepository = notifyRepository;
        this.wsService = wsService;
    }


    @Override
    public ResponseEntity<?> sendEvaluationVersion(EvaluationVersion evaluationVersion) {

        Long answerCount = answerRepository.countAnswer(getUserLoginId());
        Long criteriaVersionCount = criteriaDetailRepository.countCriteriaDetail(getUserLoginId());
        Optional<Long> statusRe2 = evaluationVersionRepository.statusReFind2(getUserLoginId());

        if (answerCount.equals(criteriaVersionCount)) {
            EvaluationVersionDTO evaluationVersionDTO = evaluationVersionRepository.getLastestVersion(getUserLoginId());
            Long idFind = evaluationVersionDTO.getEvaluationVersionId();
            EvaluationVersionStatusDTO statusRe = evaluationVersionRepository.statusReFind(getUserLoginId(), idFind);
            if (evaluationVersionDTO != null) {
                EvaluationVersion evaluationUpdate = evaluationVersionRepository.getById(idFind);
                if (statusRe2.isPresent()) {
                    if (statusRe.getStatusRecognition() == null) {
                        return ResponseEntity.badRequest().body("Đăng ký đánh giá trước đó chưa có kết quả");
                    } else if (statusRe.getStatusRecognition().equals("1")) {
                        return ResponseEntity.badRequest().body("Đã được công nhận là đô thị thông minh");
                    } else {
                        evaluationUpdate.setSend(true);
                        evaluationUpdate.setVersionName(evaluationVersion.getVersionName());
                        evaluationVersion.setStatus(evaluationUpdate.getStatus());
                        evaluationVersion.setUsers(evaluationUpdate.getUsers());
                        evaluationVersion.setCriteriaSet(evaluationUpdate.getCriteriaSet());
                        evaluationVersion.setNull(true);
                        EvaluationVersion version = evaluationVersionRepository.save(evaluationUpdate);
                        try {
                            AutoEvaluationUtil.autoEvaluationHandler(version.getEvaluationVersionId());
                            //Notify:
                            NotifiServiceImpl.printMessageUpdateScore(version.getEvaluationVersionId());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ResponseEntity.ok("Cập nhật thành công");
                    }
                } else {
                    evaluationUpdate.setSend(true);
                    evaluationUpdate.setVersionName(evaluationVersion.getVersionName());
                    evaluationVersion.setStatus(evaluationUpdate.getStatus());
                    evaluationVersion.setUsers(evaluationUpdate.getUsers());
                    evaluationVersion.setCriteriaSet(evaluationUpdate.getCriteriaSet());
                    evaluationVersion.setNull(true);
                    EvaluationVersion version = evaluationVersionRepository.save(evaluationUpdate);
                    try {
                        AutoEvaluationUtil.autoEvaluationHandler(version.getEvaluationVersionId());
                        //Notify:
                        NotifiServiceImpl.printMessageUpdateScore(version.getEvaluationVersionId());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return ResponseEntity.ok("Cập nhật thành công");
                }
            } else {
                return ResponseEntity.badRequest().body("Null");
            }
        } else if (answerCount < criteriaVersionCount) {
            evaluationVersion.setNull(false);
            return ResponseEntity.badRequest().body("Thiếu câu trả lời");
        } else {
            return ResponseEntity.badRequest().body("Số câu trả lời không hợp lệ");
        }
    }


    @Override
    public ResponseEntity<?> checkAnswer() {

        Long answerCount = answerRepository.countAnswer(getUserLoginId());
        Long criteriaVersionCount = criteriaDetailRepository.countCriteriaDetail(getUserLoginId());
        if (answerCount.equals(criteriaVersionCount)) {
            return ResponseEntity.ok("Full answer");
        } else {
            return ResponseEntity.ok("Missing answer");
        }
    }


    @Override
    public ResponseEntity<?> getLastestVersion() {

        EvaluationVersionDTO data = evaluationVersionRepository.getLastestVersion(getUserLoginId());
        return ResponseEntity.ok(data);
    }


    public static Long getUserLoginId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    @Override
    public ResponseEntity<?> getCountPassAnswer(Long evaluationVersionId) {
        StaCountAnswerDTO result = new StaCountAnswerDTO();
        result.setCountAnswerPass(answerRepository.countPass(evaluationVersionId));
        result.setCountAnswerFail(answerRepository.countFail(evaluationVersionId));
        result.setCountAnswerIncomplete(answerRepository.countIncomplete(evaluationVersionId));
        return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
    }

}
