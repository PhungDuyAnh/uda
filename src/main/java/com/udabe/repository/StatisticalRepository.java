package com.udabe.repository;

import com.udabe.entity.CriteriaSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticalRepository extends JpaRepository<CriteriaSet, Long> {

    @Query(value = "select count(c3.criteria_class_3_id) " +
            "from criteria_class_3 c3 inner join criteria_class_2 c on c3.criteria_class_2_id = c.criteria_class_2_id " +
            "inner join criteria_class_1 cc1 on c.criteria_class_1_id = cc1.criteria_class_1_id inner join " +
            "criteria_set cs on cc1.criteria_set_id = cs.criteria_set_id where cs.criteria_set_id = ?1", nativeQuery = true)
    Integer getNumClass3(Long criteriaSetId);


    @Query(value = "select count(c2.criteria_class_2_id) " +
            "from criteria_class_2 c2 inner join criteria_class_1 c on c2.criteria_class_1_id = c.criteria_class_1_id " +
            "inner join criteria_set cs on c.criteria_set_id = cs.criteria_set_id " +
            "where cs.criteria_set_id = ?1", nativeQuery = true)
    Integer getNumClass2(Long criteriaSetId);


    @Query(value = "select count(c1.criteria_class_1_id) " +
            "from criteria_class_1 c1 inner join criteria_set cs on c1.criteria_set_id = cs.criteria_set_id " +
            "where c1.criteria_set_id = ?1", nativeQuery = true)
    Integer getNumClass1(Long criteriaSetId);

    @Query(value = "SELECT evaluation_version_id FROM evaluation_version WHERE user_id = ?1 AND updated_at = (SELECT MAX(updated_at) FROM evaluation_version WHERE user_id = ?1 AND status <> 'N')", nativeQuery = true)
    Long getEvaluationVersion (Long userId);

    @Query(value = "select criteria_version from criteria_set where applied_status = 'Y'", nativeQuery = true)
    String getCriteriaVersion();

    @Query(value = "select urban_status from users where user_id = ?1", nativeQuery = true)
    Integer getUrbanStatus(Long userId);

    @Query(value = "SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1", nativeQuery = true)
    Long getMaxEvaluationVersionId(Long userId, int year);

    @Query(value = "select COUNT(a.answer_id) from answer a where a.evaluation_version_id = " +
            "(SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1) and a.is_pass = true", nativeQuery = true)
    Float getPassNum(Long userId, int year);

    @Query(value = "select COUNT(a.answer_id) from answer a where a.evaluation_version_id = " +
            "(SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1) and a.is_pass is null || a.is_pass = false", nativeQuery = true)
    Float getNotPassNum(Long userId, int year);


    //Số câu đạt class3:
    @Query(value = "SELECT COUNT(c3.criteria_class_3_id) " +
            "FROM criteria_class_3 c3 " +
            "         LEFT JOIN ( " +
            "    SELECT c3.criteria_class_3_id, COUNT(cd.criteria_detail_id) AS num_criteria_detail " +
            "    FROM criteria_class_3 c3 " +
            "             LEFT JOIN criteria_class_2 c2 ON c3.criteria_class_2_id = c2.criteria_class_2_id " +
            "             LEFT JOIN criteria_class_1 c1 ON c2.criteria_class_1_id = c1.criteria_class_1_id " +
            "             LEFT JOIN criteria_detail cd ON c3.criteria_class_3_id = cd.criteria_class_3_id " +
            "             INNER JOIN answer a ON cd.criteria_detail_id = a.criteria_detail_id " +
            "             INNER JOIN evaluation_version e ON a.evaluation_version_id = e.evaluation_version_id " +
            "    WHERE a.evaluation_version_id = ( " +
            "        SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1 " +
            "    ) " +
            "      AND a.is_pass = true " +
            "    GROUP BY c3.criteria_class_3_id " +
            ") AS passed_criteria ON c3.criteria_class_3_id = passed_criteria.criteria_class_3_id " +
            "WHERE passed_criteria.num_criteria_detail = ( " +
            "    SELECT COUNT(cd2.criteria_detail_id) " +
            "    FROM criteria_detail cd2 " +
            "    WHERE cd2.criteria_class_3_id = c3.criteria_class_3_id " +
            ") ",nativeQuery = true)
    Float getNumPassClass3(Long userId, int year);



    //Số câu đạt class2:
    @Query(value = "SELECT COUNT(c2.criteria_class_2_id)  " +
            "FROM criteria_class_2 c2  " +
            "         LEFT JOIN (  " +
            "    SELECT c2.criteria_class_2_id, COUNT(c3.criteria_class_3_id) AS num_criteria_class_3  " +
            "    FROM criteria_class_2 c2  " +
            "             LEFT JOIN criteria_class_3 c3 ON c2.criteria_class_2_id = c3.criteria_class_2_id  " +
            "             LEFT JOIN (  " +
            "        SELECT c3.criteria_class_3_id, COUNT(cd.criteria_detail_id) AS num_criteria_detail  " +
            "        FROM criteria_class_3 c3  " +
            "                 LEFT JOIN criteria_detail cd ON c3.criteria_class_3_id = cd.criteria_class_3_id  " +
            "                 INNER JOIN answer a ON cd.criteria_detail_id = a.criteria_detail_id  " +
            "                 INNER JOIN evaluation_version e ON a.evaluation_version_id = e.evaluation_version_id  " +
            "        WHERE a.evaluation_version_id = (  " +
            "            SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1 " +
            "        )  " +
            "          AND a.is_pass = true  " +
            "        GROUP BY c3.criteria_class_3_id  " +
            "    ) AS passed_criteria ON c3.criteria_class_3_id = passed_criteria.criteria_class_3_id  " +
            "    WHERE passed_criteria.num_criteria_detail = (  " +
            "        SELECT COUNT(cd2.criteria_detail_id)  " +
            "        FROM criteria_detail cd2  " +
            "        WHERE cd2.criteria_class_3_id = c3.criteria_class_3_id  " +
            "    )  " +
            "    GROUP BY c2.criteria_class_2_id  " +
            ") AS passed_criteria_class_3 ON c2.criteria_class_2_id = passed_criteria_class_3.criteria_class_2_id  " +
            "WHERE passed_criteria_class_3.num_criteria_class_3 = (  " +
            "    SELECT COUNT(c3.criteria_class_3_id)  " +
            "    FROM criteria_class_3 c3  " +
            "    WHERE c3.criteria_class_2_id = c2.criteria_class_2_id  " +
            ") ", nativeQuery = true)
    Float getNumPassClass2(Long userId, int year);



    //Số câu đạt class1:
    @Query(value = "SELECT COUNT(c1.criteria_class_1_id)   " +
            "FROM criteria_class_1 c1   " +
            "         LEFT JOIN (   " +
            "    SELECT c1.criteria_class_1_id, COUNT(c2.criteria_class_2_id) AS num_criteria_class_2   " +
            "    FROM criteria_class_1 c1   " +
            "             LEFT JOIN criteria_class_2 c2 ON c1.criteria_class_1_id = c2.criteria_class_1_id   " +
            "             LEFT JOIN (   " +
            "        SELECT c2.criteria_class_2_id, COUNT(c3.criteria_class_3_id) AS num_criteria_class_3   " +
            "        FROM criteria_class_2 c2   " +
            "                 LEFT JOIN criteria_class_3 c3 ON c2.criteria_class_2_id = c3.criteria_class_2_id   " +
            "                 LEFT JOIN (   " +
            "            SELECT c3.criteria_class_3_id, COUNT(cd.criteria_detail_id) AS num_criteria_detail   " +
            "            FROM criteria_class_3 c3   " +
            "                     LEFT JOIN criteria_detail cd ON c3.criteria_class_3_id = cd.criteria_class_3_id   " +
            "                     INNER JOIN answer a ON cd.criteria_detail_id = a.criteria_detail_id   " +
            "                     INNER JOIN evaluation_version e ON a.evaluation_version_id = e.evaluation_version_id   " +
            "            WHERE a.evaluation_version_id = (   " +
            "                SELECT evaluation_version_id FROM evaluation_version WHERE YEAR(updated_at) = ?2 AND user_id = ?1 AND status <> 'N' ORDER BY updated_at DESC LIMIT 1  " +
            "            )   " +
            "              AND a.is_pass = true   " +
            "            GROUP BY c3.criteria_class_3_id   " +
            "        ) AS passed_criteria ON c3.criteria_class_3_id = passed_criteria.criteria_class_3_id   " +
            "        WHERE passed_criteria.num_criteria_detail = (   " +
            "            SELECT COUNT(cd2.criteria_detail_id)   " +
            "            FROM criteria_detail cd2   " +
            "            WHERE cd2.criteria_class_3_id = c3.criteria_class_3_id   " +
            "        )   " +
            "        GROUP BY c2.criteria_class_2_id   " +
            "    ) AS passed_criteria_class_3 ON c2.criteria_class_2_id = passed_criteria_class_3.criteria_class_2_id   " +
            "    WHERE passed_criteria_class_3.num_criteria_class_3 = (   " +
            "        SELECT COUNT(c3.criteria_class_3_id)   " +
            "        FROM criteria_class_3 c3   " +
            "        WHERE c3.criteria_class_2_id = c2.criteria_class_2_id   " +
            "    )   " +
            "    GROUP BY c1.criteria_class_1_id   " +
            ") AS passed_criteria_class_2 ON c1.criteria_class_1_id = passed_criteria_class_2.criteria_class_1_id   " +
            "WHERE passed_criteria_class_2.num_criteria_class_2 = (   " +
            "    SELECT COUNT(c2.criteria_class_2_id)   " +
            "    FROM criteria_class_2 c2   " +
            "    WHERE c2.criteria_class_1_id = c1.criteria_class_1_id   " +
            ") ", nativeQuery = true)
    Float getNumPassClass1(Long userId, int yeat);

}

