package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udabe.cmmn.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "criteria_class_2")
public class CriteriaClass2 extends BaseEntity {

    /**
     * ID lớp 2.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_class_2_id")
    private Long criteriaClass2Id;

    /**
     * Tiêu đề lớp 2.
     */
    @Column(name = "content_vi")
    private String contentVi;

    @OneToMany(mappedBy = "criteriaClass2", cascade = CascadeType.ALL)
    private List<CriteriaClass3> criteriaClass3s;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "criteria_class_1_id")
    private CriteriaClass1 criteriaClass1;

    @Override
    public Long getSeq() {
        return this.criteriaClass2Id;
    }

    /**
     * Tham số Tìm kiếm theo lớp cha(Transient).
     */
    @Transient
    private Long criteriaClass1Id;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    @Transient
    private boolean isNull;

    @PostLoad
    public void initialize() {
        this.isNull = (this.contentVi == null || this.contentVi.trim().isEmpty());
    }

}
