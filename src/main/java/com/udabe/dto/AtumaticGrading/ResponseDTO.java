package com.udabe.dto.AtumaticGrading;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO {
    private Long answerId;

    private Long questionId;

    private String selectedOption;

    private Long type;

    private Long evaluationId;

    private Double point;

    private Double weight;

    private String conditions;

    private Integer settingConditions;


    public ResponseDTO(Long answerId, Long questionId, String selectedOption, Long type, Double point, String conditions, Integer settingConditions){
        this.answerId = answerId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.type = type;
        this.point = point;
        this.conditions = conditions;
        this.settingConditions = settingConditions;
    }

    public ResponseDTO(Long answerId, Long questionId, Long evaluationId, Long type, Double point, Double weight){
        this.answerId = answerId;
        this.questionId = questionId;
        this.evaluationId = evaluationId;
        this.type = type;
        this.point = point;
        this.weight = weight;
    }
}
