package com.udabe.dto.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CriteriaReceiveDTO2 {

    private Long evaluationVersionId;

    private String versionName;

    private String criteriaVersion;

    private LocalDateTime createdAt;

    protected LocalDateTime timeReturn;

    private String statusEvaluate;

    private String statusRecognition;

    public CriteriaReceiveDTO2(Long evaluationVersionId, String versionName, String criteriaVersion, LocalDateTime createdAt, LocalDateTime timeReturn, String statusEvaluate, String statusRecognition) {
        this.evaluationVersionId = evaluationVersionId;
        this.versionName = versionName;
        this.criteriaVersion = criteriaVersion;
        this.createdAt = createdAt;
        this.timeReturn = timeReturn;
        this.statusEvaluate = statusEvaluate;
        this.statusRecognition = statusRecognition;
    }
}
