package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.dto.Dashboard.*;
import com.udabe.entity.DashboardFunction;
import com.udabe.repository.DashboardFunctionRepository;
import com.udabe.repository.DashboardRepository;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.DashboardFunctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardFunctionServiceImpl extends BaseCrudService<DashboardFunction,Long> implements DashboardFunctionService {

    private final DashboardFunctionRepository dashboardFunctionRepository;

    private final DashboardRepository dashboardRepository;

    private final UsersServiceImpl usersService;

    public DashboardFunctionServiceImpl(DashboardFunctionRepository dashboardFunctionRepository, DashboardRepository dashboardRepository, UsersServiceImpl usersService) {
        this.dashboardFunctionRepository = dashboardFunctionRepository;
        this.dashboardRepository = dashboardRepository;
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<?> dbFunctionList(){
        List<DashboardFunctionDTO> tempList = new ArrayList<>();
        Map<Object, Object> mapResult = new HashMap<>();
        Long userID = getUserLoginId();
        Long roleFind = dashboardFunctionRepository.getRoleByUserId(userID);
        List<String> contentList = dashboardFunctionRepository.contentList(roleFind);
        for(String i : contentList){
            List<DashboardFunctionDTO> functionList = dashboardFunctionRepository.functionListByContent(i, roleFind);
            tempList.addAll(functionList);
            mapResult.put(i,functionList);
        }
        return ResponseEntity.ok(mapResult);
    }


    @Override
    public ResponseEntity<?> getFunctionDashboard() {
        Long userID = getUserLoginId();
        List<DashboardFunctionDTO> functionDTOList =  dashboardFunctionRepository.getDashboardFunctionByUserID(userID);

        return ResponseEntity.ok(new Response().setDataList(functionDTOList).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getSingleDashboardFunction(Long userFunctionId) {
        DashboardFunctionDTO data = dashboardFunctionRepository.getSingleDashboardFunction(userFunctionId).get();
        return ResponseEntity.ok(data);
    }


    public static Long getUserLoginId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

}
