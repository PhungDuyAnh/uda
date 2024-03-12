package com.udabe.dto.Dashboard;
import com.udabe.service.impl.DashboardServiceImpl;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTO {

    private Long userID;

    private String fullName;

    private Float sumScore;

    private Integer urbanStatus;

    private Float sumPercent;

    private double latitude;

    private double longitude;

    private String districtAddress;

    private String provinceAddress;

//    private List<DashboardAddressDTO> DashboardAddressDTOList;

    public DashboardDTO(Long userID, String fullName, Float sumScore, Integer urbanStatus, Float sumPercent) {
        this.userID = userID;
        this.fullName = fullName;
        this.sumScore = sumScore;
        this.urbanStatus = urbanStatus;
        this.sumPercent = sumPercent;
        this.latitude = DashboardServiceImpl.findLatitudeByUserId(userID);
        this.longitude = DashboardServiceImpl.findLongitudeByUserId(userID);
        this.districtAddress = UsersServiceImpl.findDistrictDashboard(userID);
        this.provinceAddress = UsersServiceImpl.findProvinceDashboard(userID);
//        this.DashboardAddressDTOList = UsersServiceImpl.findAddressUserDashboard(userID);
    }
}
