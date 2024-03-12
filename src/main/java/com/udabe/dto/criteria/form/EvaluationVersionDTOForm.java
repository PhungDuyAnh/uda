package com.udabe.dto.criteria.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationVersionDTOForm {

    private Long evaluationVersionId;

    private String status;

    private boolean isSend;
}
