package com.udabe.cmmn.util;

import com.udabe.entity.*;

public class ValidateUtilCriteria {

    public static boolean validateApplied(CriteriaSet criteriaSet) {
        if (isNullOrWhitespace(criteriaSet.getCriteriaSetCode()) || isNullOrWhitespace(criteriaSet.getCriteriaSetName())) {
            return false;
        }
        if (criteriaSet.getCriteriaClass1s().size() <= 0) {
            return false;
        }
        for (CriteriaClass1 class1 : criteriaSet.getCriteriaClass1s()) {
            if (isNullOrWhitespace(class1.getContentVi())) {
                return false;
            }
            if (class1.getCriteriaClass2s().size() <= 0) {
                return false;
            }
            for (CriteriaClass2 class2 : class1.getCriteriaClass2s()) {
                if (isNullOrWhitespace(class2.getContentVi())) {
                    return false;
                }
                if (class2.getCriteriaClass3s().size() <= 0) {
                    return false;
                }
                for (CriteriaClass3 class3 : class2.getCriteriaClass3s()) {
                    if (isNullOrWhitespace(class3.getContentVi())) {
                        return false;
                    }
                    if (class3.getCriteriaDetails().size() <= 0) {
                        return false;
                    }
                    for (CriteriaDetail detail : class3.getCriteriaDetails()) {
                        if (detail.getEvaluationType() == null) {
                            return false;
                        }
                        //1. Trắc nghiệm có/không? && 2. Trắc nghiệm lựa chọn đơn.
                        if (detail.getEvaluationType() == 1L || detail.getEvaluationType() == 2L) {
                            if (isNullOrWhitespace(detail.getContentVi()) || isNullOrWhitespace(detail.getSymbol())
                                    || isNullOrWhitespace(detail.getEvaluationType().toString())) {
                                return false;
                            }
                        }
                        //3. Nhập liệu theo tỷ lệ %. &&  4. Nhập liệu số.
                        if (detail.getEvaluationType() == 3L) {
                            if (isNullOrWhitespace(detail.getContentVi()) || isNullOrWhitespace(detail.getSymbol())
                                    || isNullOrWhitespace(detail.getEvaluationType().toString())
                                    || isNullOrWhitespace(detail.getConditions())
                                    || detail.getSettingConditions() == null || isNullOrWhitespace(detail.getSettingConditions().toString())
                                    || isNullOrWhitespace(detail.getUnitOfMeasure())) {
                                return false;
                            }
                        }
                        if (detail.getEvaluationType() == 4L) {
                            if (isNullOrWhitespace(detail.getContentVi()) || isNullOrWhitespace(detail.getSymbol())
                                    || isNullOrWhitespace(detail.getEvaluationType().toString())
                                    || isNullOrWhitespace(detail.getConditions())
                                    || detail.getSettingConditions() == null || isNullOrWhitespace(detail.getSettingConditions().toString())
                            ) {
                                return false;
                            }
                        }
                        //5. Nhập liệu phân số.
                        if (detail.getEvaluationType() == 5L) {
                            if (isNullOrWhitespace(detail.getContentVi()) || isNullOrWhitespace(detail.getSymbol())
                                    || isNullOrWhitespace(detail.getEvaluationType().toString())
                                    || isNullOrWhitespace(detail.getConditions())
                                    || detail.getSettingConditions() == null || isNullOrWhitespace(detail.getSettingConditions().toString())) {
                                return false;
                            }
                        }
                        for (Evaluation evaluation : detail.getEvaluations()) {
                            if (evaluation.getCriteriaDetail().getEvaluationType() == 2L) {
                                if (isNullOrWhitespace(evaluation.getValue()) || isNullOrWhitespace(evaluation.getPercentPass().toString())) {
                                    return false;
                                }
                            }
                            if (isNullOrWhitespace(evaluation.getValue())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    private static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

}
