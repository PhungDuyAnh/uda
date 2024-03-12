package com.udabe.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalCriteriaDTO {
    /**
     * Mã tiêu chí
     */
    private Long criteriaID;

    /**
     * Nội dung tiêu chí
     */
    private String contentVi;

    /**
     * Tổng số đô thị tham gia đăng ký đánh giá tiêu chí
     */
    private int sumUrbanJoinCriteria;

    /**
     * Tổng số đô thị đã đạt tiêu chí
     */
    private int sumUrbanPassCriteria;

    /**
     * Tỉ lệ đô thị đã hoàn thành tiêu chí
     *  (sumUrbanPassCriteria/sumUrbanJoinCriteria)*100
     */
    private float percentUrbanPassCriteria;
    /**
     * Danh sách các tiêu chí lớp con
     */
    private List<CriteriaClass2DTO> criteriaClass2DTOList;
    private List<CriteriaClass3DTO> criteriaClass3DTOList;
    private List<CriteriaDetailDTO> criteriaDetailDTOList;
}
