package com.udabe.repository;

import com.udabe.dto.Dashboard.UserDashboardDTO;
import com.udabe.dto.user.*;
import com.udabe.entity.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    //Tìm tài khoản chủ tịch + thư ký:
    @Query(value = "select * from users where account_type = 2 and status_council = 'N' and council_type in(1,2)", nativeQuery = true)
    List<Users> getChairManAndSecretary();

    @Query(value = "select * from users where disable = 'N'", nativeQuery = true)
    List<Users> findUserActive();

    Optional<Users> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean existsByAddressCodeId(Long addressCodeId);

    Boolean existsByPhoneNumber(String phoneNumber);


    @Query("SELECT NEW com.udabe.dto.user.UserDTODetail(" +
            "u.userID, u.userName, u.fullName, u.email, u.disable, u.phoneNumber, u.accountType, u.urbanType, " +
            "u.organization, u.position, u.createdAt, u.updatedAt, ur.role.roleID, u.disableCouncil, u.councilType " +
            ") " +
            "FROM Users u LEFT JOIN u.userRoles ur " +
            "WHERE u.userID = ?1 ")
    UserDTODetail findByIdDTO(Long userId);


    //Vô hiệu hoá, kích hoạt User:
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.disable = :disable WHERE u.userID = :userId")
    void disable(@Param("disable") String disable, @Param("userId") Long userId);


    @Query("SELECT NEW com.udabe.dto.user.UserDTOAll(" +
            "u.userID, u.userName, u.fullName, u.email, u.disable, u.phoneNumber, u.accountType, u.createdAt " +
            ") " +
            "FROM Users u " +
            "WHERE (:disable IS NULL OR CAST(u.disable AS string) like CONCAT('%', :disable, '%')) AND " +
            "(:accountType IS NULL OR u.accountType = :accountType) " +
            "AND (:userName IS NULL OR u.userName like CONCAT('%', :userName, '%')) ")
    Page<UserDTOAll> selectAllUsers(@Param("disable") String disable,
                                    @Param("accountType") Long accountType,
                                    @Param("userName") String userName,
                                    Pageable pageable);

    @Query("SELECT NEW com.udabe.dto.user.UserUrbanDTO(" +
            "u.userID, u.fullName, u.urbanStatus, u.updatedAt " +
            ") " +
            "FROM Users u " +
            "WHERE u.accountType = 3 AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) AND (:urbanStatusRe IS NULL OR u.urbanStatus like CONCAT('%', :urbanStatusRe, '%'))  " +
            "AND (:timeMinOfDay IS NULL OR u.updatedAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR u.updatedAt <= :timeMaxOfDay) ")
    Page<Object> selectAllUrbanAcc(@Param("fullName") String fullName, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, String urbanStatusRe, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.user.UrbanCouncilDTO(" +
            "u.userID, u.userID, u.fullName, u.urbanStatus, u.statusCouncil, cs.criteriaSetId, cs.criteriaVersion, ev.evaluationVersionId, ev.versionName, ev.statusRecognition, ev.updatedAt " +
            ") " +
            "FROM Users u inner join u.evaluationVersions ev inner join ev.criteriaSet cs " +
            "WHERE u.accountType = 3 AND ev.sumPercent = 100 " +
            "AND (u.userID, ev.updatedAt) IN(SELECT u.userID, MAX(ev.updatedAt) FROM Users u JOIN u.evaluationVersions ev " +
            "WHERE u.accountType = 3 AND ev.sumPercent = 100 GROUP BY u.userID) AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:urbanStatusRe IS NULL OR u.urbanStatus like CONCAT('%', :urbanStatusRe, '%')) " +
            "AND (:versionName IS NULL OR ev.versionName  like CONCAT('%', :versionName, '%')) " +
            "AND (:timeMinOfDay IS NULL OR ev.updatedAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR ev.updatedAt <= :timeMaxOfDay) ")
    Page<Object> selectAllUrbanAccCouncil(@Param("fullName") String fullName, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, String urbanStatusRe, String versionName, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.user.UrbanCouncilDTO(" +
            "ev.users.userID, u.userID, ev.users.fullName, ev.users.urbanStatus, ev.users.statusCouncil, cs.criteriaSetId, cs.criteriaVersion, ev.evaluationVersionId, ev.versionName, ev.updatedAt ,evu.statusEvaluate " +
            ") " +
            "FROM Users u inner join u.evaluationVersionUsers evu inner join evu.evaluationVersion ev inner join ev.criteriaSet cs " +
            "WHERE evu.users.userID = :userId " +
            "AND (:fullName IS NULL OR ev.users.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:urbanStatusRe IS NULL OR ev.users.urbanStatus like CONCAT('%', :urbanStatusRe, '%')) " +
            "AND (:versionName IS NULL OR ev.versionName  like CONCAT('%', :versionName, '%')) " +
            "AND (:timeMinOfDay IS NULL OR ev.updatedAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR ev.updatedAt <= :timeMaxOfDay) ")
    Page<Object> selectAllUrbanAccCouncilMember(@Param("fullName") String fullName, LocalDateTime timeMinOfDay, LocalDateTime timeMaxOfDay, String urbanStatusRe, String versionName, Long userId, Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.user.UserCouncilDTO(" +
            "u.userID, u.userName, u.fullName, u.email, u.disable, u.disableCouncil, u.phoneNumber, u.accountType, u.councilType, u.organization, u.position, u.createdAt " +
            ") " +
            "FROM Users u " +
            "WHERE u.accountType = 2 AND u.councilType != null AND u.councilType != 4 AND (:disable IS NULL OR CAST(u.disable AS string) like CONCAT('%', :disable, '%')) " +
            "AND (:disableCouncil IS NULL OR CAST(u.disableCouncil AS string) like CONCAT('%', :disableCouncil, '%')) " +
            "AND (:councilType IS NULL OR u.councilType = :councilType) " +
            "AND (:userName IS NULL OR u.userName like CONCAT('%', :userName, '%')) " +
            "AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:organization IS NULL OR u.organization like CONCAT('%', :organization, '%')) ")
    Page<UserCouncilDTO> selectAllCouncil(@Param("disable") String disable,
                                          @Param("disableCouncil") String disableCouncil,
                                          @Param("councilType") Long councilType,
                                          @Param("userName") String userName,
                                          @Param("fullName") String fullName,
                                          @Param("organization") String organization,
                                          Pageable pageable);


    @Query("SELECT NEW com.udabe.dto.user.UserCouncilDTO(" +
            "u.userID, u.userName, u.fullName, u.email, u.disable, u.disableCouncil, u.phoneNumber, u.accountType, u.councilType, u.organization, u.position, u.createdAt " +
            ") " +
            "FROM Users u " +
            "WHERE u.accountType = 2 " +
            "AND u.councilType = 3 and u.disableCouncil = 'N' " +
            "AND ((:type = 'add' AND u.userID NOT IN (SELECT evu2.users.userID FROM EvaluationVersionUser evu2 WHERE evu2.evaluationVersion.id = :evaluationVersionId)) " +
            "OR (:type = 'delete' AND u.userID IN (SELECT evu2.users.userID FROM EvaluationVersionUser evu2 WHERE evu2.evaluationVersion.id = :evaluationVersionId))) " +
            "AND (:disable IS NULL OR CAST(u.disable AS string) LIKE CONCAT('%', :disable, '%')) " +
            "AND (:disableCouncil IS NULL OR CAST(u.disableCouncil AS string) LIKE CONCAT('%', :disableCouncil, '%')) " +
            "AND (:councilType IS NULL OR u.councilType = :councilType) " +
            "AND (:userName IS NULL OR u.userName LIKE CONCAT('%', :userName, '%')) " +
            "AND (:fullName IS NULL OR u.fullName LIKE CONCAT('%', :fullName, '%')) " +
            "AND (:organization IS NULL OR u.organization LIKE CONCAT('%', :organization, '%'))")
    Page<UserCouncilDTO> selectAllCouncilScore(
            @Param("evaluationVersionId") Long evaluationVersionId,
            @Param("type") String type,
            @Param("disable") String disable,
            @Param("disableCouncil") String disableCouncil,
            @Param("councilType") Long councilType,
            @Param("userName") String userName,
            @Param("fullName") String fullName,
            @Param("organization") String organization,
            Pageable pageable
    );


    @Query("SELECT NEW com.udabe.dto.user.UserCouncilDTO(" +
            "u.userID, u.userName, u.fullName, u.email, u.disable, u.disableCouncil, u.phoneNumber, u.accountType, u.councilType, u.organization, u.position, u.createdAt " +
            ") " +
            "FROM Users u " +
            "WHERE u.accountType = 2 AND u.councilType = null AND u.disable = 'N' " +
            "AND (:userName IS NULL OR u.userName like CONCAT('%', :userName, '%')) " +
            "AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:councilType IS NULL OR u.councilType = :councilType) " +
            "OR " +
            "u.accountType = 2 AND u.councilType = 4 AND u.disable = 'N' " +
            "AND (:userName IS NULL OR u.userName like CONCAT('%', :userName, '%')) " +
            "AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:councilType IS NULL OR u.councilType = :councilType) ")
    Page<UserCouncilDTO> selectAllCouncilNull(@Param("userName") String userName,
                                              @Param("fullName") String fullName,
                                              @Param("councilType") Long councilType,
                                              Pageable pageable);


//    @Query("SELECT NEW com.udabe.dto.user.UserCouncilDTO(" +
//            "u.userID, u.userName, u.fullName, u.email, u.disable, u.disableCouncil, u.phoneNumber, u.accountType, u.councilType, u.organization, u.position, u.createdAt " +
//            ") " +
//            "FROM Users u " +
//            "WHERE u.accountType = 2 AND u.councilType = 4 ")
//    List<UserCouncilDTO> selectAllCouncilType4();


    Optional<Users> findByEmail(String email);


    @Query(nativeQuery = true,
            value = "SELECT * FROM users WHERE email = ?1 ")
    Optional<Users> findOtpByEmail(String email);


    @Query(value = "select address_code_id from users where user_id =?1", nativeQuery = true)
    Long findAddressUser(Long userId);


    @Query(value = "SELECT CASE WHEN COUNT(*) = 0 THEN 'false' ELSE 'true' END AS result FROM users u WHERE u.address_code_id = ?1 AND u.user_id != ?2", nativeQuery = true)
    Boolean existsByAddressCodeIdAndIdNot(Long addressCodeId, Long userId);


    @Query(nativeQuery = true,
            value = "SELECT council_type FROM users WHERE user_id = ?1")
    Long findCouncilType(Long userId);


    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM Users u where u.accountType = 2 and u.councilType = 1 ")
    Boolean existsByCouncilType();


    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.disableCouncil = :disableCouncil WHERE u.userID = :userId")
    void disableCouncil(@Param("disableCouncil") String disableCouncil, @Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET council_type = 1 WHERE user_id = ?1", nativeQuery = true)
    void updateCouncilType(Long userId);


    @Query(nativeQuery = true,
            value = "SELECT disable_council FROM users WHERE user_id = ?1")
    String findDisableCouncil(Long userId);


    @Query("SELECT NEW com.udabe.dto.user.UserCouncilScoreDTO(" +
            "ev.users.userID, u.userID, u.fullName, u.userName, u.position, u.organization, evu.point, evu.statusEvaluate ,evu.evaluationVersion.criteriaSet.criteriaSetId, ev.evaluationVersionId " +
            ") " +
            "FROM Users u inner join u.evaluationVersionUsers evu inner join evu.evaluationVersion ev inner join ev.criteriaSet cs " +
            "WHERE evu.evaluationVersion.evaluationVersionId = ?1 ")
    List<UserCouncilScoreDTO> getAllCouncilSend(Long evaluationVersionId);

    @Query(nativeQuery = true,
            value = "SELECT u.full_name FROM users u where u.user_id =?1")
    String getFullNameByUserID(Long aLong);

    @Query(nativeQuery = true,
            value = "SELECT phone_number FROM users WHERE phone_number = :phoneNumber")
    String[] selectPhoneNumber(String phoneNumber);

    @Query(nativeQuery = true,
            value = "SELECT phone_number FROM users WHERE phone_number = :phoneNumber and user_id <> :userId")
    String[] selectPhoneNumberByUserId(String phoneNumber, Long userId);

    @Query(nativeQuery = true,
            value = "SELECT user_name FROM users WHERE user_name = :userName and user_id <> :userId")
    String[] checkUserName(String userName, Long userId);

    @Query(nativeQuery = true,
            value = "SELECT email FROM users WHERE email = :email and user_id <> :userId")
    String[] checkByEmail(String email, Long userId);

    @Query(value = "select count(evu.user_id) from evaluation_version_user evu where evu.user_id = ?1 and evu.status_evaluate = 'N'", nativeQuery = true)
    Integer checkCouncilScoreLeft(Long userId);

}
