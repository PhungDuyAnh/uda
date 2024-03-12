package com.udabe.dto.criteria;

import com.udabe.service.impl.CriteriaSetServiceImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CriteriaSetDTO {

    /**
     * ID bộ chỉ số.
     */
    private Long criteriaSetId;

    /**
     * Mã bộ chỉ số.
     */
    private String criteriaSetCode;

    /**
     * Tên bộ chỉ số.
     */
    private String criteriaSetName;

    /**
     * N : Không có hiệu lực.
     * Y : Ban hành.
     * E : Hết hiệu lực.
     * R : Thu hồi.
     */
    private String appliedStatus;

    /**
     * Ngày ban hành bộ chỉ số.
     */
    protected LocalDateTime appliedDate;

    /**
     * Version bộ chỉ số.
     */
    private String criteriaVersion;

    /**
     * Ngày tạo bộ chỉ số.
     */
    protected LocalDateTime createdAt;

    /**
     * Tham số Check khoảng trắng, null validate form.
     */
    private boolean isNull;

    /**
     * Số điểm còn lại(dựa vào số điểm ban đầu).
     */
    private Double pointLeft;

    public CriteriaSetDTO(Long criteriaSetId, String criteriaSetCode, String criteriaSetName, String appliedStatus, LocalDateTime appliedDate, String criteriaVersion, LocalDateTime createdAt) {
        this.criteriaSetId = criteriaSetId;
        this.criteriaSetCode = criteriaSetCode;
        this.criteriaSetName = criteriaSetName;
        this.appliedStatus = appliedStatus;
        this.appliedDate = appliedDate;
        this.criteriaVersion = criteriaVersion;
        this.createdAt = createdAt;
        this.isNull = CriteriaSetServiceImpl.checkIsNull(this.criteriaSetId);
    }

}
