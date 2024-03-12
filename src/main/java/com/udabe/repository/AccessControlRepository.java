package com.udabe.repository;

import com.udabe.dto.menu.AccessControlDTO;
import com.udabe.dto.role.RoleDTO;
import com.udabe.entity.AccessControl;
import com.udabe.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessControlRepository extends JpaRepository<AccessControl, Long> {

    /**
     * Repository lấy danh sách role của menu.
     *
     * @param menuID
     */
    @Query(value = "Select r.role_id as roleID, r.role_name as roleName " +
            "from role r join access_control ac on r.role_id = ac.role_id " +
            "where ac.menu_id = ?1",
            nativeQuery = true)
    List<RoleDTO> findAllRoleMenu(Long menuID);

    /**
     * Repository kiểm tra menu đã có role id chưa
     *
     * @param menuID ,roleID
     */
    @Query(value = "Select COUNT(*) from access_control ac where ac.menu_id = ?1  and ac.role_id = ?2", nativeQuery = true)
    Long countByMenuIDAndRolID(Long menuID, Long roleID);

    /**
     * Repository Thêm role cho menu.
     *
     * @param menuID, roleID
     */
    @Query(value = "insert into access_control(menu_id, role_id) VALUE (?1, ?2)", nativeQuery = true)
    AccessControlDTO saveAccessControl(Long menuID, Long roleID);

    /**
     * Repository lấy danh sách role của menu.
     *
     * @param menuID
     */
    @Query(value = "Select ac.role_id from access_control ac where ac.menu_id = ?1", nativeQuery = true)
    Long[] findAllRoleIDByMenuID(Long menuID);

    /**
     * Repository Xóa role của menu.
     *
     * @param menuID
     */
    @Query(value = "delete from  access_control  where menu_id=?1 and role_id=?2", nativeQuery = true)
    void deleteByMenuIDandRoleID(Long menuID, Long oRole);
}
