package com.udabe.dto.menu;

import com.udabe.dto.role.RoleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class MenuDTO {

    private Long menuID;

    private String menuNameVi;

    private String menuLink;

    private Long upperSeq;

    private String menuCode;

    private boolean issued;

    private boolean deleted;

    private Long menuOrder;

    private String iconName;

    private ArrayList<String> roleName;

    private List<MenuDTO> subMenu;

    public MenuDTO(Long menuID, String menuNameVi, String menuLink, Long upperSeq, String menuCode, boolean issued, boolean deleted, Long menuOrder, String iconName) {
        this.menuID = menuID;
        this.menuNameVi = menuNameVi;
        this.menuLink = menuLink;
        this.upperSeq = upperSeq;
        this.menuCode = menuCode;
        this.issued = issued;
        this.deleted = deleted;
        this.menuOrder = menuOrder;
        this.iconName = iconName;
    }
}
