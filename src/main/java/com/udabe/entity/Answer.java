package com.udabe.entity;

import com.udabe.cmmn.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "value_answer")
    private String valueAnswer;

    @Transient
    private Long evaluationId;

    @Transient
    private Long criteriaDetailId;

    @Transient
    private Long evaluationVersionId;

    @Transient
    private String firstNumber;

    @Transient
    private String secondNumber;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "criteria_detail_id")
    private CriteriaDetail criteriaDetail;

    @ManyToOne
    @JoinColumn(name = "evaluation_version_id")
    private EvaluationVersion evaluationVersion;

    @Column(name = "is_pass")
    private Boolean isPass;

    @Override
    public Long getSeq() {
        return null;
    }
}

