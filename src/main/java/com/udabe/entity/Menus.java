package com.udabe.entity;

import com.udabe.cmmn.base.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serial;
import java.util.List;

@Data
@Entity
@Table(name = "menus")
public class Menus extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuID;

    @Column(name = "menu_code", nullable = false, length = 20)
    private String menuCode;

    @Column(name = "menu_link", length = 255)
    private String menuLink;

    @Column(name = "upper_seq", nullable = false)
    private Long upperSeq;

    @Column(name = "menu_order", nullable = false)
    private Long menuOrder;

    @Column(name = "menu_name_vi", length = 255, nullable = false)
    private String menuNameVi;

    @Column(name = "menu_name_en", length = 255)
    private String menuNameEn;

    @Column(name = "issued", nullable = false)
    private boolean issued;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "icon_name")
    private String iconName ;

    @Transient
    private List<Long> arrange;

    public Menus() {

    }

    @Override
    public Long getSeq() {
        return this.menuID;
    }

    public Menus(Long menuID, String menuCode, String menuLink, Long upperSeq, Long menuOrder, String menuNameVi, String menuNameEn, boolean issued) {
        this.menuID = menuID;
        this.menuCode = menuCode;
        this.menuLink = menuLink;
        this.upperSeq = upperSeq;
        this.menuOrder = menuOrder;
        this.menuNameVi = menuNameVi;
        this.menuNameEn = menuNameEn;
        this.issued = issued;
    }
}


