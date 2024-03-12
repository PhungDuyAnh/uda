package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udabe.cmmn.base.BaseEntity;
import com.udabe.dto.criteria.form.AnswerDTOForm;
import com.udabe.service.impl.AnswerServiceImpl;
import com.udabe.service.impl.CriteriaDetailServiceImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "criteria_detail")
public class CriteriaDetail extends BaseEntity {

    /**
     * ID lớp 4.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_detail_id")
    private Long criteriaDetailId;

    /**
     * Tiêu đề lớp 4.
     */
    @Column(name = "content_vi")
    private String contentVi;

    /**
     * Chú thích.
     */
    @Column(name = "note")
    private String note;

    /**
     * Ký hiệu.
     */
    @Column(name = "symbol")
    private String symbol;

    /**
     * true : Mức độ áp dụng nâng cao.
     * false : Mức độ áp dụng cơ bản.
     */
    @Column(name = "apply_level")
    private boolean applyLevel;

    /**
     * Tiêu chuẩn đạt(áp dụng cho loại câu hỏi nhập liệu).
     */
    @Column(name = "conditions")
    private String conditions;

    /**
     * Số điểm của câu hỏi.
     */
    @Column(name = "point")
    private Double point;

    /**
     * 1. Trắc nghiệm có/không?
     * 2. Trắc nghiệm lựa chọn đơn.
     * 3. Nhập liệu theo tỷ lệ %.
     * 4. Nhập liệu số.
     * 5. Nhập liệu phân số.
     */
    @Column(name = "evaluation_type")
    private Long evaluationType;

    /**
     * Điều kiện(áp dụng cho loại câu hỏi tỷ lệ (điều kiện)).
     * 1. Nhỏ hơn.
     * 2. Lớn hơn.
     * 3. Bằng.
     * 4. Nhỏ hơn hoặc bằng.
     * 5. Lớn hơn hoặc bằng.
     */
    @Column(name = "setting_conditions")
    private Integer settingConditions;

    /**
     * Đơn vị(áp dụng cho loại câu hỏi tỷ lệ (điều kiện)).
     */
    @Column(name = "unit_of_measure")
    private String unitOfMeasure;


    @OneToMany(mappedBy = "criteriaDetail", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations;

    @JsonIgnore
    @OneToMany(mappedBy = "criteriaDetail", cascade = CascadeType.ALL)
    private List<CouncilScore> councilScores;

    @JsonIgnore
    @OneToMany(mappedBy = "criteriaDetail", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "criteria_class_3_id")
    private CriteriaClass3 criteriaClass3;

    @Override
    public Long getSeq() {
        return this.criteriaDetailId;
    }

    /**
     * Tham số nhận vào ID class 3(Transient).
     */
    @Transient
    private Long criteriaClass3Id;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    @Transient
    private boolean isNull;

    @Transient
    private AnswerDTOForm answerUser;

    @Transient
    private AnswerDTOForm answerUserReceive;

    @PreUpdate
    public void setDefaultValue() {
        if (this.evaluationType == 3) {
            this.unitOfMeasure = "%";
        }
    }

    @PostLoad
    public void initialize() {
//        this.answerUser = CriteriaDetailServiceImpl.getAnswerOfUser(this.criteriaDetailId);
        if (isNullOrWhitespace(this.contentVi)) {
            this.isNull = true;
        }
        //1. Trắc nghiệm có/không? && 2. Trắc nghiệm lựa chọn đơn.
        if (this.evaluationType == 1L || this.evaluationType == 2L) {
            if (isNullOrWhitespace(this.contentVi) || isNullOrWhitespace(this.symbol)
                    || isNullOrWhitespace(this.evaluationType.toString())) {
                this.isNull = true;
            } else {
                this.isNull = false;
            }
        }
        //3. Nhập liệu theo tỷ lệ %. &&  4. Nhập liệu số.
        if (this.evaluationType == 3L) {
            if (isNullOrWhitespace(this.contentVi) || isNullOrWhitespace(this.symbol)
                    || isNullOrWhitespace(this.evaluationType.toString())
                    || isNullOrWhitespace(this.conditions)
                    || isNullOrWhitespace(this.unitOfMeasure)) {
                this.isNull = true;
            } else {
                // Check if this.settingConditions is null before calling toString()
                if (this.settingConditions == null || isNullOrWhitespace(this.settingConditions.toString())) {
                    this.isNull = true;
                } else {
                    this.isNull = false;
                }
            }
        }
        if (this.evaluationType == 4L) {
            if (isNullOrWhitespace(this.contentVi) || isNullOrWhitespace(this.symbol)
                    || isNullOrWhitespace(this.evaluationType.toString())
                    || isNullOrWhitespace(this.conditions)) {
                this.isNull = true;
            } else {
                // Check if this.settingConditions is null before calling toString()
                if (this.settingConditions == null || isNullOrWhitespace(this.settingConditions.toString())) {
                    this.isNull = true;
                } else {
                    this.isNull = false;
                }
            }
        }
        //5. Nhập liệu phân số.
        if (this.evaluationType == 5L) {
            if (isNullOrWhitespace(this.contentVi) || isNullOrWhitespace(this.symbol)
                    || isNullOrWhitespace(this.evaluationType.toString())
                    || isNullOrWhitespace(this.conditions)) {
                this.isNull = true;
            } else {
                // Check if this.settingConditions is null before calling toString()
                if (this.settingConditions == null || isNullOrWhitespace(this.settingConditions.toString())) {
                    this.isNull = true;
                } else {
                    this.isNull = false;
                }
            }
        }
        if (CriteriaDetailServiceImpl.checkNullEvalution(this.criteriaDetailId) == true) {
            this.isNull = true;
        }
    }

    private static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

}
