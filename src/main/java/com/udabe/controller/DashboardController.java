package com.udabe.controller;


import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.dto.Dashboard.CriteriaClassRequest;
import com.udabe.entity.Users;
import com.udabe.service.impl.DashboardServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController extends BaseCrudController<Users, Long> {

    private final DashboardServiceImpl dashboardServiceImpl;

    public DashboardController(DashboardServiceImpl dashboardServiceImpl) {
        this.dashboardServiceImpl = dashboardServiceImpl;
    }

    @GetMapping("/getUrbanInfo")
    public ResponseEntity<?> getUrbanInforAndPosition(@RequestParam(required = false) String fullName){
        if(fullName == null || fullName.isEmpty()){
            fullName = "";
            return dashboardServiceImpl.getAllProvinceDashboard();
        }
        else {
            return dashboardServiceImpl.getUrbanInforAndPosition(fullName);
        }
    }

    @GetMapping("/getAllProvinceDashboard")
    public ResponseEntity<?> getAllProvince(){
        return dashboardServiceImpl.getAllProvinceDashboard();
    }

    @GetMapping("/userListDashboard")
    public ResponseEntity<?> userListDashboard(){

        return dashboardServiceImpl.userListDashboard();
    }

    @GetMapping("/criteriasetDashboard")
    public ResponseEntity<?> criteriasetDashboard(){

        return dashboardServiceImpl.criteriasetDashboard();
    }

    @GetMapping("/newestEvaluationRegisterUser")
    public ResponseEntity<?> newestEvaluationRegisterUser(){

        return dashboardServiceImpl.newestEvaluationRegisterUser();
    }

    @GetMapping("/councilDashboardList")
    public ResponseEntity<?> councilDashboardList(){

        return dashboardServiceImpl.councilDashboardList();
    }

    @GetMapping("/evaluationVersionDashboard")
    public ResponseEntity<?> evaluationVersionDashboard(){
        return dashboardServiceImpl.evaluationVersionDashboard();
    }

    @GetMapping("/evaluationResultDashboard")
    public ResponseEntity<?> evaluationVersionWithResultDashboard(){
        return dashboardServiceImpl.evaluationVersionWithResultDashboard();
    }

    @GetMapping("/evaluationCouncilResult")
    public ResponseEntity<?> evaluationCouncilResult(){
        return dashboardServiceImpl.evaluationCouncilResult();
    }

    @GetMapping("/evaluationCouncilResultByUser")
    public ResponseEntity<?> evaluationCouncilResultByUser(){
        return dashboardServiceImpl.evaluationCouncilResultByUser();
    }

    @GetMapping("/evaluationVerNewest")
    public ResponseEntity<?> evaluationVerNewest(){
        return dashboardServiceImpl.evaluationVerNewest();
    }

    @GetMapping("/evaluationVerCouncil")
    public ResponseEntity<?> evaluationVerCouncil3(){
        return dashboardServiceImpl.evaluationVerCouncil3();
    }

    @GetMapping("/scoreAndPercent")
    public ResponseEntity<?> scoreAndPercent(){
        return dashboardServiceImpl.scoreAndPercent();
    }

    @PostMapping("/criteriaStatistics")
    public ResponseEntity<?> criteriaStatistics(@RequestBody CriteriaClassRequest classRequest){
        return dashboardServiceImpl.criteriaStatistics(classRequest);
    } 
}
