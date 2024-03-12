package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.dto.menu.AccessControlDTO;
import com.udabe.entity.AccessControl;
import com.udabe.entity.Menus;
import com.udabe.service.impl.AccessControlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${apiPrefix}/accesscontrol")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccessControlController extends BaseCrudController<AccessControl, Long> {

    private final AccessControlServiceImpl accessControlService;

    @Autowired
    public AccessControlController(AccessControlServiceImpl accessControlService) {
        this.accessControlService = accessControlService;
        super.setService(accessControlService);
    }

    /**
     * Controller lấy danh sách tất cả role của menu.
     *
     * @return Danh sách tất cả role của menu.
     */
    @GetMapping("/getAllRoleMenuList")
    public ResponseEntity<?> selectRoleMenuList(@RequestParam(defaultValue = "0", required = false) Long menuID
    ) {
        return accessControlService.getAllRoleMenuList(menuID);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAccessControl(@Valid @RequestBody AccessControlDTO accessControl) {
        return accessControlService.saveAccessControl(accessControl);
    }

    @PostMapping("/saveList")
    public ResponseEntity<?> saveListAccessControl(@Valid @RequestBody AccessControlDTO accessControl) {
        return accessControlService.saveListAccessControl(accessControl);
    }
}
