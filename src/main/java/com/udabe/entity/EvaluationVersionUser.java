package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "evaluation_version_user")
public class EvaluationVersionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_version_user_id")
    private Integer evaluationVersionUserId;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluationVersionUser", cascade = CascadeType.ALL)
    private List<CouncilScore> councilScores;

    /**
     * Trạng thái chấm điểm của thành viên hội đồng.
     * Y  : Đã đánh giá.
     * N  : Chưa đánh giá.
     */
    @Column(name = "status_evaluate", columnDefinition = "varchar(1) default 'N'")
    private String statusEvaluate;

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

    /**
     * Điểm số tổng của bản đánh giá hội đồng chấm.
     */
    @Column(name = "point", columnDefinition = "float default '0'")
    private Float point;
    
    @Column(name = "time_return")
    protected LocalDateTime timeReturn;

    @ManyToOne
    @JoinColumn(name = "evaluation_version_id")
    private EvaluationVersion evaluationVersion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @PrePersist
    public void setDefaultValue() {
        this.statusEvaluate = "N";
        this.point = (float) 0;
    }

}
