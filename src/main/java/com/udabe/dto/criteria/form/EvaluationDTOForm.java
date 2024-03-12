package com.udabe.dto.criteria.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDTOForm {

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
