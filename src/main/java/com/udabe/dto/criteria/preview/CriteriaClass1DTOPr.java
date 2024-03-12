package com.udabe.dto.criteria.preview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaClass1DTOPr {

    /**
     * ID lớp 1.
     */
    private Long criteriaClass1Id;

    /**
     * Tiêu đề lớp 1.
     */
    private String contentVi;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    private boolean isNull;

    private List<CriteriaClass2DTOPr> criteriaClass2s;

}
