package com.udabe.dto.Dashboard;

import java.time.LocalDateTime;

public interface EvaluationVerNewestDTO {

    public Long getEvaluationVersionId();

    public String getVersionName();

    public String getCriteriaVersion();

    public LocalDateTime getTimeReturn();

    public String getStatusEvaluate();

    public String getStatusRecognition();

//    public String getFullName();

    public Long getCriteriaSetId();
}
