package com.udabe.service;

import org.springframework.http.ResponseEntity;

public interface DashboardFunctionService {

    ResponseEntity<?> dbFunctionList();

    ResponseEntity<?> getFunctionDashboard();

    ResponseEntity<?> getSingleDashboardFunction(Long dashboardFunctionId);
}
