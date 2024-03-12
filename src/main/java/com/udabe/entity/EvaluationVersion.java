package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.udabe.cmmn.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "evaluation_version")
public class EvaluationVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_version_id")
    private Long evaluationVersionId;

    @Column(name = "version_name")
    private String versionName;

    /**
     * Trạng thái đánh giá
     * N: chưa được đánh giá
     * T đã được đánh giá tự động
     * Y đã hoàn thành đánh giá
     */
    @Column(name = "status")
    private String status;

    /**
     * Trạng thái gửi bản đánh giá
     * N: Chưa gửi
     * Y: Đã gửi
     */
    @Column(name = "is_send")
    private boolean isSend;

    @Column(name = "sum_score")
    private Float sumScore;

    @Column(name = "sum_percent")
    private Float sumPercent;

    /**
     * Trạng thái công nhận đô thị thông minh của chủ tịch hội đồng.
     * 1  : Công nhận.
     * 2  : Không công nhận.
     */
    @Column(name = "status_recognition")
    private String statusRecognition;

    /**
     * Lý do không công nhận.
     */
    @Column(name = "comment_recognition")
    private String commentRecognition;

    @Column(name = "time_return")
    protected LocalDateTime timeReturn;

    @Transient
    private boolean isNull;

    @Transient
    private Long userId;

    @Transient
    private Long criteriaSetId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "criteria_set_id")
    private CriteriaSet criteriaSet;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluationVersion", cascade = CascadeType.ALL)
    private List<Answer> answer;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluationVersion", cascade = CascadeType.ALL)
    private List<EvaluationVersionUser> evaluationVersionUsers;

    @Override
    public Long getSeq() {
        return null;
    }
}
