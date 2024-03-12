package com.udabe.dto.criteria.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaClass2DTOForm {

    /**
     * ID lớp 2.
     */
    private Long criteriaClass2Id;

    /**
     * Tiêu đề lớp 2.
     */
    private String contentVi;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    private boolean isNull;

    private List<CriteriaClass3DTOForm> criteriaClass3s;
}