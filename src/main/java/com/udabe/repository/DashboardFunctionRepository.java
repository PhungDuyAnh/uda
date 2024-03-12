package com.udabe.repository;

import com.udabe.dto.Dashboard.DashboardFunctionDTO;
import com.udabe.entity.DashboardFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardFunctionRepository extends JpaRepository<DashboardFunction, Long> {

    @Query(nativeQuery = true,
            value = "SELECT df.Dashboard_function_id as dashboardFunctionId, df.function_code as functionCode, " +
                    "df.title as title, df.url as url " +
                    "FROM dashboard_function df " +
                    "JOIN function_role fr on df.dashboard_function_id = fr.dashboard_function_id " +
                    "WHERE df.content = ?1 AND fr.role_id = ?2 ")
    List<DashboardFunctionDTO> functionListByContent(String content, Long roleId);

    @Query(nativeQuery = true,
            value = "SELECT af.content as content " +
                    "FROM dashboard_function af " +
                    "JOIN function_role fr on af.dashboard_function_id = fr.dashboard_function_id " +
                    "WHERE fr.role_id = ?1 ")
    List<String> contentList(Long roleId);

    @Query(nativeQuery = true,
            value = "SELECT ur.role_id as roleID " +
                    "FROM user_role ur " +
                    "JOIN users u on ur.user_id = u.user_id " +
                    "WHERE u.user_id = ?1")
    Long getRoleByUserId(Long userId);

    @Query(nativeQuery = true,
            value ="SELECT df.dashboard_function_id AS dashboardFunctionId , df.function_code as functionCode, " +
                    "df.title as title, df.url as url , df.api as api,df.method as method, uf.body_config as bodyConfig," +
                    "uf.user_function_id as userFunctionId, uf.type as type " +
                    "FROM dashboard_function df " +
                    "join user_function uf on df.dashboard_function_id = uf.dashboard_function_id " +
                    "WHERE uf.user_id = ?1")
    List<DashboardFunctionDTO> getDashboardFunctionByUserID(Long userID);

    @Query(nativeQuery = true,
    value = "SELECT df.dashboard_function_id AS dashboardFunctionId , df.function_code as functionCode, " +
            "df.title as title, df.url as url , df.api as api,df.method as method, uf.body_config as bodyConfig, " +
            "uf.user_function_id as userFunctionId, uf.type as type " +
            "FROM dashboard_function df " +
            "join user_function uf on df.dashboard_function_id = uf.dashboard_function_id " +
            "WHERE uf.user_function_id = ?1 ")
    Optional<DashboardFunctionDTO> getSingleDashboardFunction(Long userFunctionId);
}
