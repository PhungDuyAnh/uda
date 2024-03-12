package com.udabe.repository;

import com.udabe.dto.Dashboard.*;
import com.udabe.dto.user.AddressCodeDTO;
import com.udabe.entity.AddressCode;
import com.udabe.entity.Users;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardRepository extends JpaRepository<Users, Long> {

    @Query("SELECT NEW com.udabe.dto.Dashboard.DashboardDTO(" +
            "u.userID, u.fullName, ev.sumScore, u.urbanStatus, ev.sumPercent ) " +
            "FROM Users u " +
            "INNER JOIN u.evaluationVersions ev " +
            "INNER JOIN ev.criteriaSet cs " +
            "WHERE cs.appliedStatus = 'Y' AND u.accountType = 3 AND u.fullName like %?1%")
    List<DashboardDTO> getUrban(String userName);

    @Query("SELECT NEW com.udabe.dto.Dashboard.DashboardDTONoEvaluation(" +
            "u.userID, u.fullName, u.urbanStatus) " +
            "FROM Users u " +
            "WHERE u.accountType = 3 AND u.fullName like %?1%")
    List<DashboardDTONoEvaluation> getUrban2(String userName);

    @Query("SELECT NEW com.udabe.dto.Dashboard.DashboardDTONoEvaluation(" +
            "u.userID, u.fullName, u.urbanStatus ) " +
            "FROM Users u " +
            "WHERE u.accountType = 3 ")
    List<DashboardDTONoEvaluation> getUrban3();

    @Query("SELECT ac.addressCodeId " +
            "FROM AddressCode ac " +
            "WHERE ac.addressName like %?1%")
    Long listIdByName(String addressName);


    @Query("SELECT NEW com.udabe.dto.Dashboard.DashboardAllUrbanDTO(" +
            "u.userID, u.fullName, u.urbanStatus) " +
            "FROM Users u " +
            "WHERE u.accountType = 3 ")
    List<DashboardAllUrbanDTO> findAllProvince();

    @Query("SELECT NEW com.udabe.dto.Dashboard.UserDashboardDTO(" +
            "u.userID ,u.userName, u.fullName, u.disable, u.accountType " +
            ") " +
            "FROM Users u " +
            "WHERE u.disable = 'N' order by u.createdAt desc")
    List<UserDashboardDTO>  selectAllUsersLimitByDashboard(PageRequest pageRequest);

    @Query("SELECT New com.udabe.dto.Dashboard.CriteriaSetDashboardDTO(" +
            "cs.criteriaSetId ,cs.criteriaSetCode, cs.criteriaSetName, cs.appliedDate ) " +
            "FROM CriteriaSet cs " +
            "WHERE cs.appliedStatus = 'Y' ")
    CriteriaSetDashboardDTO getCriteriaDashboard();

    @Query(nativeQuery = true,
            value = "SELECT u.user_id as userID, u.full_name as fullName, ev.sum_percent as sumPercent, u.urban_status as urbanStatus FROM users u " +
                    "INNER JOIN evaluation_version ev on u.user_id = ev.user_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE cs.applied_status = 'Y' AND u.disable = 'N' order by ev.created_at DESC LIMIT 5 ")
    List<NewestEvaluationRegisterUserDTO> listNewestEvaluationRegisterUser();


    @Query(nativeQuery = true,
            value = "SELECT u.user_id as userID ,u.user_name as userName, u.full_name as fullName, u.council_type as councilType FROM users u " +
                    "WHERE u.disable = 'N' AND u.account_type = 2 AND u.council_type is not null  " +
                    "group by u.user_id order by u.council_type ASC, u.updated_at DESC LIMIT 5")
    List<CouncilDashboardDTO> councilDashboardList();

    @Query(nativeQuery = true,
            value = "SELECT ac.latitude as latitude FROM address_code ac " +
                    "INNER JOIN users u on ac.address_code_id = u.address_code_id " +
                    "WHERE u.user_id = ?1")
    Double findLatitudeByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT ac.longitude as longitude FROM address_code ac " +
                    "INNER JOIN users u on ac.address_code_id = u.address_code_id " +
                    "WHERE u.user_id = ?1")
    Double findlongitudeByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId ,ev.version_name as versionName ,ev.updated_at as updatedAt, " +
                    "ev.sum_score as sumScore, cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version ev " +
                    "JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE cs.applied_status = 'Y' AND ev.user_id = :userId " +
                    "order by ev.updated_at desc limit 5")
    List<EvaluationVersionDashboardDTO> evaluationVersionDashboard(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId ,ev.version_name as versionName, " +
                    "ev.time_return as timeReturn, evu.status_recognition as statusRecognition, cs.criteria_version as criteriaVersion, " +
                    "ev.sum_score as sumScore " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN evaluation_version_user evu on ev.evaluation_version_id = evu.evaluation_version_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE ev.user_id = ?1 " +
                    "order by evu.time_return desc limit 1 ")
    Optional<EvaluationVersionWithResultDashboardDTO> evaluationVersionWithResultDashboard(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT evu.evaluation_version_user_id as evaluationVersionUserId, u.full_name as fullName, " +
                    " ev.version_name as versionName, evu.point as point, ev.evaluation_version_id as evaluationVersionId, " +
                    "cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version_user evu " +
                    "INNER JOIN users u on evu.user_id = u.user_id " +
                    "INNER JOIN evaluation_version ev on evu.evaluation_version_id = ev.evaluation_version_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "order by evu.time_return desc limit 5")
    List<EvaluationCouncilResultDTO> evaluationCouncilResult();

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.version_name as versionName, " +
                    "cs.criteria_version as criteriaVersion, evu.time_return as timeReturn, evu.status_evaluate as statusEvaluate, " +
                    "evu.status_recognition as statusRecognition, cs.criteria_set_id as criteriaSetId " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "INNER JOIN evaluation_version_user evu on ev.evaluation_version_id = evu.evaluation_version_id " +
                    "INNER JOIN  users u on ev.user_id = u.user_id " +
                    "order by evu.time_return desc limit 5")
    List<EvaluationVerNewestDTO> evaluationVerNewest();

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId , u.full_name as fullName, ev.version_name as versionName, " +
                    "cs.criteria_version as criteriaVersion, evu.status_recognition as statusRecogntion " +
                    "FROM evaluation_version ev " +
                    "INNER JOIN users u on ev.user_id = u.user_id " +
                    "INNER JOIN  evaluation_version_user evu on ev.evaluation_version_id = evu.evaluation_version_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "WHERE evu.user_id = ?1 " +
                    "order by ev.created_at desc limit 5")
    List<EvaluationVerCouncil3DTO> evaluationVerCouncil3(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId ,ev.sum_percent as sumPercent, ev.sum_score as sumscore " +
                    "FROM evaluation_version ev " +
                    "JOIN users u on ev.user_id = u.user_id " +
                    "WHERE u.user_id = ?1 AND ev.status = 'T' OR u.user_id = ?1 AND ev.status = 'Y' " +
                    "order by ev.created_at desc limit 1 ")
    List<EvaluationVerScoreAndPercentDTO> scoreAndPercent(Long userId);

    @Query("SELECT ev.sumScore " +
            "FROM EvaluationVersion ev " +
            "JOIN ev.users u " +
            "WHERE u.userID = ?1 order by ev.createdAt desc ")
    Float sumScoreFind(Long userID, PageRequest pageRequest);

    @Query("SELECT ev.sumPercent " +
            "FROM EvaluationVersion ev " +
            "JOIN ev.users u " +
            "WHERE u.userID = ?1 order by ev.createdAt desc ")
    Float sumPercentFind(Long userID, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "SELECT ev.evaluation_version_id as evaluationVersionId, ev.version_name as versionName, evu.point as point , evu.time_return as timeReturn, " +
                    "evu.status_evaluate as statusEvaluate, cs.criteria_version as criteriaVersion, cs.criteria_set_id as criteriaSetId, u.full_name as fullName " +
                    "FROM evaluation_version_user evu " +
                    "INNER JOIN users u on evu.user_id = u.user_id " +
                    "INNER JOIN evaluation_version ev on evu.evaluation_version_id = ev.evaluation_version_id " +
                    "INNER JOIN criteria_set cs on ev.criteria_set_id = cs.criteria_set_id " +
                    "AND u.user_id = :userId " +
                    "order by ev.updated_at desc limit 5")
    List<EvaluationCouncilResultByUserDTO> evaluationCouncilResultByUser(Long userId);

}
