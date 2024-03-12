package com.udabe.repository;

import com.udabe.entity.UserFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserFunctionRepository extends JpaRepository<UserFunction, Long> {

    @Query(nativeQuery = true,
            value = "SELECT COUNT(uf.user_function_id) " +
                    "FROM user_function uf " +
                    "WHERE uf.user_id = ?1")
    Long countUserFunction(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT df.dashboard_function_id " +
                    "FROM dashboard_function df " +
                    "INNER JOIN user_function uf on df.dashboard_function_id = uf.dashboard_function_id " +
                    "INNER JOIN users u on uf.user_id = u.user_id " +
                    "WHERE u.user_id = ?1")
    List<Long> getListFunctionIdByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT df.function_name as functionName " +
                    "FROM dashboard_function df " +
                    "WHERE df.dashboard_function_id = ?1 ")
    String getFunctionName(Long DashboardFunctionId);

    @Query("SELECT CASE WHEN count(uf) > 0 THEN true ELSE false END FROM UserFunction uf " +
            "WHERE uf.users.userID = ?1 ")
    Boolean existsByDashboardFunctionId(Long userID);

    @Query(nativeQuery = true,
            value = "SELECT ef.dashboard_function_id FROM user_function ef WHERE ef.user_id =:userID")
    Long[] findUserFunctionIdByUserID(Long userID);

    @Query(nativeQuery = true,
            value = "SELECT ef.user_function_id FROM user_function ef WHERE ef.user_id =:userID AND ef.dashboard_function_id= :dashboardFunctionId")
    Long findUserFunction(Long userID, Long dashboardFunctionId);

    @Query(nativeQuery = true,
            value = "insert into user_function (user_id, dashboard_function_id) values (:userID,:DashboardFunctionId)")
    void saveUserFunction(Long userID, Long DashboardFunctionId);


    @Query(nativeQuery = true,
            value = "delete from user_function where user_id = :userID and dashboard_function_id=:DashboardFunctionId")
    void deleteUserFunction(Long userID, Long DashboardFunctionId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "delete from user_function where dashboard_function_id=:dashboardFunctionId and user_id= :userID")
    void deleteUserFunctionByUserFunctionId(Long dashboardFunctionId, Long userID);

    @Query(nativeQuery = true,
            value = "update user_function set user_id = :userID, dashboard_function_id = :dashboardFunctionId , body_config = :bodyConfig where user_function_id = :userFunctionId")
    void saveUserFunctionBodyConfig(Long userFunctionId, Long userID, Long dashboardFunctionId, String bodyConfig);
}
