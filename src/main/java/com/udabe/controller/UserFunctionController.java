package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.dto.Dashboard.UserFunctionDashboardDTO;
import com.udabe.dto.UserFunction.UserFunctionDTO;
import com.udabe.entity.UserFunction;
import com.udabe.service.impl.UserFunctionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/userFunction")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserFunctionController extends BaseCrudController<UserFunction, Long> {

    private final UserFunctionServiceImpl userFunctionServiceImpl;

    public UserFunctionController(UserFunctionServiceImpl userFunctionServiceImpl) {
        this.userFunctionServiceImpl = userFunctionServiceImpl;
    }

    @PostMapping("/addUserFunction")
    public ResponseEntity<?> addUserFunction(@RequestBody Long[] DashboardFunctionId){

        return userFunctionServiceImpl.addUserFunction(DashboardFunctionId);
    }

    @DeleteMapping("/removeUserFunction")
    public ResponseEntity<?> removeUserFunction(@RequestParam(required = true) Long dashboardFunctionId){

        return userFunctionServiceImpl.deleteUserFunction(dashboardFunctionId);
    }

    @GetMapping("/getUserFunctionData")
    public ResponseEntity<?> getUserFunctionData(){
        return userFunctionServiceImpl.getUserFunctionData();
    }

    @PostMapping("/saveBodyConfig")
    public ResponseEntity<?> saveBodyConfig(@RequestBody UserFunctionDashboardDTO userFunctionDTO){
        return userFunctionServiceImpl.saveBodyConfig(userFunctionDTO);
    }

    @PutMapping("/updateType/{userFunctionId}")
    public ResponseEntity<?> updateType(@PathVariable("userFunctionId") Long userFunctionId, @RequestBody UserFunction userFunction){

        return userFunctionServiceImpl.updateType(userFunctionId, userFunction);
    }

}
