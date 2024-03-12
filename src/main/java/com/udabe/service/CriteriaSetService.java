package com.udabe.service;

import com.udabe.entity.CriteriaSet;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CriteriaSetService {

    ResponseEntity<?> saveCriteriaSet(CriteriaSet criteriaSet);

    ResponseEntity<?> checkNullForm(Long criDraftId);

    ResponseEntity<?> findCriteriaSetById(Long criteriaSetId);

    ResponseEntity<?> updateCriteriaSet(CriteriaSet criteriaSet, Long criteriaSetId);

    ResponseEntity<?> versionCriteriaFilter(CriteriaSet entity, Pageable pageable);

    ResponseEntity<?> selectAllDraft();

    ResponseEntity<?> updateStatusApplied(Long criDraftId, String criteriaVersion);

    ResponseEntity<?> updateStatusRecall(Long criDraftId, String criteriaVersion);

}
