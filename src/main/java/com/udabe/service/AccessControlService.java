package com.udabe.service;

import com.udabe.dto.menu.AccessControlDTO;
import com.udabe.entity.AccessControl;
import org.springframework.http.ResponseEntity;

public interface AccessControlService {
    ResponseEntity<?> getAllRoleMenuList(Long menuID);

    ResponseEntity<?> saveAccessControl(AccessControlDTO accessControl);

    ResponseEntity<?> removeAccessControl(Long id);

    ResponseEntity<?> saveListAccessControl(AccessControlDTO accessControl);
}
