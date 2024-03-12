package com.udabe.dto.criteria;

import com.udabe.dto.criteria.form.AnswerDTOForm;
import com.udabe.service.impl.CriteriaDetailServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriteriaDetailIds {

    private Long criteriaDetailId;

    private AnswerDTOForm answerUser;

    public CriteriaDetailIds(Long criteriaDetailId) {
        this.criteriaDetailId = criteriaDetailId;
        this.answerUser = CriteriaDetailServiceImpl.getAnswerOfUser(this.criteriaDetailId);
    }

}
