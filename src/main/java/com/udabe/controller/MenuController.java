package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.Menus;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.MenuRepository;
import com.udabe.service.impl.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${apiPrefix}/menu")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MenuController extends BaseCrudController<Menus, Long> {

    private final MenuServiceImpl menuService;
    private final MenuRepository menuRepository;

    @Autowired
    public MenuController(MenuServiceImpl menuService, MenuRepository menuRepository) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        super.setService(menuService);
    }

    /**
     * Controller lấy danh sách menu kèm theo role name đang được hệ thống sử dụng hiện thị trên sidebar.
     *
     * @return Danh sách các menu kèm theo role name của menu.
     */
    @GetMapping("/getAllMenu")
    public ResponseEntity<?> selectListMenuAndRole() {
        return menuService.getAllMenuAndRole();
    }

    /**
     * Controller lấy danh sách tất cả thông tin menu.
     *
     * @return Danh sách tất cả thông tin của menu.
     */
    @GetMapping("/getAllMenuMnt")
    public ResponseEntity<?> selectListMenu() {
        return menuService.getAllMenu();
    }

    /**
     * Controller thêm mới menu.
     */
    @PostMapping("/save")
    public ResponseEntity<?> insertMenu(@Valid @RequestBody Menus menu) {
        return menuService.saveMenu(menu);
    }

    /**
     * Controller cập nhật thông tin của menu.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateMenu(@Valid @RequestBody Menus menu) {
        return menuService.updateMenu(menu, menu.getMenuID());
    }

    /**
     * Controller thực hiện vô hiệu hóa menu.
     */
    @PutMapping("/disable")
    public ResponseEntity<?> disableMenu(@RequestParam(required = true) Long menuID) {
        return menuService.disableMenu(menuID);
    }

    /**
     * Controller thực hiện kích hoạt menu.
     */
    @PutMapping("/enable")
    public ResponseEntity<?> enableMenu(@RequestParam(required = true) Long menuID) {
        return menuService.enableMenu(menuID);
    }

    /**
     * Controller sắp xếp thứ tự menu con theo menu cha
     */
    @PostMapping("/updateMenuOrder")
    public ResponseEntity<?> updateMenuOrder(@RequestBody Menus menus) {
        return menuService.updateMenuOrder(menus);
    }

    /**
     * Controller cập nhật trạng thái đã xóa khỏi hệ thống của menu
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeMenu(@RequestParam(required = true) Long menuID) {
        return menuService.removeMenuByID(menuID);
    }

    @GetMapping("/getMenu")
    public ResponseEntity<?> getMenuByMenuID(@RequestParam(required = true) Long menuID) {
        return menuService.getMenuByMenuID(menuID);
    }

    @GetMapping("/getSubMenu")
    public ResponseEntity<?> getSubMenuByMenuID(@RequestParam(required = true) Long upperSeq) {
        return menuService.getMenuByUpperSeq(upperSeq);
    }

    @GetMapping("/getRoleNamePath")
    public ResponseEntity<?> getMenuRoleNameAndPath() {
        return menuService.getMenuRoleNameAndPath();
    }
}
