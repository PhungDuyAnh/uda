package com.udabe.service;

import com.udabe.dto.Statistical.StatisticalDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface StatisticalService {

    ResponseEntity<?> getStatusGrowClass(Long userId, String year);

    ResponseEntity<?> getUrbanList(Long addressCodeId, String urbanName);

    ResponseEntity<?> getUrbanDevStatus(StatisticalDTO statisticalDTO);

    ResponseEntity<?> searchProvince(String provinceName);

    ResponseEntity<?> getBasicInfo(Byte unitChart);

    ResponseEntity<?> getStaUrbanList(StatisticalDTO statisticalDTO, Long addressCodeid, Pageable pageable);

    ResponseEntity<?> getBasicInfoEvaluated(StatisticalDTO statisticalDTO);

    ResponseEntity<?> getUrbanRegisterStatus(StatisticalDTO statisticalDTO);

    ResponseEntity<?> getBasicInfoUrban(Byte unitChart, Long userId);

    ResponseEntity<?> getCriteriaClass1();

    ResponseEntity<?> getStaCriteriaClass1(Long criteriaID, int year);

    ResponseEntity<?> getStaCriteriaClass2(Long criteriaClass2Id, int year);

    ResponseEntity<?> getStaCriteriaClass3(Long criteriaClass3Id, int year);

    ResponseEntity<?> getStaCriteriaDetail(Long criteriaDetail, int year);
}
