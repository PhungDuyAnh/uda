package com.udabe.entity;

import com.udabe.cmmn.base.BaseEntity;

import com.udabe.service.impl.AnswerServiceImpl;
import com.udabe.service.impl.EvaluationVersionServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "criteria_set")
public class CriteriaSet extends BaseEntity {

    /**
     * ID bộ chỉ số.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_set_id")
    private Long criteriaSetId;

    /**
     * Mã bộ chỉ số.
     */
    @Column(name = "criteria_set_code", length = 50)
    private String criteriaSetCode;

    /**
     * Tên bộ chỉ số.
     */
    @Column(name = "criteria_set_name")
    private String criteriaSetName;

    /**
     * N : Không có hiệu lực.
     * Y : Ban hành.
     * E : Hết hiệu lực.
     * R : Thu hồi.
     */
    @Column(name = "applied_status", length = 1)
    private String appliedStatus;

    /**
     * Ngày ban hành bộ chỉ số.
     */
    @Column(name = "applied_date")
    protected LocalDateTime appliedDate;

    /**
     * Version bộ chỉ số.
     */
    @Column(name = "criteria_version", length = 50)
    private String criteriaVersion;

    @Column(name = "is_updated")
    private boolean isUpdated;

    /**
     * Tổng điểm ban đầu(mặc định là 100).
     */
    @Column(name = "total_point")
    private Double totalPoint;

    @OneToMany(mappedBy = "criteriaSet", cascade = CascadeType.ALL)
    private List<CriteriaClass1> criteriaClass1s;

    @OneToMany(mappedBy = "criteriaSet", cascade = CascadeType.ALL)
    private List<EvaluationVersion> evaluationVersions;

    /**
     * Tham số Tìm kiếm theo ngày(Transient).
     */
    @Transient
    private LocalDate searchAppliedDate;

    /**
     * Số điểm còn lại(dựa vào số điểm ban đầu).
     */
    @Transient
    private Double pointLeft;

    @Override
    public Long getSeq() {
        return this.criteriaSetId;
    }

    @PostPersist
    public void setDefaultValue() {

        this.totalPoint = 100.0;

    }

}
