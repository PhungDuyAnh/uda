package com.udabe.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaClassRequest {
    private Long class1Id;

    private Long class2Id;

    private Long class3Id;

    private Long classDetailId;

    private int year;
}
