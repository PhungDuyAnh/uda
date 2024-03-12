package com.udabe.dto.criteria.receive;

import com.udabe.service.impl.CriteriaSetServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EvaluationReceiveDTO {

    //DTO bản lịch sử đăng ký đánh giá.

    private Long evaluationVersionId;

    private LocalDateTime createdAt;

    private boolean isSend;

    private String versionName;

    /**
     * Trạng thái đánh giá
     * N: chưa được đánh giá
     * T đã được đánh giá tự động
     * Y đã hoàn thành đánh giá
     */
    private String status;

    private Float sumScore;

    private Float sumPercent;

    private Long criteriaSetId;

    private Integer urbanStatus;

    //Thông tin bộ chỉ số.
    private CriteriaSetDTOFormReceive criteriaSetDTOForm;

    public EvaluationReceiveDTO(Long evaluationVersionId, LocalDateTime createdAt, boolean isSend, String versionName, String status, Float sumScore, Float sumPercent, Long criteriaSetId, Integer urbanStatus) {
        this.evaluationVersionId = evaluationVersionId;
        this.createdAt = createdAt;
        this.isSend = isSend;
        this.versionName = versionName;
        this.status = status;
        this.sumScore = sumScore;
        this.sumPercent = sumPercent;
        this.criteriaSetId = criteriaSetId;
        this.urbanStatus = urbanStatus;
        this.criteriaSetDTOForm = CriteriaSetServiceImpl.getReceiveFormSet(this.criteriaSetId);
    }

}
