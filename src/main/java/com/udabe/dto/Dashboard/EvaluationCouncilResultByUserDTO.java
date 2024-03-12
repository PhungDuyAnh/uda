package com.udabe.dto.Dashboard;

import java.time.LocalDateTime;

public interface EvaluationCouncilResultByUserDTO {

    public Long getEvaluationVersionId();

    public String getVersionName();

    public String getCriteriaVersion();

    public LocalDateTime getTimeReturn();

    public String getStatusEvaluate();

    public Float getPoint();

    public Long getCriteriaSetId();

    public String getFullName();
}
