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
@Table(name = "evaluation")
public class Evaluation extends BaseEntity {

    /**
     * ID lớp 5.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long evaluationId;

    /**
     * Nội dung lớp 5.
     */
    @Column(name = "value")
    private String value;

    /**
     * % số điểm.
     */
    @Column(name = "percent_pass")
    private Double percentPass;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "criteria_detail_id")
    private CriteriaDetail criteriaDetail;

    @Override
    public Long getSeq() {
        return this.evaluationId;
    }

    /**
     * Tham số nhận vào ID class 4(Transient).
     */
    @Transient
    private Long criteriaDetailId;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    private List<Answer> answers;

}
