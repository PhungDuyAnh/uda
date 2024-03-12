package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udabe.cmmn.base.BaseEntity;

import com.udabe.service.impl.AnswerServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "criteria_class_1")
public class CriteriaClass1 extends BaseEntity {

    /**
     * ID lớp 1.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_class_1_id")
    private Long criteriaClass1Id;

    /**
     * Tiêu đề lớp 1.
     */
    @Column(name = "content_vi")
    private String contentVi;

    @OneToMany(mappedBy = "criteriaClass1", cascade = CascadeType.ALL)
    private List<CriteriaClass2> criteriaClass2s;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "criteria_set_id")
    private CriteriaSet criteriaSet;

    @Override
    public Long getSeq() {
        return this.criteriaClass1Id;
    }

    /**
     * Tham số Tìm kiếm theo lớp cha(Transient).
     */
    @Transient
    private Long parentId;

    /**
     * Tham số Tìm kiếm theo số lớp(Transient).
     */
    @Transient
    private Long classNumber;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    @Transient
    private boolean isNull;

    @Transient
    private boolean numAnswerBy;

    @PostLoad
    public void initialize() {
        this.isNull = (this.contentVi == null || this.contentVi.trim().isEmpty());
    }

}
