package com.udabe.repository;

import com.udabe.dto.criteria.form.AnswerDTOForm;
import com.udabe.entity.Answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(answer_id) FROM answer a JOIN evaluation_version ev on a.evaluation_version_id = ev.evaluation_version_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE  cs.applied_status = 'Y' AND ev.is_send = false AND ev.user_id = ?1  ")
    Long countAnswer(Long userId);


    //Tìm câu trả lời của User:
    @Query("SELECT NEW com.udabe.dto.criteria.form.AnswerDTOForm(" +
            "a.answerId, a.valueAnswer, a.evaluation.evaluationId , a.isPass" +
            ") " +
            "FROM Answer a inner join a.evaluationVersion ev where ev.users.userID = ?1 and ev.criteriaSet.criteriaSetId = ?2 and a.criteriaDetail.criteriaDetailId = ?3 and ev.isSend = false ")
    AnswerDTOForm getAnswerOfUser(Long userId, Long criteriaSetId, Long criteriaDetailId);


    //Tìm câu trả lời của User receive:
    @Query("SELECT NEW com.udabe.dto.criteria.form.AnswerDTOForm(" +
            "a.answerId, a.valueAnswer, a.evaluation.evaluationId , a.isPass" +
            ") " +
            "FROM Answer a inner join a.evaluationVersion ev where ev.users.userID = ?1 and ev.criteriaSet.criteriaSetId = ?2 and a.criteriaDetail.criteriaDetailId = ?3 and ev.isSend = TRUE and ev.evaluationVersionId = ?4 ")
    AnswerDTOForm getAnswerOfUserReceive(Long userId, Long criteriaSetId, Long criteriaDetailId, Long evaluationVersionId);


    @Query(nativeQuery = true,
            value = "SELECT COUNT(answer_id) FROM answer a INNER JOIN criteria_detail cd on cd.criteria_detail_id = a.criteria_detail_id " +
                    "INNER JOIN criteria_class_3 c on cd.criteria_class_3_id = c.criteria_class_3_id " +
                    "INNER JOIN criteria_class_2 cc2 on c.criteria_class_2_id = cc2.criteria_class_2_id " +
                    "INNER JOIN criteria_class_1 cc1 on cc2.criteria_class_1_id = cc1.criteria_class_1_id " +
                    "INNER JOIN criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id " +
                    "INNER JOIN evaluation_version ev on a.evaluation_version_id = ev.evaluation_version_id " +
                    "WHERE cs.applied_status = 'Y' AND ev.is_send = false AND cc1.criteria_class_1_id = ?1 AND ev.user_id = ?2 ")
    Long countAnswerByClass1(Long criteria_class_1_id, Long userId);

    @Modifying
    @Transactional
    @Query(value = "update answer set is_pass = true where answer_id = ?1", nativeQuery = true)
    void updateAnswer(Long answerId);

    @Modifying
    @Transactional
    @Query(value = "update answer set is_pass = true where answer_id IN ?1", nativeQuery = true)
    void updateAllAnswer(List<Long> answerIds);

    @Query(value = "Select count(*) from answer where evaluation_version_id = ?1 and is_pass = true", nativeQuery = true)
    int countPass(Long evaluationVersionId);

    @Query(value = "Select count(*) from answer where evaluation_version_id = ?1 and is_pass is null ", nativeQuery = true)
    int countFail(Long evaluationVersionId);

    @Query(value = "Select count(*) " +
            "from answer a join criteria_detail cd on cd.criteria_detail_id = a.criteria_detail_id " +
            "join evaluation e on cd.criteria_detail_id = e.criteria_detail_id " +
            "where a.evaluation_version_id = :evaluationVersionId and a.is_pass is null and e.percent_pass = 100;", nativeQuery = true)
    int countIncomplete(Long evaluationVersionId);

    @Query(value = "SELECT a.answer_id " +
            "from answer a join criteria_detail cd on cd.criteria_detail_id = a.criteria_detail_id " +
            "join evaluation e on cd.criteria_detail_id = e.criteria_detail_id " +
            "where a.answer_id = :answerId and a.is_pass is null and e.percent_pass = 100", nativeQuery = true)
    Long findIncomplete(Long answerId);
}
