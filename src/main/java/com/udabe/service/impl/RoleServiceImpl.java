package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.entity.Menus;
import com.udabe.entity.Role;
import com.udabe.repository.RoleRepository;
import com.udabe.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class RoleServiceImpl extends BaseCrudService<Role, Long> implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        super.setRepository(roleRepository);
    }

    @Override
    public ResponseEntity<?> getAllRole() {
        List<Role> result = roleRepository.findAllRole();
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }
}
