package com.udabe.repository;

import com.udabe.entity.ERole;
import com.udabe.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(ERole name);

    /**
     * Repository lấy danh sách role.
     */
    @Query("SELECT NEW com.udabe.entity.Role(" +
            "r.roleID, r.roleName" +
            ")" +
            "FROM Role r " +
            "ORDER BY r.roleID ASC")
    List<Role> findAllRole();
}
