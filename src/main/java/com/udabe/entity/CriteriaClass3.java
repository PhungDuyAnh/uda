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
@Table(name = "criteria_class_3")
public class CriteriaClass3 extends BaseEntity {

    /**
     * ID lớp 3.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_class_3_id")
    private Long criteriaClass3Id;

    /**
     * Tiêu đề lớp 3.
     */
    @Column(name = "content_vi")
    private String contentVi;

    @OneToMany(mappedBy = "criteriaClass3", cascade = CascadeType.ALL)
    private List<CriteriaDetail> criteriaDetails;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "criteria_class_2_id")
    private CriteriaClass2 criteriaClass2;

    @Override
    public Long getSeq() {
        return this.criteriaClass3Id;
    }

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
