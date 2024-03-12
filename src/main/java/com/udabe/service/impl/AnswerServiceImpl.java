package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.dto.user.AddressCodeDTOInter;
import com.udabe.entity.*;
import com.udabe.repository.*;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.AnswerService;

import org.apache.commons.lang.StringUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AnswerServiceImpl extends BaseCrudController<Answer, Long> implements AnswerService {

    public static AnswerRepository answerRepository;

    private final UsersRepository usersRepository;

    private static CriteriaDetailRepository criteriaDetailRepository;

    private final EvaluationRepository evaluationRepository;

    private static EvaluationVersionRepository evaluationVersionRepository;

    private final CriteriaSetRepository criteriaSetRepository;

    private static AddressCodeRepository addressCodeRepository;

    private static UsersServiceImpl usersServiceImpl;


    public AnswerServiceImpl(AnswerRepository answerRepository, UsersRepository usersRepository, CriteriaDetailRepository criteriaDetailRepository, EvaluationRepository evaluationRepository, EvaluationVersionRepository evaluationVersionRepository, CriteriaSetRepository criteriaSetRepository, AddressCodeRepository addressCodeRepository, UsersServiceImpl usersServiceImpl) {

        this.answerRepository = answerRepository;
        this.usersRepository = usersRepository;
        this.criteriaDetailRepository = criteriaDetailRepository;
        this.evaluationRepository = evaluationRepository;
        this.evaluationVersionRepository = evaluationVersionRepository;
        this.criteriaSetRepository = criteriaSetRepository;
        this.addressCodeRepository = addressCodeRepository;
        this.usersServiceImpl = usersServiceImpl;
    }


    @Override
    public ResponseEntity<?> addAnswer(List<Answer> answer, EvaluationVersion evaluationVersion){

        if(!evaluationVersionRepository.existsByUserId(getUserLoginId())) {
            saveEvaluationVersion(evaluationVersion);
        }
        List<Answer> temp = new ArrayList<>();
        for(Answer i : answer) {
            Long criteriaDetailId = i.getCriteriaDetailId();
            CriteriaDetail citeriaFind = criteriaDetailRepository.findById(criteriaDetailId).get();
            i.setCriteriaDetail(citeriaFind);
            if(citeriaFind.getEvaluationType() == 5){
                String firstNumber = i.getFirstNumber();
                String secondNumber = i.getSecondNumber();
                String result = firstNumber + "/" + secondNumber;
                i.setValueAnswer(result);
            }

            Long evaluationId = i.getEvaluationId();
            if (citeriaFind.getEvaluationType() == 2){
                Evaluation evaluationFind = evaluationRepository.findById(evaluationId).get();
                i.setEvaluation(evaluationFind);
            }
            else{
                i.setEvaluation(null);
            }
            i.setEvaluationVersionId(evaluationVersionRepository.getLastestVersionByUserId(getUserLoginId()).getEvaluationVersionId());
            EvaluationVersion evaluationVersionFind = evaluationVersionRepository.findById(i.getEvaluationVersionId()).get();
            i.setEvaluationVersion(evaluationVersionFind);
            temp.add(i);
        }
        answerRepository.saveAll(temp);
        return ResponseEntity.ok("Saved");
    }


    @Override
    public ResponseEntity<?> updateAnswer(List<Answer> answer){

        List<Answer> temp = new ArrayList<>();
        for(Answer i: answer){
            Answer answerUpdate = answerRepository.getById(i.getAnswerId());
            if(answerUpdate != null){
                i.setCriteriaDetail(answerUpdate.getCriteriaDetail());
                CriteriaDetail citeriaFind = criteriaDetailRepository.findById(i.getCriteriaDetail().getCriteriaDetailId()).get();
                if(citeriaFind.getEvaluationType() == 5){
                    String firstNumber = i.getFirstNumber();
                    String secondNumber = i.getSecondNumber();
                    String result = firstNumber + "/" + secondNumber;
                    answerUpdate.setValueAnswer(result);
                }
                else {
                    answerUpdate.setValueAnswer(i.getValueAnswer());
                }
                Long evaluationId = i.getEvaluationId();
                if(citeriaFind.getEvaluationType() == 2) {
                    Evaluation evaluationFind = evaluationRepository.findById(evaluationId).get();
                    answerUpdate.setEvaluation(evaluationFind);
                }
                else{
                    i.setEvaluation(answerUpdate.getEvaluation());
                }
                i.setEvaluationVersion(answerUpdate.getEvaluationVersion());
                temp.add(answerUpdate);
            }
        }
        answerRepository.saveAll(temp);
        return ResponseEntity.ok("Update success");
    }


    public static Long getUserLoginId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }


    void saveEvaluationVersion(EvaluationVersion evaluationVersion){

        evaluationVersion.setUserId(getUserLoginId());
        Users userFind = usersRepository.findById(evaluationVersion.getUserId()).get();
        Long criteriaSetFind = criteriaSetRepository.getApplied().getCriteriaSetId();
        evaluationVersion.setUsers(userFind);
        evaluationVersion.setVersionName(null);
        evaluationVersion.setStatus("N");
        evaluationVersion.setSend(false);
        evaluationVersion.setEvaluationVersionId(criteriaSetFind);
        CriteriaSet criteriaSet = criteriaSetRepository.findById(evaluationVersion.getEvaluationVersionId()).get();
        evaluationVersion.setCriteriaSet(criteriaSet);
        evaluationVersionRepository.save(evaluationVersion);
    }

    public static String createVersionName(){

        List<Character> temp = new ArrayList<>();
        Optional<AddressCodeDTOInter> addressCodeDTOInter = addressCodeRepository.addressByUser(getUserLoginId());
        if(addressCodeDTOInter.isPresent()) {
            String address = addressCodeDTOInter.get().getAddressName();
            Long versionNumber;
            char c[] = address.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] != ' ' && (i == 0 || c[i - 1] == ' ')) {
                    temp.add(c[i]);
                }
            }
            String addressName = StringUtils.join(temp, "").toUpperCase(Locale.ROOT);
            versionNumber = 0001L;
            String result1 = String.format("ĐKĐG/%s/%04d", addressName, versionNumber);
            if (evaluationVersionRepository.existsByVersionName(result1, getUserLoginId())) {
                String versionFind = evaluationVersionRepository.getLatestVersionById(getUserLoginId()).getVersionName();
                versionFind = versionFind.replaceAll("ĐKĐG/" + addressName + "/", "");
                long versionFind2 = Long.parseLong(versionFind);
                String result2 = String.format("ĐKĐG/%s/%04d", addressName, versionFind2 + 0001L);
                return result2;
            }
            else {
                return result1;
            }
        }
        else {
            return null;
        }
    }


    public static boolean getNumAnswersOfUser() {

        Long answerCount = answerRepository.countAnswer(getUserLoginId());
        Long criteriaVersionCount = criteriaDetailRepository.countCriteriaDetail(getUserLoginId());
        if(answerCount.equals(criteriaVersionCount)) {
            return true;
        }
        return false;
    }


    public static boolean getNumAnswerClass1(Long criteriaClass1Id){

        Long answerCountByClass1 = answerRepository.countAnswerByClass1(criteriaClass1Id, getUserLoginId());
        Long detailCountByClass1 = criteriaDetailRepository.countDetailByClass1(criteriaClass1Id);
        if(answerCountByClass1.equals(detailCountByClass1)){
            return true;
        }
        return false;
    }
}
