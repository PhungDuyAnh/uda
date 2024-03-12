package com.udabe.dto.criteria.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaClass3DTOForm {

    /**
     * ID lớp 3.
     */
    private Long criteriaClass3Id;

    /**
     * Tiêu đề lớp 3.
     */
    private String contentVi;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    private boolean isNull;

    private List<CriteriaDetailDTOForm> criteriaDetails;
}
