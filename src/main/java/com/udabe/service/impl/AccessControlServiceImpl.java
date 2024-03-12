package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.dto.menu.AccessControlDTO;
import com.udabe.dto.role.RoleDTO;
import com.udabe.entity.AccessControl;
import com.udabe.entity.Menus;
import com.udabe.entity.Role;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.AccessControlRepository;
import com.udabe.repository.RoleRepository;
import com.udabe.service.AccessControlService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessControlServiceImpl extends BaseCrudService<AccessControl, Long> implements AccessControlService {

    private final RoleRepository roleRepository;
    private final AccessControlRepository accessControlRepository;

    public AccessControlServiceImpl(RoleRepository roleRepository, AccessControlRepository accessControlRepository) {
        this.roleRepository = roleRepository;
        this.accessControlRepository = accessControlRepository;
        super.setRepository(accessControlRepository);
    }

    @Override
    public ResponseEntity<?> getAllRoleMenuList(Long menuID) {
        List<RoleDTO> result = accessControlRepository.findAllRoleMenu(menuID);
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> saveAccessControl(AccessControlDTO accessControlDTO) {
        Long count = accessControlRepository.countByMenuIDAndRolID(accessControlDTO.getMenuID(), accessControlDTO.getRoleID());
        if (count > 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role is already defined!"));
        }
        AccessControlDTO saveEntity = accessControlRepository.saveAccessControl(accessControlDTO.getMenuID(), accessControlDTO.getRoleID());
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> removeAccessControl(Long id) {
        if (!accessControlRepository.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Role does not exist"));
        }
        accessControlRepository.deleteById(id);
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> saveListAccessControl(AccessControlDTO accessControl) {
//        List<Long> menuOdersList = menus.getArrange();
//        AccessControlDTO saveEntity = accessControlRepository.saveAccessControl(accessControl.getMenuID(), accessControl.getRoleID());
        for (Long roleID : accessControl.getArrange()) {
            Long count = accessControlRepository.countByMenuIDAndRolID(accessControl.getMenuID(), roleID);
            if (!(count > 0)) {
                AccessControlDTO saveEntity = accessControlRepository.saveAccessControl(accessControl.getMenuID(), roleID);
            }
        }
        Long[] oldRoleID = accessControlRepository.findAllRoleIDByMenuID(accessControl.getMenuID());
        for (Long oRole : oldRoleID) {
            if(!accessControl.getArrange().contains(oRole)){
                accessControlRepository.deleteByMenuIDandRoleID(accessControl.getMenuID(),oRole);
            }
        }
        return ResponseEntity.ok(new Response().setMessage("Successfully!"));
    }

}
