package com.udabe.repository;

import com.udabe.dto.AtumaticGrading.ResponseDTO;
import com.udabe.dto.criteria.CriteriaDetailDTO;
import com.udabe.entity.CriteriaClass3;
import com.udabe.entity.CriteriaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CriteriaDetailRepository extends JpaRepository<CriteriaDetail, Long> {

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaDetailDTO(" +
            "c.criteriaDetailId, c.contentVi, c.note , c.symbol, c.applyLevel, c.conditions, c.unitOfMeasure , c.settingConditions, c.evaluationType, c.point " +
            ") " +
            "FROM CriteriaDetail c where c.criteriaClass3.criteriaClass3Id = ?1")
    List<CriteriaDetailDTO> getAllCriteriaDetailByClass3Id(Long criteriaClass3Id);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaDetailDTO(" +
            "c.criteriaDetailId, c.contentVi, c.note, c.symbol, c.applyLevel, c.conditions, c.unitOfMeasure, c.settingConditions, c.evaluationType, c.point " +
            ") " +
            "FROM CriteriaDetail c where c.criteriaDetailId = ?1")
    CriteriaDetailDTO findCriteriaDetailById(Long criteriaDetailId);

    List<CriteriaDetail> findAllByCriteriaClass3(CriteriaClass3 criteriaClass3);


    @Query(nativeQuery = true,
    value = "SELECT COUNT(criteria_detail_id) FROM criteria_detail cd JOIN criteria_class_3 c on cd.criteria_class_3_id = c.criteria_class_3_id " +
            "INNER JOIN criteria_class_2 cc2 on c.criteria_class_2_id = cc2.criteria_class_2_id " +
            "INNER JOIN criteria_class_1 cc1 on cc2.criteria_class_1_id = cc1.criteria_class_1_id " +
            "INNER JOIN criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id WHERE cs.applied_status = 'Y'")
    Long countCriteriaDetail(Long userID);


    @Query(nativeQuery = true,
    value = "SELECT COUNT(criteria_detail_id) FROM criteria_detail cd " +
            "INNER JOIN criteria_class_3 c on cd.criteria_class_3_id = c.criteria_class_3_id " +
            "INNER JOIN criteria_class_2 cc2 on c.criteria_class_2_id = cc2.criteria_class_2_id " +
            "INNER JOIN criteria_class_1 cc1 on cc2.criteria_class_1_id = cc1.criteria_class_1_id " +
            "INNER JOIN criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id " +
            "WHERE cs.applied_status = 'Y' AND cc1.criteria_class_1_id = ?1")
    Long countDetailByClass1(Long criteriaClass1Id);


    @Query("SELECT NEW com.udabe.dto.AtumaticGrading.ResponseDTO(" +
            "a.answerId, cd. criteriaDetailId, a.valueAnswer, cd.evaluationType, cd.point, cd.conditions, cd.settingConditions )" +
            "FROM EvaluationVersion ev " +
            "JOIN Answer a ON ev.evaluationVersionId = a.evaluationVersion.evaluationVersionId " +
            "JOIN CriteriaDetail cd ON a.criteriaDetail.criteriaDetailId = cd.criteriaDetailId " +
            "WHERE ev.evaluationVersionId = ?1 and cd.evaluationType !=2")
    List<ResponseDTO> findResponseValue(Long criteriaSetId);

    @Query("SELECT NEW com.udabe.dto.AtumaticGrading.ResponseDTO(" +
            "a.answerId, cd. criteriaDetailId, a.evaluation.evaluationId, cd.evaluationType, cd.point, e.percentPass)" +
            "FROM EvaluationVersion ev " +
            "JOIN Answer a ON ev.evaluationVersionId = a.evaluationVersion.evaluationVersionId " +
            "JOIN CriteriaDetail cd ON a.criteriaDetail.criteriaDetailId = cd.criteriaDetailId JOIN Evaluation e ON a.evaluation.evaluationId = e.evaluationId " +
            "WHERE ev.evaluationVersionId = ?1 and cd.evaluationType = 2 ORDER BY cd.criteriaDetailId")
    List<ResponseDTO> findResponseValueType2(Long criteriaSetId);

    @Query(nativeQuery = true,
            value = "SELECT criteria_detail_id FROM criteria_detail WHERE criteria_class_3_id = ?1 ")
    Long[] getClassDetailIDs(Long criteriaClass3Id);

    @Query(nativeQuery = true,
    value = "SELECT symbol FROM criteria_detail " +
            "join criteria_class_3 cc3 on cc3.criteria_class_3_id = criteria_detail.criteria_class_3_id " +
            "join criteria_class_2 cc2 on cc2.criteria_class_2_id = cc3.criteria_class_2_id " +
            "join criteria_class_1 cc1 on cc1.criteria_class_1_id = cc2.criteria_class_1_id " +
            "join criteria_set cs on cs.criteria_set_id = cc1.criteria_set_id " +
            "WHERE criteria_detail_id <> :criteriaDetailId AND symbol = :symbol AND cs.criteria_set_id = :criteriaSetId ")
    String[] findByCriteriaDetailSymbol(String symbol, Long criteriaDetailId, Long criteriaSetId);

    @Query(nativeQuery = true,
    value = "select cs.criteria_set_id from criteria_set cs join criteria_class_1 cc1 on cs.criteria_set_id = cc1.criteria_set_id " +
            "join criteria_class_2 cc2 on cc1.criteria_class_1_id = cc2.criteria_class_1_id " +
            "join criteria_class_3 cc3 on cc2.criteria_class_2_id = cc3.criteria_class_2_id " +
            "join criteria_detail cd on cc3.criteria_class_3_id = cd.criteria_class_3_id " +
            "where cd.criteria_detail_id = :criteriaDetailId ")
    Long findCriteriaSetIdByDetailId(Long criteriaDetailId);
}
