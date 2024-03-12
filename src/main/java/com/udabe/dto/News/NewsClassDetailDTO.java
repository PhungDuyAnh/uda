package com.udabe.dto.News;

import com.udabe.service.impl.NewsServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsClassDetailDTO {

    private Long newsId;

    private String newsTitle;

    private Integer newsType;

    private String generalContent;

    private Long userID;

    private String fullName;

    private Long updatedBy;

    private String updateFullName;

    private String isDisplay;

    private String urlImage;

    private Long contentId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public NewsClassDetailDTO(Long newsId, String newsTitle, Integer newsType, String generalContent, Long userID,
                              String fullName, Long updatedBy, String isDisplay, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.newsType = newsType;
        this.generalContent = generalContent;
        this.userID = userID;
        this.fullName = fullName;
        this.updatedBy = updatedBy;
        this.isDisplay = isDisplay;
        this.updateFullName = NewsServiceImpl.getUpdateFullName(this.updatedBy);
        if(NewsServiceImpl.getNewImageId(this.newsId) != null) {
            this.urlImage = "/newsImage/displayImg/" + NewsServiceImpl.getNewImageId(this.newsId);
        }
        else{
            this.urlImage = null;
        }
        this.contentId = NewsServiceImpl.getContentId(this.newsId);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
