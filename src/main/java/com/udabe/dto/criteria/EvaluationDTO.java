package com.udabe.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDTO {

    /**
     * ID lớp 5.
     */
    private Long evaluationId;

    /**
     * Nội dung lớp 5.
     */
    private String value;

    /**
     * % số điểm.
     */
    private Double percentPass;

}
