package com.udabe.repository;

import com.udabe.dto.criteria.CriteriaDetailIds;
import com.udabe.dto.criteria.CriteriaReceiveDTO;
import com.udabe.dto.criteria.CriteriaSetDTO;
import com.udabe.dto.criteria.receive.CriteriaSetDTOFormReceive;
import com.udabe.dto.criteria.receive.EvaluationReceiveDTO;
import com.udabe.entity.CriteriaSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CriteriaSetRepository extends JpaRepository<CriteriaSet, Long> {

    @Query(value = "select criteria_set_id as criteriaSetId from criteria_set where applied_status = 'Y'", nativeQuery = true)
    Long getCriteriaSetIdApply();

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaSetDTO(" +
            "c.criteriaSetId, c.criteriaSetCode, c.criteriaSetName, c.appliedStatus, c.appliedDate, c.criteriaVersion, c.createdAt " +
            ") " +
            "FROM CriteriaSet c where c.appliedStatus = ?1")
    List<CriteriaSetDTO> findByAppliedStatus(String applied);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaSetDTO(" +
            "c.criteriaSetId, c.criteriaSetCode, c.criteriaSetName, c.appliedStatus, c.appliedDate, c.criteriaVersion, c.createdAt " +
            ") " +
            "FROM CriteriaSet c where c.criteriaSetId = ?1")
    Optional<CriteriaSetDTO> findCriteriaSetById(Long criteriaSetId);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaSetDTO(" +
            "c.criteriaSetId, c.criteriaSetCode, c.criteriaSetName, c.appliedStatus, c.appliedDate, c.criteriaVersion, c.createdAt " +
            ") " +
            "FROM CriteriaSet c " +
            "WHERE (:criteriaSetCode IS NULL OR c.criteriaSetCode like CONCAT('%',:criteriaSetCode,'%')) " +
            "AND (:criteriaSetName IS NULL OR c.criteriaSetName like CONCAT('%',:criteriaSetName,'%'))" +
            "AND (:appliedStatus IS NULL OR c.appliedStatus = :appliedStatus) " +
            "AND  (:timeMinOfDay IS NULL OR c.createdAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR c.createdAt <= :timeMaxOfDay) " +
            "ORDER BY CASE WHEN c.appliedStatus = 'Y' THEN 0 WHEN c.appliedStatus = 'N' THEN 1 ELSE 2 END ")
    Page<CriteriaSetDTO> selectCriteriaSetPage(String criteriaSetCode, String criteriaSetName, String appliedStatus, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaSetDTO(" +
            "c.criteriaSetId, c.criteriaSetCode, c.criteriaSetName, c.appliedStatus, c.appliedDate, c.criteriaVersion, c.createdAt " +
            ") " +
            "FROM CriteriaSet c " +
            "WHERE c.appliedStatus <> 'N'" +
            "AND (:criteriaSetCode IS NULL OR c.criteriaSetCode like CONCAT('%',:criteriaSetCode,'%')) " +
            "AND (:criteriaSetName IS NULL OR c.criteriaSetName like CONCAT('%',:criteriaSetName,'%'))" +
            "AND (:criteriaVersion IS NULL OR c.criteriaVersion like CONCAT('%',:criteriaVersion,'%'))" +
            "AND (:appliedStatus IS NULL OR c.appliedStatus = :appliedStatus)" +
            "AND (:timeMinOfDay IS NULL OR c.createdAt >= :timeMinOfDay)" +
            "AND (:timeMaxOfDay IS NULL OR c.createdAt <= :timeMaxOfDay)" +
            "ORDER BY CASE WHEN c.appliedStatus = 'Y' THEN 0 ELSE 1 END")
    Page<CriteriaSetDTO> versionCriteriaSetPage(String criteriaSetCode, String criteriaSetName, String appliedStatus,
                                                String criteriaVersion, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE criteria_set SET criteria_set_code = ?1 , criteria_set_name = ?2 where criteria_set_id = ?3", nativeQuery = true)
    void updateCriteriaSet(String criteriaSetCode, String criteriaSetName, Long criteriaSetId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE criteria_set SET applied_status = 'E' WHERE applied_status = 'Y'", nativeQuery = true)
    void updateStatusApplied();

    @Modifying
    @Transactional
    @Query(value = "UPDATE criteria_set SET applied_status = 'R' WHERE applied_status = 'Y'", nativeQuery = true)
    void updateStatusRecall();

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaSetDTO(" +
            "c.criteriaSetId, c.criteriaSetCode, c.criteriaSetName, c.appliedStatus, c.appliedDate, c.criteriaVersion, c.createdAt) " +
            "FROM CriteriaSet c WHERE c.criteriaSetId = ?1")
    CriteriaSetDTO getCriteriaSetById(Long criDraftId);

    @Query(value = "SELECT c.criteria_version FROM criteria_set c WHERE c.criteria_version = ?1", nativeQuery = true)
    String findByCriteriaVersion(String criteriaVersion);

    @Query(value = "select count(*) from criteria_detail cd inner join criteria_class_3 c on cd.criteria_class_3_id = c.criteria_class_3_id " +
            "inner join criteria_class_2 cc2 on c.criteria_class_2_id = cc2.criteria_class_2_id " +
            "inner join criteria_class_1 cc1 on cc2.criteria_class_1_id = cc1.criteria_class_1_id " +
            "inner join criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id where cs.criteria_set_id = ?1", nativeQuery = true)
    Double getTotalClassDetail(Long criteriaSetId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE criteria_detail cd " +
            "    INNER JOIN criteria_class_3 c ON cd.criteria_class_3_id = c.criteria_class_3_id " +
            "    INNER JOIN criteria_class_2 cc2 ON c.criteria_class_2_id = cc2.criteria_class_2_id " +
            "    INNER JOIN criteria_class_1 cc1 ON cc2.criteria_class_1_id = cc1.criteria_class_1_id " +
            "    INNER JOIN criteria_set cs ON cc1.criteria_set_id = cs.criteria_set_id " +
            "SET cd.point = ?1 " +
            "WHERE cs.criteria_set_id = ?2", nativeQuery = true)
    void updatePoint(Double value , Long criteriaSetId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM criteria_set WHERE applied_status = 'Y'")
    CriteriaSet getApplied();

    @Query(nativeQuery = true,
            value = "SELECT * FROM criteria_set WHERE criteria_set_id = ?1")
    CriteriaSet getAppliedReceive(Long criteriaSetId);

//    @Query(nativeQuery = true,
//            value = "select cd.criteria_detail_id from criteria_set cs inner join criteria_class_1 c on cs.criteria_set_id = c.criteria_set_id " +
//                    "inner join criteria_class_2 cc2 on c.criteria_class_1_id = cc2.criteria_class_1_id " +
//                    "inner join criteria_class_3 cc3 on cc2.criteria_class_2_id = cc3.criteria_class_2_id " +
//                    "inner join criteria_detail cd on cc3.criteria_class_3_id = cd.criteria_class_3_id " +
//                    "where cs.applied_status = 'Y' ")
//    List<CriteriaDetailIds> getCriteriaDetailIdList();


    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaDetailIds(" +
            "cd.criteriaDetailId " +
            ") " +
            "FROM CriteriaSet c inner join c.criteriaClass1s c1 inner join c1.criteriaClass2s c2 inner join c2.criteriaClass3s " +
            "c3 inner join c3.criteriaDetails cd where c.appliedStatus = 'Y' AND c1.criteriaClass1Id = ?1 ")
    List<CriteriaDetailIds> getCriteriaDetailIdList(Long criteriaClass1Id);


//    @Query("SELECT NEW com.udabe.dto.criteria.receive.EvaluationReceiveDTO(" +
//            "ev.evaluationVersionId, ev.createdAt, ev.isSend, ev.criteriaSet.criteriaSetId " +
//            ") " +
//            "FROM EvaluationVersion ev where ev.users.userID = ?1 AND ev.isSend = TRUE " +
//            "ORDER BY ev.createdAt DESC")
//    Page<EvaluationReceiveDTO> getReceiveForm(Long userId, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.criteria.receive.EvaluationReceiveDTO(" +
            "ev.evaluationVersionId, ev.createdAt, ev.isSend, ev.versionName, ev.status, ev.sumScore, ev.sumPercent , ev.criteriaSet.criteriaSetId, u.urbanStatus " +
            ") " +
            "FROM EvaluationVersion ev left join ev.users u where ev.users.userID = :userId AND ev.isSend = TRUE " +
            "AND (:versionName IS NULL OR ev.versionName like CONCAT('%', :versionName, '%')) " +
            "AND (:timeMinOfDay IS NULL OR ev.createdAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR ev.createdAt <= :timeMaxOfDay) " +
            "AND (:status IS NULL OR ev.status like CONCAT('%', :status, '%')) " +
            "ORDER BY ev.updatedAt DESC")
    Page<EvaluationReceiveDTO> getReceiveForm(Long userId, String versionName, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, String status, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.criteria.receive.EvaluationReceiveDTO(" +
            "ev.evaluationVersionId, ev.createdAt, ev.isSend, ev.versionName, ev.status, ev.sumScore, ev.sumPercent , ev.criteriaSet.criteriaSetId, u.urbanStatus " +
            ") " +
            "FROM EvaluationVersion ev left join ev.users u where ev.users.userID = :userId AND ev.isSend = TRUE ORDER BY ev.updatedAt DESC")
    List<EvaluationReceiveDTO> getReceiveFormFirst(Long userId);


    @Query("SELECT NEW com.udabe.dto.criteria.receive.EvaluationReceiveDTO(" +
            "ev.evaluationVersionId, ev.createdAt, ev.isSend, ev.versionName, ev.status, ev.sumScore, ev.sumPercent , ev.criteriaSet.criteriaSetId, u.urbanStatus " +
            ") " +
            "FROM EvaluationVersion ev left join ev.users u where ev.evaluationVersionId = :evaluationVersionId")
    EvaluationReceiveDTO getReceiveFormDetail(Long evaluationVersionId);


    @Query("SELECT NEW com.udabe.dto.criteria.receive.CriteriaSetDTOFormReceive(" +
            "c.criteriaSetId , c.criteriaSetName, c.criteriaVersion " +
            ") " +
            "FROM CriteriaSet c where c.criteriaSetId = ?1")
    CriteriaSetDTOFormReceive getReceiveFormSet(Long criteriaSetId);


    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaReceiveDTO(" +
            "ev.evaluationVersionId, ev.versionName, cs.criteriaVersion, ev.createdAt, ev.timeReturn, evu.statusEvaluate, ev.statusRecognition, evu.commentRecognition, cs.criteriaSetId, evu.users.userID, ev.users.userID, evu.users.fullName, evu.users.userName, evu.users.position, evu.users.organization, evu.point " +
            ") " +
            "FROM EvaluationVersion ev left join ev.criteriaSet cs left join ev.evaluationVersionUsers evu where ev.users.userID = ?1 and evu.statusRecognition != null ")
    List<CriteriaReceiveDTO> receiveResult(Long userId);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaReceiveDTO(" +
            "ev.evaluationVersionId, ev.versionName, cs.criteriaVersion, ev.createdAt, ev.timeReturn, evu.statusEvaluate, ev.statusRecognition " +
            ") " +
            "FROM EvaluationVersion ev " +
            "LEFT JOIN ev.criteriaSet cs " +
            "LEFT JOIN ev.evaluationVersionUsers evu " +
            "WHERE ev.users.userID = ?1 " +
            "AND ev.sumPercent = 100 " +
            "AND evu.statusRecognition is null")
    List<CriteriaReceiveDTO> receiveResultNotSendCouncil(Long userId);



    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaReceiveDTO(" +
            "ev.evaluationVersionId, ev.versionName, cs.criteriaVersion, ev.createdAt, ev.timeReturn, evu.statusEvaluate, ev.statusRecognition, evu.commentRecognition, cs.criteriaSetId, evu.users.userID, ev.users.userID, evu.users.fullName, evu.users.userName, evu.users.position, evu.users.organization, evu.point " +
            ") " +
            "FROM EvaluationVersion ev inner join ev.criteriaSet cs left join ev.evaluationVersionUsers evu where ev.users.userID = ?1 and ev.evaluationVersionId = ?2 group by ev.evaluationVersionId")
    List<CriteriaReceiveDTO> detailReceiveResult(Long userId, Long evaluationVersionId);
    @Query(nativeQuery = true,
            value = "SELECT criteria_set_id FROM criteria_set WHERE applied_status = 'Y'")
    Long getCriteriaSetAppliedID();

    @Query(nativeQuery = true,
    value = "SELECT criteria_set_code FROM criteria_set WHERE criteria_set_code = :criteriaSetCode AND criteria_set_id <> :criteriaSetId")
    String[] findByCriteriaSetCode(String criteriaSetCode, Long criteriaSetId);
}
