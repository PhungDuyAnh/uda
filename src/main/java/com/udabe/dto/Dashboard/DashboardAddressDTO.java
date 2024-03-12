package com.udabe.dto.Dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardAddressDTO {

    private Long addressCodeId;

    private String addressName;

    private Long upperSeq;

    private double latitude;

    private double longitude;

    public DashboardAddressDTO(Long addressCodeId, String addressName, Long upperSeq, double latitude, double longitude) {
        this.addressCodeId = addressCodeId;
        this.addressName = addressName;
        this.upperSeq = upperSeq;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
