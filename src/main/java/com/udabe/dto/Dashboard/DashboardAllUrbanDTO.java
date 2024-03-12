package com.udabe.dto.Dashboard;

import com.udabe.service.impl.DashboardServiceImpl;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DashboardAllUrbanDTO {

    private Long userID;

    private String fullName;

    private Integer urbanStatus;

    private Double latitude;

    private Double longitude;

    private String districtAddress;

    private String provinceAddress;

    private Float sumScore;

    private Float sumPercent;

    public DashboardAllUrbanDTO(Long userID, String fullName, Integer urbanStatus) {
        this.userID = userID;
        this.fullName = fullName;
        this.urbanStatus = urbanStatus;
        this.latitude = DashboardServiceImpl.findLatitudeByUserId(userID);
        this.longitude = DashboardServiceImpl.findLongitudeByUserId(userID);
        this.districtAddress = UsersServiceImpl.findDistrictDashboard(userID);
        this.provinceAddress = UsersServiceImpl.findProvinceDashboard(userID);
        this.sumScore = DashboardServiceImpl.sumScoreFind(userID);
        this.sumPercent = DashboardServiceImpl.sumPercentFind(userID);
    }
}
