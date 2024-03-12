package com.udabe.dto.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CriteriaReceiveDTO {

    private Long evaluationVersionId;

    private String versionName;

    private String criteriaVersion;

    private LocalDateTime createdAt;

    protected LocalDateTime timeReturn;

    private String statusEvaluate;

    private String statusRecognition;

    private String commentRecognition;

    /**
     * ID bộ chỉ số.
     */
    private Long criteriaSetId;

    private Long userIdReceive;

    private Long userId;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String fullName;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String userName;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(Chức danh, chức vụ trong tổ chức).
     */
    private String position;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(thuộc tổ chức nào?).
     */
    private String organization;

    private Float point;

    public CriteriaReceiveDTO(Long evaluationVersionId, String versionName, String criteriaVersion, LocalDateTime createdAt, LocalDateTime timeReturn, String statusEvaluate, String statusRecognition) {
        this.evaluationVersionId = evaluationVersionId;
        this.versionName = versionName;
        this.criteriaVersion = criteriaVersion;
        this.createdAt = createdAt;
        this.timeReturn = timeReturn;
        this.statusEvaluate = statusEvaluate;
        this.statusRecognition = statusRecognition;
    }

    public CriteriaReceiveDTO(Long evaluationVersionId, String versionName, String criteriaVersion, LocalDateTime createdAt, LocalDateTime timeReturn, String statusEvaluate, String statusRecognition, String commentRecognition, Long criteriaSetId, Long userIdReceive, Long userId, String fullName, String userName, String position, String organization, Float point) {
        this.evaluationVersionId = evaluationVersionId;
        this.versionName = versionName;
        this.criteriaVersion = criteriaVersion;
        this.createdAt = createdAt;
        this.timeReturn = timeReturn;
        this.statusEvaluate = statusEvaluate;
        this.statusRecognition = statusRecognition;
        this.commentRecognition = commentRecognition;
        this.criteriaSetId = criteriaSetId;
        this.userIdReceive = userIdReceive;
        this.userId = userId;
        this.fullName = fullName;
        this.userName = userName;
        this.position = position;
        this.organization = organization;
        this.point = point;
    }
}
