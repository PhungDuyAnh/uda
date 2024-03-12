package com.udabe.service;

import com.udabe.dto.Dashboard.CriteriaClassRequest;
import org.springframework.http.ResponseEntity;

public interface DashboardService {

    ResponseEntity<?> getUrbanInforAndPosition(String fullName);

    ResponseEntity<?> getAllProvinceDashboard();

    ResponseEntity<?> userListDashboard();

    ResponseEntity<?> criteriasetDashboard();

    ResponseEntity<?> newestEvaluationRegisterUser();

    ResponseEntity<?> councilDashboardList();

    ResponseEntity<?> evaluationVersionDashboard();

    ResponseEntity<?> evaluationVersionWithResultDashboard();

    ResponseEntity<?> evaluationCouncilResult();

    ResponseEntity<?> evaluationVerNewest();

    ResponseEntity<?> evaluationVerCouncil3();

    ResponseEntity<?> scoreAndPercent();

    ResponseEntity<?> criteriaStatistics(CriteriaClassRequest classRequest);

    ResponseEntity<?> evaluationCouncilResultByUser();
}
