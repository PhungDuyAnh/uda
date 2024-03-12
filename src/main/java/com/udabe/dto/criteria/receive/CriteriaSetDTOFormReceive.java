package com.udabe.dto.criteria.receive;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaSetDTOFormReceive {

    /**
     * ID bộ chỉ số.
     */
    private Long criteriaSetId;

    /**
     * Tên bộ chỉ số.
     */
    private String criteriaSetName;

    /**
     * Version bộ chỉ số.
     */
    private String criteriaVersion;

}
