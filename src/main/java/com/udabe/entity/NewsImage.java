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
@Table(name = "news_image")
public class NewsImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_image_id")
    private Long newsImageId;

    @Column(name = "image_nm")
    private String imageNm;

    @Column(name = "save_nm")
    private String saveNm;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "image_size")
    private Long imageSize;

    @CreationTimestamp
    private LocalDateTime saveDt;

    @Override
    public Long getSeq() {
        return null;
    }
}
