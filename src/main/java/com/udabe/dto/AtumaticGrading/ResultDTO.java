package com.udabe.dto.AtumaticGrading;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDTO {

    private double score;
    private float countPass;
    List<Long> answerId = new ArrayList<>();

    public ResultDTO(double score, float countPass, List<Long> answerId ) {
        this.score = score;
        this.countPass = countPass;
        this.answerId = answerId;
    }

    public ResultDTO() {
    }
}
