package com.udabe.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaDetailDTO {

    /**
     * ID lớp 4.
     */
    private Long criteriaDetailId;

    /**
     * Tiêu đề lớp 4.
     */
    private String contentVi;

    /**
     * Chú thích.
     */
    private String note;

    /**
     * Ký hiệu.
     */
    private String symbol;

    /**
     * true : Mức độ áp dụng nâng cao.
     * false : Mức độ áp dụng cơ bản.
     */
    private boolean applyLevel;

    /**
     * Điều kiện(áp dụng cho loại câu hỏi tỷ lệ (điều kiện)).
     */
    private String conditions;

    /**
     * Đơn vị(áp dụng cho loại câu hỏi tỷ lệ (điều kiện)).
     */
    private String unitOfMeasure;

    /**
     * Thiết lập tiêu chuẩn đạt.
     * 1. Nhỏ hơn.
     * 2. Lớn hơn.
     * 3. Bằng.
     * 4. Nhỏ hơn hoặc bằng.
     * 5. Lớn hơn hoặc bằng.
     */
    private Integer settingConditions;

    /**
     * 1. Câu hỏi có/không?
     * 2. Câu hỏi nhiều lựa chọn.
     * 3. Câu hỏi tỷ lệ (điều kiện).
     * 4. Câu hỏi nhập liệu.
     */
    private Long evaluationType;

    private Double point;

}
