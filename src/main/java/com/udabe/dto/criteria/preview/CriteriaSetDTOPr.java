package com.udabe.dto.criteria.preview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaSetDTOPr {

    private Long criteriaSetId;

    /**
     * Mã bộ chỉ số.
     */
    private String criteriaSetCode;

    /**
     * Tên bộ chỉ số.
     */
    private String criteriaSetName;

    /**
     * N : Không có hiệu lực.
     * Y : Ban hành.
     * E : Hết hiệu lực.
     * R : Thu hồi.
     */
    private String appliedStatus;

    /**
     * Ngày ban hành bộ chỉ số.
     */
    protected LocalDateTime appliedDate;

    /**
     * Version bộ chỉ số.
     */
    private String criteriaVersion;

    private List<CriteriaClass1DTOPr> criteriaClass1s;

}
