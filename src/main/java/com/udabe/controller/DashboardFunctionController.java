package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.DashboardFunction;
import com.udabe.service.impl.DashboardFunctionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/dashboardFunction")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardFunctionController extends BaseCrudController<DashboardFunction, Long> {

    private final DashboardFunctionServiceImpl dashboardFunctionServiceImpl;

    public DashboardFunctionController(DashboardFunctionServiceImpl dashboardFunctionServiceImpl) {
        this.dashboardFunctionServiceImpl = dashboardFunctionServiceImpl;
    }

    @GetMapping("/dbFunctionList")
    public ResponseEntity<?> dbFunctionList() {
        return dashboardFunctionServiceImpl.dbFunctionList();
    }

    @GetMapping("/getFunctionDashboard")
    public ResponseEntity<?> getFunctionDashboard(){

        return dashboardFunctionServiceImpl.getFunctionDashboard();
    }

    @GetMapping("/getSingleDashboardFunction/{userFunctionId}")
    public ResponseEntity<?> getSingleDashboardFunction(@PathVariable("userFunctionId") Long userFunctionId){

        return dashboardFunctionServiceImpl.getSingleDashboardFunction(userFunctionId);
    }

}
