package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.dto.Dashboard.CriteriaClassRequest;
import com.udabe.dto.Dashboard.DashboardAllUrbanDTO;
import com.udabe.dto.Dashboard.DashboardDTO;
import com.udabe.entity.Users;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.AddressCodeRepository;
import com.udabe.repository.DashboardRepository;
import com.udabe.repository.EvaluationVersionRepository;
import com.udabe.repository.UsersRepository;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl extends BaseCrudService<Users, Long> implements DashboardService {

    private static EvaluationVersionRepository evaluationVersionRepository;

    private final UsersRepository usersRepository;

    private static DashboardRepository dashboardRepository;

    private static AddressCodeRepository addressCodeRepository;

    private static StatisticalServiceImpl statisticalService;

    public DashboardServiceImpl(EvaluationVersionRepository evaluationVersionRepository, UsersRepository usersRepository, DashboardRepository DashboardRepository, AddressCodeRepository addressCodeRepository, StatisticalServiceImpl statisticalService) {
        this.evaluationVersionRepository = evaluationVersionRepository;
        this.usersRepository = usersRepository;
        this.dashboardRepository = DashboardRepository;
        this.addressCodeRepository = addressCodeRepository;
        this.statisticalService = statisticalService;
    }

    @Override
    public ResponseEntity<?> getUrbanInforAndPosition(String fullName) {

        List<DashboardDTO> list1 = dashboardRepository.getUrban(fullName);
        if (list1.isEmpty()) {
            return ResponseEntity.ok(dashboardRepository.getUrban2(fullName));
        } else {
            return ResponseEntity.ok(list1);
        }
    }


    @Override
    public ResponseEntity<?> getAllProvinceDashboard() {

        List<DashboardAllUrbanDTO> DashboardDTOS = dashboardRepository.findAllProvince();
        if (DashboardDTOS.isEmpty()) {
            return ResponseEntity.ok(dashboardRepository.getUrban3());
        }
        return ResponseEntity.ok(DashboardDTOS);
    }


    public static Double findLatitudeByUserId(Long userId) {
        return dashboardRepository.findLatitudeByUserId(userId);
    }


    public static Double findLongitudeByUserId(Long userId) {
        return dashboardRepository.findlongitudeByUserId(userId);
    }


    @Override
    public ResponseEntity<?> userListDashboard() {

        return ResponseEntity.ok(dashboardRepository.selectAllUsersLimitByDashboard(PageRequest.of(0, 5)));
    }


    @Override
    public ResponseEntity<?> criteriasetDashboard() {

        return ResponseEntity.ok(dashboardRepository.getCriteriaDashboard());
    }


    @Override
    public ResponseEntity<?> newestEvaluationRegisterUser() {

        return ResponseEntity.ok(dashboardRepository.listNewestEvaluationRegisterUser());
    }


    @Override
    public ResponseEntity<?> councilDashboardList() {

        return ResponseEntity.ok(dashboardRepository.councilDashboardList());
    }


    @Override
    public ResponseEntity<?> evaluationVersionDashboard() {
        Long userId = getUserLoginId();

        return ResponseEntity.ok(dashboardRepository.evaluationVersionDashboard(userId));
    }


    @Override
    public ResponseEntity<?> evaluationVersionWithResultDashboard() {

        return ResponseEntity.ok(dashboardRepository.evaluationVersionWithResultDashboard(getUserLoginId()));
    }


    @Override
    public ResponseEntity<?> evaluationCouncilResult() {

        return ResponseEntity.ok(dashboardRepository.evaluationCouncilResult());
    }


    @Override
    public ResponseEntity<?> evaluationVerNewest() {

        return ResponseEntity.ok(dashboardRepository.evaluationVerNewest());
    }


    @Override
    public ResponseEntity<?> evaluationVerCouncil3() {

        Long userID = getUserLoginId();
        return ResponseEntity.ok(dashboardRepository.evaluationVerCouncil3(userID));
    }


    @Override
    public ResponseEntity<?> scoreAndPercent() {

        return ResponseEntity.ok(dashboardRepository.scoreAndPercent(getUserLoginId()));
    }


    public static Float sumScoreFind(Long userID) {

        return dashboardRepository.sumScoreFind(userID, PageRequest.of(0, 1));
    }


    public static Float sumPercentFind(Long userID) {

        return dashboardRepository.sumPercentFind(userID, PageRequest.of(0, 1));
    }


    public static Long getUserLoginId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }


    @Override
    public ResponseEntity<?> criteriaStatistics(CriteriaClassRequest classRequest) {
        ResponseEntity<?> entity = null;
        if (classRequest.getClass1Id() != null) {
            entity = statisticalService.getStaCriteriaClass1(classRequest.getClass1Id(), classRequest.getYear());
        }
        if (classRequest.getClass2Id() != null) {
            entity = statisticalService.getStaCriteriaClass2(classRequest.getClass2Id(), classRequest.getYear());
        }
        if (classRequest.getClass3Id() != null) {
            entity = statisticalService.getStaCriteriaClass3(classRequest.getClass3Id(), classRequest.getYear());
        }
        if (classRequest.getClassDetailId() != null) {
            entity = statisticalService.getStaCriteriaDetail(classRequest.getClassDetailId(), classRequest.getYear());
        }
        return entity;
    }


    @Override
    public ResponseEntity<?> evaluationCouncilResultByUser() {
        Long userId = getUserLoginId();
        return ResponseEntity.ok(dashboardRepository.evaluationCouncilResultByUser(userId));
    }
}
