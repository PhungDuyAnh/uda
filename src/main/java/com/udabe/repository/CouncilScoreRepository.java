package com.udabe.repository;

import com.udabe.entity.CouncilScore;
import com.udabe.entity.CriteriaClass1;
import com.udabe.entity.CriteriaDetail;
import com.udabe.entity.EvaluationVersionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilScoreRepository extends JpaRepository<CouncilScore, Integer> {

    @Query(value = "select * from council_score where criteria_detail_id = ?1 and evaluation_version_user_id = " +
            "(select evaluation_version_user_id from evaluation_version_user where user_id = ?2 and evaluation_version_id = ?3)" ,nativeQuery = true)
    CouncilScore getPointCouncil(Long criteriaDetailId, Long userId, Long evaluationVersionId);

    boolean existsByCriteriaDetailAndEvaluationVersionUser(CriteriaDetail criteriaDetail, EvaluationVersionUser evaluationVersionUser);

    CouncilScore findByCriteriaDetailAndEvaluationVersionUser(CriteriaDetail criteriaDetail, EvaluationVersionUser evaluationVersionUser);

}
