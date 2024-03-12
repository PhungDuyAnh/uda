package com.udabe.dto.notifi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class NotifyVIDTO {

    private Long notifyId;

    private String notifyContentVI;

    private boolean status;

    private boolean disable;

    private String link;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long userId;

    private boolean tempDelete;

    public NotifyVIDTO(Long notifyId, String notifyContentVI, boolean status, boolean disable, String link, LocalDateTime createdAt, LocalDateTime updatedAt, Long userId, boolean tempDelete) {
        this.notifyId = notifyId;
        this.notifyContentVI = notifyContentVI;
        this.status = status;
        this.disable = disable;
        this.link = link;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.tempDelete = tempDelete;
    }

}
