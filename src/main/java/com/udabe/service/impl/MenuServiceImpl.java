package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.dto.menu.MenuDTO;
import com.udabe.entity.Menus;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.AccessControlRepository;
import com.udabe.repository.MenuRepository;
import com.udabe.repository.RoleRepository;
import com.udabe.service.MenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class MenuServiceImpl extends BaseCrudService<Menus, Long> implements MenuService {

    private final ModelMapper modelMapper;

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;
    private final AccessControlRepository accessControlRepository;

    @Autowired
    public MenuServiceImpl(ModelMapper modelMapper, MenuRepository menuRepository, RoleRepository roleRepository, AccessControlRepository accessControlRepository) {
        this.modelMapper = modelMapper;
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
        this.accessControlRepository = accessControlRepository;
        super.setRepository(menuRepository);
    }

    /**
     * Service lấy danh sách menu kèm theo role name đang được hệ thống sử dụng hiện thị trên sidebar.
     *
     * @return Danh sách các menu kèm theo role name của menu.
     */
    @Override
    public ResponseEntity<?> getAllMenuAndRole() {
//        List<UserDTO> result;

        //get primary menu list with upperSeq =1L
        List<MenuDTO> result = menuRepository.findMenuListByUpperSeq(1L);

        for (MenuDTO item : result) {
            //get role name for each primary menu
            ArrayList<String> roleNameList = menuRepository.findRoleNameById(item.getMenuID());
            item.setRoleName(roleNameList);
            //get secondary menu list
            List<MenuDTO> subMenus = menuRepository.findMenuListByUpperSeq(item.getMenuID());
            if (subMenus.size() != 0) {
                for (MenuDTO s : subMenus) {
                    s.setRoleName(menuRepository.findRoleNameById(s.getMenuID()));
                }
            }
            item.setSubMenu(subMenus);
        }
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

    /**
     * Service lấy danh sách tất cả thông tin menu.
     *
     * @return Danh sách tất cả thông tin của menu.
     */
    @Override
    public ResponseEntity<?> getAllMenu() {
        List<MenuDTO> result = menuRepository.findMenuListByUpperSeq(1L);
        for (MenuDTO m : result) {
            List<MenuDTO> subMenus = menuRepository.findMenuListByUpperSeq(m.getMenuID());
            m.setSubMenu(subMenus);
        }
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

    /**
     * Service vô hiệu hóa menu.
     */
    @Override
    public ResponseEntity<?> disableMenu(Long menuID) {
        if (menuRepository.existsByMenuID(menuID)) {
            Long count = menuRepository.countSubMenuIssued(menuID);
            if (count > 0) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Đã xảy ra lỗi!"));
            }
            Menus menu = menuRepository.findById(menuID).get();
            menu.setIssued(false);
            Menus saveEntity = menuRepository.save(menu);
            return ResponseEntity.ok(new Response().setMessage("Successfully!"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Lỗi: Đã xảy ra lỗi!"));
    }

    /**
     * Service kích hoạt  menu.
     */
    @Override
    public ResponseEntity<?> enableMenu(Long menuID) {
        if (menuRepository.existsByMenuID(menuID)) {
            Menus menu = menuRepository.findById(menuID).get();
            menu.setIssued(true);
            Menus saveEntity = menuRepository.save(menu);
            return ResponseEntity.ok(new Response().setMessage("Successfully!"));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Failed!"));
    }

    /**
     * Service lưu thông tin menu.
     */
    @Override
    public ResponseEntity<?> saveMenu(Menus menu) {
        Long menuIDs = menuRepository.checkMenuCodeDeleted(menu.getMenuCode());

        if (menuIDs != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: Mã menu đã được sử dụng!"));
        }
        Long maxMenuOrder = menuRepository.maxMenuOrder(menu.getUpperSeq());
        maxMenuOrder = (maxMenuOrder == null) ? 1 : maxMenuOrder++;
        menu.setMenuOrder(maxMenuOrder);
        Menus saveEntity = menuRepository.save(menu);
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

    /**
     * Service cập nhật thông tin  menu.
     */
    @Override
    public ResponseEntity<?> updateMenu(Menus menu, Long menuID) {
        if (!menuRepository.existsByMenuID(menuID)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: Không tìm thấy Menu!"));
        }
        if (!menu.isIssued()) {
            Long count = menuRepository.countSubMenuIssued(menuID);
            if (count > 0) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Đã có lỗi xảy ra"));
            }
        }
        Long menuIDd = menuRepository.checkMenuCodeDeleted(menu.getMenuCode());

        if (menuIDd != null) {
            if(!menuIDd.equals(menuID)){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Mã menu đã được sử dụng!"));
            }
        }
        Menus saveEntity = menuRepository.save(menu);
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

    /**
     * Service sắp xếp menu con theo menu cha.
     */
    @Override
    public ResponseEntity<?> updateMenuOrder(Menus menus) {
        List<Long> menuOdersList = menus.getArrange();
        for (int i = 0; i < menus.getArrange().size(); i++) {
            Menus menus1 = menuRepository.findById(menuOdersList.get(i)).get();
            menus1.setMenuOrder((long) i + 1);
            menuRepository.save(menus1);
        }
        return ResponseEntity.ok("Update position successfully");
    }

    /**
     * Service Xóa menu
     */
    @Override
    public ResponseEntity<?> removeMenuByID(Long menuID) {
        List<MenuDTO> subMenu = menuRepository.findMenuListByUpperSeq(menuID);
        if (subMenu.size() > 0) {
            for (MenuDTO item : subMenu) {
                Menus menu = menuRepository.findById(item.getMenuID()).get();
                menu.setDeleted(true);
                Menus menuSave = menuRepository.save(menu);
            }
        }
        Menus menu = menuRepository.findById(menuID).get();
        menu.setDeleted(true);
        Menus saveEntity = menuRepository.save(menu);
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getMenuByMenuID(Long menuID) {

        MenuDTO menuDTO = menuRepository.findMenuByMenuID(menuID);
        return ResponseEntity.ok(new Response().setData(menuDTO).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getMenuByUpperSeq(Long upperSeq) {
        List<MenuDTO> menuDTOList = menuRepository.findMenuListByUpperSeq(upperSeq);
        return ResponseEntity.ok(new Response().setData(menuDTOList).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getMenuRoleNameAndPath() {
        List<MenuDTO> result = menuRepository.findAllMenu(0L);
        for (MenuDTO item : result) {
            //get role name for each primary menu
            ArrayList<String> roleNameList = menuRepository.findRoleNameById(item.getMenuID());
            item.setRoleName(roleNameList);
        }
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

}