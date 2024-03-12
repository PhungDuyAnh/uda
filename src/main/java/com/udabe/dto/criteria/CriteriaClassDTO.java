package com.udabe.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Class body kế thừa class 1->3.
 */
public class CriteriaClassDTO {

    /**
     * ID class.
     */
    private Long criteriaClassId;

    /**
     * Tiêu đề class.
     */
    private String contentVi;

    /**
     * Tham số thể hiện số class(Transient).
     */
    @Transient
    private Long classNumber;

}
