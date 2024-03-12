package com.udabe.entity;

import com.udabe.cmmn.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "news")
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "news_title")
    private String newsTitle;

    /**
     * Loại tin tức:
     * 1. Tin bộ chỉ số
     * 2. Tin phát triển đô thị
     * 3. Thông báo
     */
    @Column(name = "news_type")
    private Integer newsType;

    @Column(name = "general_content")
    private String generalContent;

    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * Trạng thái hiển thị tin
     * Y. Hiển thị
     * N. Không hiển thị
     */
    @Column(name = "is_display")
    private String isDisplay;

//    @Transient
//    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Transient
    private String fullName;

    @Override
    public Long getSeq() {
        return this.newsId;
    }
}
