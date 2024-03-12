package com.udabe.repository;

import com.udabe.dto.criteria.CriteriaClass2DTO;
import com.udabe.entity.CriteriaClass2;
import com.udabe.entity.CriteriaSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CriteriaClass2Repository extends JpaRepository<CriteriaClass2, Long> {

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass2DTO(" +
            "c.criteriaClass2Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass2 c where c.criteriaClass1.criteriaClass1Id = ?1")
    List<CriteriaClass2DTO> getAllCriteriaClass2ByClass1Id(Long criteriaClass1Id);


    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass2DTO(" +
            "c.criteriaClass2Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass2 c where c.criteriaClass2Id = ?1")
    CriteriaClass2DTO findcriteriaClass2ById(Long criteriaClass2Id);

    @Modifying
    @Transactional
    @Query("update CriteriaClass2 c set c.contentVi = ?1 where c.criteriaClass2Id = ?2")
    void updateCriteriaClass2(String contentVi, Long criteriaClass2Id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM criteria_class_2 WHERE criteria_class_1_id = ?1 ")
    List<CriteriaClass2> getByClass1Preview(Long criteriaClass1Id);

    @Query(nativeQuery = true,
            value = "SELECT criteria_class_2_id FROM criteria_class_2 WHERE criteria_class_1_id = ?1 ")
    Long[] getClass2IDs(Long criteriaClass1Id);
}
