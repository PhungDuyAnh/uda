package com.udabe.dto.user;

import com.udabe.service.impl.CriteriaSetServiceImpl;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UrbanCouncilDTO {

    /**
     * ID tài khoản.
     */
    private Long userID;


    private Long userIdReceive;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String fullName;

    /**
     * Trạng thái khu đô thị.
     */
    private Integer urbanStatus;

    /**
     * Trạng thái đánh giá khu đô thị của hội đồng.
     * N  : Hội đồng đã đánh giá.
     * Y  : Hội đồng chưa đánh giá.
     */
    private String statusCouncil;

    /**
     * ID bộ chỉ số.
     */
    private Long criteriaSetId;

    /**
     * Tên version bộ chỉ số.
     */
    private String criteriaVersion;

    /**
     * ID version chấm điểm.
     */
    private Long evaluationVersionId;

    /**
     * Tên version chấm điểm.
     */
    private String versionName;

    /**
     * ID version chấm điểm User được giao.
     */
    private Integer evaluationVersionUserId;

    /**
     * Thời gian cập nhật.
     */
    private LocalDateTime updatedAt;

    /**
     * Trạng thái đánh giá của thành viên hội đồng.
     */
    private String statusEvaluate;

    private String statusRecognition;


    private List<AddressDetailDTO> addressDetailDTOS;


    public UrbanCouncilDTO(Long userID, Long userIdReceive, String fullName, Integer urbanStatus, String statusCouncil, Long criteriaSetId,
                           String criteriaVersion, Long evaluationVersionId, String versionName, LocalDateTime updatedAt, String statusEvaluate) {
        this.userID = userID;
        this.userIdReceive = userIdReceive;
        this.fullName = fullName;
        this.urbanStatus = urbanStatus;
        this.statusCouncil = statusCouncil;
        this.criteriaSetId = criteriaSetId;
        this.criteriaVersion = criteriaVersion;
        this.evaluationVersionId = evaluationVersionId;
        this.versionName = versionName;
        this.evaluationVersionUserId = CriteriaSetServiceImpl.findEvaluationVersionUserId(this.evaluationVersionId);
        this.updatedAt = updatedAt;
        this.statusEvaluate = statusEvaluate;
        this.addressDetailDTOS = UsersServiceImpl.findAddressUser(userID);
    }

    public UrbanCouncilDTO(Long userID, Long userIdReceive, String fullName, Integer urbanStatus, String statusCouncil, Long criteriaSetId,
                          String criteriaVersion, Long evaluationVersionId, String versionName, String statusRecognition ,LocalDateTime updatedAt) {
        this.userID = userID;
        this.userIdReceive = userIdReceive;
        this.fullName = fullName;
        this.urbanStatus = urbanStatus;
        this.statusCouncil = statusCouncil;
        this.criteriaSetId = criteriaSetId;
        this.criteriaVersion = criteriaVersion;
        this.evaluationVersionId = evaluationVersionId;
        this.versionName = versionName;
        this.evaluationVersionUserId = CriteriaSetServiceImpl.findEvaluationVersionUserId(this.evaluationVersionId);
        this.updatedAt = updatedAt;
        this.statusEvaluate = statusRecognition;
        this.addressDetailDTOS = UsersServiceImpl.findAddressUser(userID);
    }

}
