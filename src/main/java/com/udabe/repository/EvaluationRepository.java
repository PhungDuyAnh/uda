package com.udabe.repository;

import com.udabe.dto.criteria.EvaluationDTO;
import com.udabe.entity.Evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("SELECT NEW com.udabe.dto.criteria.EvaluationDTO(" +
            "e.evaluationId, e.value, e.percentPass " +
            ") " +
            "FROM Evaluation e where e.evaluationId = ?1")
    List<EvaluationDTO> findEvalutionDetailById(Long evaluationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Evaluation e where e.criteriaDetail.criteriaDetailId = ?1")
    void deleteWhenModifyType(@Param("criteriaDetailId") Long criteriaDetailId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Evaluation e where e.evaluationId = ?1")
    void deleteEvaluationByID(@Param("evalutionId") Long evalutionId);

}
