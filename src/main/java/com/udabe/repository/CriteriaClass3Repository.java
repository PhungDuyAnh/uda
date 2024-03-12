package com.udabe.repository;

import com.udabe.dto.criteria.CriteriaClass2DTO;
import com.udabe.dto.criteria.CriteriaClass3DTO;
import com.udabe.entity.CriteriaClass3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CriteriaClass3Repository extends JpaRepository<CriteriaClass3, Long> {

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass3DTO(" +
            "c.criteriaClass3Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass3 c where c.criteriaClass2.criteriaClass2Id = ?1")
    List<CriteriaClass3DTO> getAllCriteriaClass3ByClass2Id(Long criteriaClass2Id);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass3DTO(" +
            "c.criteriaClass3Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass3 c where c.criteriaClass3Id = ?1")
    CriteriaClass3DTO findcriteriaClass3ById(Long criteriaClass3Id);

    @Modifying
    @Transactional
    @Query("update CriteriaClass3 c set c.contentVi = ?1 where c.criteriaClass3Id = ?2")
    void updateCriteriaClass3(String contentVi, Long criteriaClass3Id);

}
