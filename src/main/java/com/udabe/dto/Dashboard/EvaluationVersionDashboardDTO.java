package com.udabe.dto.Dashboard;

import java.time.LocalDateTime;

public interface EvaluationVersionDashboardDTO {

    public Long getEvaluationVersionId();

    public String getVersionName();

//    public LocalDateTime getTimeReturn();

    public LocalDateTime getUpdatedAt();

    public Float getSumScore();

    public Long getCriteriaSetId();
}
