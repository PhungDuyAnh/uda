package com.udabe.dto.Dashboard;

import com.udabe.service.impl.DashboardServiceImpl;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTONoEvaluation {

    private Long userID;

    private String fullName;

    private Integer urbanStatus;

    private double latitude;

    private double longitude;

    private String districtAddress;

    private String provinceAddress;


//    private List<DashboardAddressDTO> DashboardAddressDTOList;

    public DashboardDTONoEvaluation(Long userID, String fullName, Integer urbanStatus) {
        this.userID = userID;
        this.fullName = fullName;
        this.urbanStatus = urbanStatus;
        this.latitude = DashboardServiceImpl.findLatitudeByUserId(userID);
        this.longitude = DashboardServiceImpl.findLongitudeByUserId(userID);
        this.districtAddress = UsersServiceImpl.findDistrictDashboard(userID);
        this.provinceAddress = UsersServiceImpl.findProvinceDashboard(userID);
//        this.DashboardAddressDTOList = UsersServiceImpl.findAddressUserDashboard(userID);
    }
}
