package com.udabe.dto.News;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NewsImageDTO {

    private Long newsImageId;

    private String imageNm;

    private String saveNm;

    private String groupId;

    private Long imageSize;

    private LocalDateTime saveDt;

    public NewsImageDTO(Long newsImageId, String imageNm, String saveNm, String groupId, Long imageSize, LocalDateTime saveDt) {
        this.newsImageId = newsImageId;
        this.imageNm = imageNm;
        this.saveNm = saveNm;
        this.groupId = groupId;
        this.imageSize = imageSize;
        this.saveDt = saveDt;
    }
}
