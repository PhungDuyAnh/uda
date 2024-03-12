package com.udabe.dto.EvaluationVersion;

public interface EvaluationVersionDTO {

    public Long getEvaluationVersionId();

    public String getVersionName();

    public String getStatus();

    public boolean getIsSend();

    public Long getUserId();

    public Long getCriteriaSetId();
}
