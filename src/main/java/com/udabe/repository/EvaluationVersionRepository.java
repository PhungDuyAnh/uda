package com.udabe.repository;

import com.udabe.dto.EvaluationVersion.EvaluationVersionStatusDTO;
import com.udabe.dto.EvaluationVersion.StaEvaluationVersionDTO;
import com.udabe.dto.EvaluationVersion.EvaluationVersionDTO;
import com.udabe.dto.EvaluationVersion.EvaluationVersionScorePercentDTO;
import com.udabe.entity.EvaluationVersion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface EvaluationVersionRepository extends JpaRepository<EvaluationVersion, Long> {

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.version_name as versionName, ev.status as status, ev.is_send as isSend, " +
                    "u.user_id as userId, cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version ev INNER JOIN users u on ev.user_id = u.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE u.user_id = ?1 AND ev.is_send = false AND cs.applied_status = 'Y' ")
    EvaluationVersionDTO getLastestVersion(Long userId);


    @Query("SELECT CASE WHEN count(ev) > 0 THEN true ELSE false END FROM EvaluationVersion ev where ev.users.userID = ?1 " +
            "AND ev.isSend = false ")
    Boolean existsByUserId(Long userId);


    @Query("SELECT CASE WHEN count(ev) > 0 THEN true ELSE false END FROM EvaluationVersion ev where ev.versionName = ?1 " +
            " AND ev.users.userID = ?2 ")
    Boolean existsByVersionName(String versionName, Long userId);


    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.version_name as versionName, ev.status as status, ev.is_send as isSend, " +
                    "u.user_id as userId, cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version ev INNER JOIN users u on ev.user_id = u.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE u.user_id = ?1 AND ev.version_name is not null " +
                    "ORDER BY ev.evaluation_version_id DESC LIMIT 1")
    EvaluationVersionDTO getLatestVersionById(Long userId);

    @Modifying
    @Transactional
    @Query(value = "update evaluation_version set sum_score = ?1, sum_percent = ?2 , status = 'T' where evaluation_version_id=?3", nativeQuery = true)
    void updateScoreUser(float sumScore, float sumPercent, Long evaluationVersionId);

    @Query(nativeQuery = true,
            value = "SELECT ev.sum_score " +
                    "FROM evaluation_version ev " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) AS updated_at " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ?1 AND MONTH(ev2.updated_at) = MONTH(?2) " +
                    "AND ev2.status = 'T' AND YEAR(ev2.updated_at) = YEAR (?2));")
    Float getScore(Long aLong, String date);

    @Query(nativeQuery = true,
            value = "SELECT ev.sum_percent " +
                    "FROM evaluation_version ev " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) AS updated_at " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ?1 AND MONTH(ev2.updated_at) = MONTH(?2) AND ev2.status = 'T' " +
                    "AND ev2.status = 'T' AND YEAR(ev2.updated_at) = YEAR (?2))")
    Float getPercent(Long aLong, String date);

    @Query(nativeQuery = true,
            value = "SELECT ev.sum_score " +
                    "FROM evaluation_version ev " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) AS updated_at " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ?1 " +
                    "AND ev2.status = 'T' AND YEAR(ev2.updated_at) = YEAR (?2));")
    Float getScoreYear(Long aLong, String date);

    @Query(nativeQuery = true,
            value = "SELECT ev.sum_percent " +
                    "FROM evaluation_version ev " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) AS updated_at " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ?1 AND ev2.status = 'T' " +
                    "AND ev2.status = 'T' AND YEAR(ev2.updated_at) = YEAR (?2))")
    Float getPercentYear(Long aLong, String date);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT ev.user_id FROM evaluation_version ev WHERE ev.status = 'T'")
    int[] countUrbanEvaluatedAuto();

    @Query(nativeQuery = true,
            value = "SELECT ev.sum_score as sumScore, ev.sum_percent as sumPercent FROM evaluation_version ev INNER JOIN users u on ev.user_id = u.user_id " +
                    "WHERE u.user_id = ?1 AND ev.is_send = true " +
                    "AND status = 'T' order by ev.evaluation_version_id DESC limit 1;")
    EvaluationVersionScorePercentDTO getLatestVersionByUserId(int userId);

    @Query(nativeQuery = true,
            value = "SELECT ev.user_id AS userID, u.full_name AS fullName, ev.sum_score AS sumScore, ev.sum_percent AS sumPercent, ac.address_name AS district, ac2.address_name AS province " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN users u ON ev.user_id = u.user_id  " +
                    "INNER JOIN address_code ac ON u.address_code_id = ac.address_code_id " +
                    "INNER JOIN address_code ac2 ON ac2.address_code_id = ac.upper_address_seq " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "INNER JOIN users u2 ON ev2.user_id = u2.user_id " +
                    "WHERE YEAR(ev2.updated_at) = YEAR(:date) " +
                    "AND u2.user_id = ev.user_id)" +
                    "AND ev.status = 'T' " +
                    "AND (:addressCodeId IS NULL OR ac.upper_address_seq = :addressCodeId) ",
            countQuery = "SELECT COUNT(ev.user_id) " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN users u ON ev.user_id = u.user_id  " +
                    "INNER JOIN address_code ac ON u.address_code_id = ac.address_code_id " +
                    "INNER JOIN address_code ac2 ON ac2.address_code_id = ac.upper_address_seq " +
                    "WHERE ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "INNER JOIN users u2 ON ev2.user_id = u2.user_id " +
                    "WHERE YEAR(ev2.updated_at) = YEAR(:date) " +
                    "AND u2.user_id = ev.user_id)" +
                    "AND ev.status = 'T' " +
                    "AND (:addressCodeId IS NULL OR ac.upper_address_seq = :addressCodeId) ")
    Page<StaEvaluationVersionDTO> staAllUrbanScorePercent(@Param("addressCodeId") Long addressCodeId, LocalDate date, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT ev.user_id AS userID, u.full_name AS fullName, ev.sum_score AS sumScore, ev.sum_percent AS sumPercent, ac.address_name AS district, ac2.address_name AS province " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN users u ON ev.user_id = u.user_id  " +
                    "INNER JOIN address_code ac ON u.address_code_id = ac.address_code_id " +
                    "INNER JOIN address_code ac2 ON ac2.address_code_id = ac.upper_address_seq " +
                    "WHERE ev.status = 'T' " +
                    "AND (:addressCodeId IS NULL OR ac.upper_address_seq = :addressCodeId) " +
                    "and year(ev.updated_at) >= year(:startYear) " +
                    "and year(ev.updated_at) <= year(:endYear) " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "join users u2 on ev2.user_id = u2.user_id " +
                    "where year(ev2.updated_at) = year(ev.updated_at) " +
                    "and u2.user_id = ev.user_id) ",
            countQuery = "SELECT COUNT(ev.user_id) " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN users u ON ev.user_id = u.user_id  " +
                    "INNER JOIN address_code ac ON u.address_code_id = ac.address_code_id " +
                    "INNER JOIN address_code ac2 ON ac2.address_code_id = ac.upper_address_seq " +
                    "WHERE ev.status = 'T' " +
                    "AND (:addressCodeId IS NULL OR ac.upper_address_seq = :addressCodeId) " +
                    "and year(ev.updated_at) >= year(:startYear) " +
                    "and year(ev.updated_at) <= year(:endYear) " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "join users u2 on ev2.user_id = u2.user_id " +
                    "where year(ev2.updated_at) = year(ev.updated_at) " +
                    "and u2.user_id = ev.user_id) ")
    Page<StaEvaluationVersionDTO> staAllUrbanScorePercentMultiYear(@Param("addressCodeId") Long addressCodeId, LocalDate startYear, LocalDate endYear, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(evaluation_version_id) from evaluation_version ev " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE cs.applied_status = 'Y' AND ev.user_id = ?1 ")
    Long sum(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.user_id) FROM evaluation_version ev WHERE ev.status = 'T' AND YEAR(ev.updated_at) = YEAR(?1)")
    int countUrbanEvaluatedAutoInYear(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(distinct ev.evaluation_version_id) FROM evaluation_version ev where ev.status='T' AND YEAR(ev.updated_at) = YEAR(?1);")
    int countEvaluationVersionInYear(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(distinct ev.evaluation_version_id) FROM evaluation_version ev where ev.status='T' AND YEAR(ev.updated_at) >= YEAR(?1) AND YEAR(ev.updated_at) <= YEAR(?2);")
    int countEvaluationVersionInMultiYear(LocalDate startYear, LocalDate endYear);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.user_id) FROM evaluation_version ev WHERE ev.status = 'T' AND YEAR(ev.updated_at) >= YEAR(?1) AND YEAR(ev.updated_at) <= YEAR(?2)")
    int countUrbanEvaluatedAutoInMultiYear(LocalDate startYear, LocalDate endYear);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.user_id) FROM evaluation_version ev WHERE ev.status = 'T' AND MONTH(ev.updated_at) = MONTH(?1) AND YEAR(ev.updated_at) = YEAR(?1)")
    int countUrbanEvaluatedByMonth(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.evaluation_version_id) FROM evaluation_version ev WHERE ev.status = 'T' AND MONTH(ev.updated_at) = MONTH(?1) AND YEAR(ev.updated_at) = YEAR(?1)")
    int countRegisEvaluatedByMonth(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.user_id) FROM evaluation_version ev WHERE ev.status = 'T' AND YEAR(ev.updated_at) = YEAR(?1)")
    int countUrbanEvaluatedByYear(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT ev.evaluation_version_id) FROM evaluation_version ev WHERE ev.status = 'T' AND YEAR(ev.updated_at) = YEAR(?1)")
    int countRegisEvaluatedByYear(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(distinct ev.user_id) " +
                    "FROM evaluation_version ev " +
                    "join answer a on ev.evaluation_version_id = a.evaluation_version_id " +
                    "join criteria_detail cd on cd.criteria_detail_id = a.criteria_detail_id " +
                    "WHERE ev.status = 'T' " +
                    "AND YEAR(ev.updated_at) = YEAR(:yearLD) " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ev.user_id " +
                    "AND YEAR(ev2.updated_at) = YEAR(:yearLD) " +
                    "AND ev2.status = 'T') " +
                    "AND (:isPass IS NULL OR a.is_pass = true)" +
                    "AND a.criteria_detail_id = :criteriaDetail")
    int countUrbanJoinOrPassCriteriaCInYear(Long criteriaDetail, LocalDate yearLD, Boolean isPass);

    @Query(nativeQuery = true,
            value = "SELECT ev.user_id " +
                    "FROM evaluation_version ev " +
                    "join answer a on ev.evaluation_version_id = a.evaluation_version_id " +
                    "join criteria_detail cd on cd.criteria_detail_id = a.criteria_detail_id " +
                    "WHERE ev.status = 'T' " +
                    "AND YEAR(ev.updated_at) = YEAR(:yearLD) " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "WHERE ev2.user_id = ev.user_id " +
                    "AND YEAR(ev2.updated_at) = YEAR(:yearLD) " +
                    "AND ev2.status = 'T') " +
                    "AND (:isPass IS NULL OR a.is_pass = true)" +
                    "AND a.criteria_detail_id = :criteriaDetail")
    int[] getUrbanJoinOrPassCriteria4CInYear(Long criteriaDetail, LocalDate yearLD, Boolean isPass);

    @Query(nativeQuery = true,
            value = "SELECT ev.user_id " +
                    "FROM evaluation_version ev " +
                    "join criteria_set cs on cs.criteria_set_id = ev.criteria_set_id " +
                    "WHERE ev.status = 'T' " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "JOIN criteria_set cs2 ON cs2.criteria_set_id = ev2.criteria_set_id " +
                    "WHERE ev2.user_id = ev.user_id " +
                    "AND year(ev2.updated_at) = year(:year) " +
                    "AND cs2.applied_status = 'Y')")
    int[] countUrbanEvaluatedAutoInYearBy(LocalDate year);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT a.answer_id) FROM answer a " +
                    "JOIN evaluation_version ev ON ev.evaluation_version_id = a.evaluation_version_id " +
                    "WHERE  ev.user_id = :urbanJoinCriteriaSetID " +
                    "AND a.criteria_detail_id = :criteriaDetailId AND a.is_pass = true " +
                    "AND ev.updated_at = (SELECT MAX(ev2.updated_at) " +
                    "FROM evaluation_version ev2 " +
                    "join criteria_set cs on cs.criteria_set_id = ev2.criteria_set_id " +
                    "where ev2.user_id =:urbanJoinCriteriaSetID) ")
    int checkPassCriteriaDetailsByUserID(int urbanJoinCriteriaSetID, Long criteriaDetailId);

    @Query(nativeQuery = true,
            value = "SELECT criteria_set_id from evaluation_version where evaluation_version_id = :evaluationVersionId")
    Long getCriteriaSetId(Long evaluationVersionId);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.version_name as versionName, ev.status as status, ev.is_send as isSend, " +
                    "u.user_id as userId, cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version ev INNER JOIN users u on ev.user_id = u.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE u.user_id = ?1 order by ev.evaluation_version_id DESC limit 1 ")
    EvaluationVersionDTO getLastestVersionByUserId(Long userLoginId);


//    @Query(nativeQuery = true,
//            value = "SELECT ev.status_recognition as statusRecognition " +
//                    "FROM users u " +
//                    "INNER JOIN evaluation_version ev on u.user_id = ev.user_id " +
//                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
//                    "WHERE cs.applied_status = 'Y' AND ev.time_return is not null AND u.user_id = ?1 " +
//                    "ORDER BY ev.created_at DESC LIMIT 1 ")
//    String statusReFind(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.status_recognition as statusRecognition, u.urban_status as urbanStatus " +
                    "FROM users u " +
                    "INNER JOIN evaluation_version ev on u.user_id = ev.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE cs.applied_status = 'Y' AND u.user_id = ?1 AND ev.evaluation_version_id != ?2 " +
                    "ORDER BY ev.created_at DESC LIMIT 1 ")
    EvaluationVersionStatusDTO statusReFind(Long userId, Long idFind);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evalutionVersionId " +
                    "FROM users u " +
                    "INNER JOIN evaluation_version ev on u.user_id = ev.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE cs.applied_status = 'Y' AND u.user_id = ?1 AND ev.is_send <> false " +
                    "ORDER BY ev.created_at DESC LIMIT 1 ")
    Optional<Long> statusReFind2(Long userId);
}
