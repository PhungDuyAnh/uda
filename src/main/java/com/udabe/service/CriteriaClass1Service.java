package com.udabe.service;

import com.udabe.dto.criteria.CriteriaClassDTO;
import com.udabe.entity.CriteriaClass1;
import org.springframework.http.ResponseEntity;

public interface CriteriaClass1Service {

    ResponseEntity<?> previewAllClass(Long criteriaSetId);

    ResponseEntity<?> getAllCriteriaClass(Long parentId, Long classNumber);

    ResponseEntity<?> saveCriteriaClass1(CriteriaClass1 criteriaClass1);

    ResponseEntity<?> findCriteriaClassById(Long id, Long classNumber);

    ResponseEntity<?> updateCriteriaClass(CriteriaClassDTO criteriaClassDTO, Long id);

    ResponseEntity<?> deleteCriteriaClass(Long id, Long classNumber);

}
