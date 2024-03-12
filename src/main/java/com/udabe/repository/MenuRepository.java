package com.udabe.repository;

import com.udabe.dto.menu.MenuDTO;
import com.udabe.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menus, Long> {

    /**
     * Repository lấy danh sách menu đang được hệ thống sử dụng theo upperSeq.
     *
     * @return Danh sách các menu đang được hệ thống sử dụng theo upperSeq.
     */
    @Query("SELECT NEW com.udabe.dto.menu.MenuDTO(" +
            "m.menuID, m.menuNameVi, m.menuLink, m.upperSeq, m.menuCode, m.issued, m.deleted, m.menuOrder, m.iconName" +
            ")" +
            "FROM Menus m " +
            "WHERE m.deleted = false and m.upperSeq =?1 ORDER BY m.menuOrder ASC")
    List<MenuDTO> findMenuListByUpperSeq(Long menuID);

    /**
     * Repository lấy danh sách tất cả thông tin menu.
     *
     * @return Danh sách tất cả thông tin của menu.
     */
    @Query(value = "Select r.role_name as roleName from access_control ac " +
            "join menus m on ac.menu_id = m.menu_id " +
            "join role r on r.role_id = ac.role_id " +
            "where ac.menu_id=?1", nativeQuery = true)
    ArrayList<String> findRoleNameById(Long menuID);

    /**
     * Repository Kiểm tra menu code đã tồn tại.
     */
    boolean existsByMenuCode(String menuCode);

    /**
     * Repository Kiểm tra đã tồn tại bằng menu ID.
     *
     * @param menuID
     */
    boolean existsByMenuID(Long menuID);


    /**
     * Repository Kiểm tra đã tồn tại bằng menu ID.
     *
     * @param upperSeq
     */
    boolean existsByUpperSeq(Long upperSeq);

    /**
     * Repository Kiểm tra sub menu còn được sử dụng.
     *
     * @param upperSeq
     */
    @Query(value = "Select COUNT(*)from menus m where m.upper_seq = ?1  and m.issued = true", nativeQuery = true)
    Long countSubMenuIssued(Long upperSeq);

    /**
     * Repository lấy thứ tự lớn nhất trong danh sách menu con
     *
     * @param upperSeq
     */
    @Query(value = "Select MAX(m.menu_order) from menus m where m.upper_seq = ?1", nativeQuery = true)
    Long maxMenuOrder(Long upperSeq);


    @Query("SELECT NEW com.udabe.dto.menu.MenuDTO(" +
            "m.menuID, m.menuNameVi, m.menuLink, m.upperSeq, m.menuCode, m.issued, m.deleted, m.menuOrder, m.iconName" +
            ")" +
            "FROM Menus m " +
            "WHERE m.deleted = false and m.menuCode =?1")
    MenuDTO getByMenuCode(String menuCode);

    @Query("SELECT NEW com.udabe.dto.menu.MenuDTO(" +
            "m.menuID, m.menuNameVi, m.menuLink, m.upperSeq, m.menuCode, m.issued, m.deleted, m.menuOrder, m.iconName" +
            ")" +
            "FROM Menus m " +
            "WHERE m.deleted = false and m.menuID =?1")
    MenuDTO findMenuByMenuID(Long menuID);

    @Query(value = "SELECT m.menu_id FROM menus m WHERE m.menu_code = ?1 AND m.deleted = false ", nativeQuery = true)
    Long checkMenuCodeDeleted(String menuCode);

    /**
     * Repository lấy danh sách menu đang được hệ thống sử dụng theo upperSeq.
     *
     * @return Danh sách các menu đang được hệ thống sử dụng theo upperSeq.
     */
    @Query("SELECT NEW com.udabe.dto.menu.MenuDTO(" +
            "m.menuID, m.menuNameVi, m.menuLink, m.upperSeq, m.menuCode, m.issued, m.deleted, m.menuOrder , m.iconName" +
            ")" +
            "FROM Menus m " +
            "WHERE m.deleted = false and m.upperSeq <>?1 ORDER BY m.menuOrder ASC")
    List<MenuDTO> findAllMenu(long menuID);
}
