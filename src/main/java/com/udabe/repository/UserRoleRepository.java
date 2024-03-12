package com.udabe.repository;

import com.udabe.entity.UserRole;
import com.udabe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByUser(Users users);

    @Query(value = "select u from UserRole u where u.user.userID = ?1 group by u.role")
    List<UserRole> getRoles(Long userId);

}
