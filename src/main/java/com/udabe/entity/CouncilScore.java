package com.udabe.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "council_score")
public class CouncilScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "council_score_id")
    private Integer councilScoreId;

    /**
     * Số điểm của câu hỏi do thành viên hội đồng chấm.
     */
    @Column(name = "number_score")
    private Float numberScore;

    /**
     * Nhận xét của hội đồng chấm điểm.
     */
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "criteria_detail_id")
    private CriteriaDetail criteriaDetail;

    @ManyToOne
    @JoinColumn(name = "evaluation_version_user_id")
    private EvaluationVersionUser evaluationVersionUser;

    @Transient
    private Long criteriaDetailId;

    @Transient
    private Integer evaluationVersionUserId;

}
