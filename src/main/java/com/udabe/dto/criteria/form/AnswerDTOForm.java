package com.udabe.dto.criteria.form;

import com.udabe.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTOForm {

    private Long answerId;

    private String valueAnswer;

    private Long evaluationId;

    private Boolean isPass;

}
