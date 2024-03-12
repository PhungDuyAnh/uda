package com.udabe.dto.menu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class AccessControlDTO {
    private Long accessControlId;

    private Long menuID;

    private Long roleID;

    private List<Long> arrange;

}
