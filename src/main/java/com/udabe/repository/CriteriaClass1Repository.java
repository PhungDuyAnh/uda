package com.udabe.repository;

import com.udabe.dto.criteria.CriteriaClass1DTO;
import com.udabe.entity.CriteriaClass1;
import com.udabe.entity.CriteriaSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CriteriaClass1Repository extends JpaRepository<CriteriaClass1, Long> {

    /**
     * Repository lấy danh sách class1 theo ID bộ chỉ số.
     *
     * @return Danh sách class1 theo ID bộ chỉ số.
     */
    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass1DTO(" +
            "c.criteriaClass1Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass1 c left join c.criteriaSet cs where cs.criteriaSetId = ?1")
    List<CriteriaClass1DTO> getAllCriteriaClass1(Long criteriaSetId);


    /**
     * Repository xem chi tiết class 1.
     *
     * @return Chi tiết class 1.
     */
    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass1DTO(" +
            "c.criteriaClass1Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass1 c where c.criteriaClass1Id = ?1")
    CriteriaClass1DTO findCriteriaClass1ById(Long criteriaClass1Id);

    /**
     * Repository preview class 1.
     *
     * @return Preview class 1.
     */
    @Query(value = "select * from criteria_class_1 where criteria_set_id = ?1", nativeQuery = true)
    List<CriteriaClass1> preview(Long criteriaSetId);


    /**
     * Repository Update class 1.
     *
     * @return Update class 1.
     */
    @Modifying
    @Transactional
    @Query("update CriteriaClass1 c set c.contentVi = ?1 where c.criteriaClass1Id = ?2")
    void updateCriteriaClass1(String contentVi, Long criteriaClass1Id);

    @Query("SELECT NEW com.udabe.dto.criteria.CriteriaClass1DTO(" +
            "c.criteriaClass1Id, c.contentVi " +
            ") " +
            "FROM CriteriaClass1 c where c.criteriaClass1Id = ?1")
    CriteriaClass1DTO getCriteriaClass1ByCriteriaClass1Id(Long criteriaClass1Id);
}
