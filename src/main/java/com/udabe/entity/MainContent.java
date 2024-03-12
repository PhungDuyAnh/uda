package com.udabe.entity;

import com.udabe.cmmn.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "main_content")
public class MainContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_content_id")
    private Long mainContentId;

    @Column(name = "content_nm")
    private String contentNm;

    @Column(name = "save_nm")
    private String saveNm;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "image_size")
    private Long contentSize;

    @CreationTimestamp
    private LocalDateTime saveDt;

    @Override
    public Long getSeq() {
        return null;
    }
}
