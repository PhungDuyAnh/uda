package com.udabe.dto.Dashboard;

import java.time.LocalDateTime;

public interface EvaluationVersionWithResultDashboardDTO {

    public Long getEvaluationVersionId();

    public LocalDateTime getTimeReturn();

    public String getStatusRecognition();

    public String getCriteriaVersion();

    public String getSumScore();

    public String getVersionName();

}
