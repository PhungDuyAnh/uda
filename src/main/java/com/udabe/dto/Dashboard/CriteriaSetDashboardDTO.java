package com.udabe.dto.Dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CriteriaSetDashboardDTO {

    private Long criteriaSetId;

    private String criteriaSetCode;

    private String criteriaSetName;

    protected LocalDateTime appliedDate;

    public CriteriaSetDashboardDTO(Long criteriaSetId, String criteriaSetCode, String criteriaSetName, LocalDateTime appliedDate) {
        this.criteriaSetId = criteriaSetId;
        this.criteriaSetCode = criteriaSetCode;
        this.criteriaSetName = criteriaSetName;
        this.appliedDate = appliedDate;
    }
}
