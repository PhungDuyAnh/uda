package com.udabe.dto.News;

import com.udabe.service.impl.MainContentServiceImpl;
import com.udabe.service.impl.NewsServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsClassDTO {

    private Long newsId;

    private String newsTitle;

    private Integer newsType;

    private String generalContent;

    private String isDisplay;

    private Long userID;

    private String fullName;

    private String urlImage;

    private Long contentId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public NewsClassDTO(Long newsId, String newsTitle, Integer newsType, String generalContent, String isDisplay,
                        Long userID, String fullName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.newsType = newsType;
        this.generalContent = generalContent;
        this.isDisplay = isDisplay;
        this.userID = userID;
        this.fullName = fullName;
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
