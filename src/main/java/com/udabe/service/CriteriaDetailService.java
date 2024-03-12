package com.udabe.service;

import com.udabe.entity.CriteriaDetail;
import org.springframework.http.ResponseEntity;

public interface CriteriaDetailService {

    ResponseEntity<?> saveCriteriaDetail(CriteriaDetail criteriaDetail);

    ResponseEntity<?> updateCriteriaDetail(CriteriaDetail criteriaDetail, Long criteriaDetailId);

}
