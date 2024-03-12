package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.exception.ResourceNotFoundException;
import com.udabe.dto.criteria.form.AnswerDTOForm;
import com.udabe.entity.*;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.*;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.CriteriaDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriteriaDetailServiceImpl extends BaseCrudService<CriteriaDetail, Long> implements CriteriaDetailService {

    private static CriteriaDetailRepository criteriaDetailRepository;

    private final CriteriaClass3Repository criteriaClass3Repository;

    private final EvaluationRepository evaluationRepository;

    private static AnswerRepository answerRepository;

    @Autowired
    public CriteriaDetailServiceImpl(CriteriaDetailRepository criteriaDetailRepository,
                                     CriteriaClass3Repository criteriaClass3Repository,
                                     EvaluationRepository evaluationRepository,
                                     AnswerRepository answerRepository) {
        this.criteriaDetailRepository = criteriaDetailRepository;
        this.criteriaClass3Repository = criteriaClass3Repository;
        this.evaluationRepository = evaluationRepository;
        this.answerRepository = answerRepository;
        super.setRepository(criteriaDetailRepository);
    }


    @Override
    public ResponseEntity<?> saveCriteriaDetail(CriteriaDetail criteriaDetail) {
        criteriaDetail.setCriteriaClass3(criteriaClass3Repository.findById(criteriaDetail.getCriteriaClass3Id()).get());
        CriteriaDetail criteriaClassDetailSave = criteriaDetailRepository.save(criteriaDetail);
        CriteriaSetServiceImpl.division(criteriaClassDetailSave.getCriteriaDetailId(), "add");
        return ResponseEntity.ok(new Response().setData(criteriaClassDetailSave).setMessage("Saved!"));
    }


    @Override
    public ResponseEntity<?> updateCriteriaDetail(CriteriaDetail criteriaDetail, Long criteriaDetailId) {
        Optional<CriteriaDetail> criteriaDetailFind = criteriaDetailRepository.findById(criteriaDetailId);
        if (criteriaDetailFind.isPresent()) {
            if(criteriaDetailFind.get().getEvaluationType() != criteriaDetail.getEvaluationType()) {
                evaluationRepository.deleteWhenModifyType(criteriaDetailFind.get().getCriteriaDetailId());
            }
            Long criteriaSetId =  criteriaDetailRepository.findCriteriaSetIdByDetailId(criteriaDetailId);
            String[] symbols = criteriaDetailRepository.findByCriteriaDetailSymbol(criteriaDetail.getSymbol(),criteriaDetailId, criteriaSetId);
            if (symbols.length > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Ký hiệu tiêu chí đã được sử dụng trong bộ chỉ số"));
            }
            criteriaDetail.setPoint(criteriaDetailFind.get().getPoint());
            criteriaDetail.setCriteriaClass3(criteriaDetailFind.get().getCriteriaClass3());
            criteriaDetailRepository.save(criteriaDetail);
            return ResponseEntity.ok("Successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không thể lưu tiêu chí!");
        }
    }

    public static boolean checkNullEvalution(Long criteriaDetailId) {
        CriteriaDetail criteriaDetailFind = criteriaDetailRepository.findById(criteriaDetailId).get();
        for(Evaluation evaluation : criteriaDetailFind.getEvaluations()) {
            if(evaluation.getCriteriaDetail().getEvaluationType() == 2L) {
                if(evaluation.getPercentPass() == null) {
                    return true;
                }
                if (isNullOrWhitespace(evaluation.getValue()) || isNullOrWhitespace(evaluation.getPercentPass().toString())) {
                    return true;
                }
            }
            if(isNullOrWhitespace(evaluation.getValue())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }


    public static AnswerDTOForm getAnswerOfUser(Long criteriaDetailId) {
        CriteriaDetail criteriaDetailFind = criteriaDetailRepository.findById(criteriaDetailId).get();
        Long userIdLogin = getUserLoginId();
        Long criteriaSetId = criteriaDetailFind.getCriteriaClass3().getCriteriaClass2().getCriteriaClass1().getCriteriaSet().getCriteriaSetId();
        return answerRepository.getAnswerOfUser(userIdLogin, criteriaSetId, criteriaDetailId);
    }


    public static AnswerDTOForm getAnswerOfUserReceive(Long criteriaDetailId, Long evaluationVersionId, Long userId) {
        CriteriaDetail criteriaDetailFind = criteriaDetailRepository.findById(criteriaDetailId).get();
        Long criteriaSetId = criteriaDetailFind.getCriteriaClass3().getCriteriaClass2().getCriteriaClass1().getCriteriaSet().getCriteriaSetId();
        return answerRepository.getAnswerOfUserReceive(userId, criteriaSetId, criteriaDetailId, evaluationVersionId);
    }


    public static Long getUserLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }


}
