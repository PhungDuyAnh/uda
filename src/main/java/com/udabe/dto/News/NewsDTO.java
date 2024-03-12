package com.udabe.dto.News;

import java.time.LocalDateTime;

public interface NewsDTO {

    public Long getNewsId();

    public String getNewsTitle();

    public Integer getNewsType();

    public String getGeneralContent();

    public Long getUserId();

    public String getUserName();

    public String getFullName();

    public String isDisplay();

    public LocalDateTime getCreatedAt();

    public LocalDateTime getUpdatedAt();
}
