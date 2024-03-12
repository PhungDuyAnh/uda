package com.udabe.dto.criteria.form;

import com.udabe.service.impl.AnswerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaClass1DTOForm {

    /**
     * ID lớp 1.
     */
    private Long criteriaClass1Id;

    /**
     * Tiêu đề lớp 1.
     */
    private String contentVi;

    /**
     * Tham số Check khoảng trắng, null validate form(Transient).
     */
    private boolean isNull;

    private boolean numAnswerClass1 = AnswerServiceImpl.getNumAnswerClass1(this.criteriaClass1Id) ;

}
