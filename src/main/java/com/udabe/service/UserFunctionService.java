package com.udabe.service;

import com.udabe.dto.Dashboard.UserFunctionDashboardDTO;
import com.udabe.entity.UserFunction;
import org.springframework.http.ResponseEntity;

public interface UserFunctionService {

    ResponseEntity<?> addUserFunction(Long[] DashboardFunctionId);

    ResponseEntity<?> deleteUserFunction(Long dashboardFunctionId);

    ResponseEntity<?> getUserFunctionData();

    ResponseEntity<?> saveBodyConfig(UserFunctionDashboardDTO userFunctionDTO);

    ResponseEntity<?> updateType(Long userFunctionId, UserFunction userFunction);
}
