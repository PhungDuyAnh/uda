package com.udabe.repository;

import com.udabe.entity.EvaluationVersion;
import com.udabe.entity.EvaluationVersionUser;
import com.udabe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EvaluationVersionUserRepository extends JpaRepository<EvaluationVersionUser, Integer> {

    EvaluationVersionUser findByEvaluationVersionAndUsers(EvaluationVersion evaluationVersion, Users users);

    List<EvaluationVersionUser> findByEvaluationVersion(EvaluationVersion evaluationVersion);

    boolean existsByEvaluationVersionAndUsers(EvaluationVersion evaluationVersion, Users users);

    @Query(value = "select SUM(number_score) from council_score where evaluation_version_user_id = ?1", nativeQuery = true)
    Float getTotalScore(Integer evaluationVersionUserId);

    @Transactional
    @Query(value = "update evaluation_version_user set point = ?1 where evaluation_version_user_id = ?2", nativeQuery = true)
    void updatePointCouncil(Float point, Integer evaluationVersionUserId);

    @Query(value = "select point from evaluation_version_user where evaluation_version_user_id = ?1", nativeQuery = true)
    Float getCouncilScore(Integer evaluationVersionUserId);

    @Query(value = "select count(*) from council_score where evaluation_version_user_id = ?1", nativeQuery = true)
    Integer countNumCouncilScore(Integer evaluationVersionUserId);

    @Query(value = "SELECT COUNT(criteria_detail_id) FROM criteria_detail cd JOIN criteria_class_3 c on cd.criteria_class_3_id = c.criteria_class_3_id " +
            "INNER JOIN criteria_class_2 cc2 on c.criteria_class_2_id = cc2.criteria_class_2_id INNER JOIN criteria_class_1 cc1 on " +
            "cc2.criteria_class_1_id = cc1.criteria_class_1_id INNER JOIN criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id WHERE cs.criteria_set_id = ?1", nativeQuery = true)
    Integer countNumCriteriaDetail(Long criteriaSetId);

}
