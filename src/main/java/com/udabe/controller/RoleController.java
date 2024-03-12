package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.Role;
import com.udabe.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiPrefix}/role")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController extends BaseCrudController<Role, Long> {

    private final RoleServiceImpl roleService;

    @Autowired
    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
        super.setService(roleService);
    }

    /**
     * Controller lấy tất cả danh sách Role
     */
    @GetMapping("/getAllRole")
    public ResponseEntity<?> selectListRole() {
        return roleService.getAllRole();
    }
}
