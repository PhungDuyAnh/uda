package com.udabe.repository;

import com.udabe.dto.News.NewsClassDTO;
import com.udabe.dto.News.NewsClassDetailDTO;
import com.udabe.dto.News.NewsDTO;
import com.udabe.dto.user.UserDTOAll;
import com.udabe.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("select new com.udabe.dto.News.NewsClassDTO(" +
            " n.newsId, n.newsTitle, n.newsType, n.generalContent, n.isDisplay, u.userID, u.fullName, n.createdAt, n.updatedAt ) " +
            "FROM News n " +
            "JOIN n.users u " +
            "WHERE (:newsType IS NULL OR n.newsType= :newsType) " +
            "AND (:newsTitle IS NULL OR n.newsTitle like CONCAT('%', :newsTitle, '%')) " +
            "AND (:isDisplay IS NULL OR n.isDisplay= :isDisplay) " +
            "AND (:fullName IS NULL OR u.fullName like CONCAT('%', :fullName, '%')) " +
            "AND (:timeMinOfDay IS NULL OR n.createdAt >= :timeMinOfDay) " +
            "AND (:timeMaxOfDay IS NULL OR n.createdAt <= :timeMaxOfDay) ")
    Page<NewsClassDTO> ListAllNews(@Param("newsType") Integer newsType,
                                   @Param("newsTitle") String newsTitle,
                                   @Param("isDisplay") String isDisplay,
                                   @Param("fullName") String fullName,
                                   @Param("timeMinOfDay") LocalDateTime timeMinOfDay,
                                   @Param("timeMaxOfDay") LocalDateTime timeMaxOfDay,
                                   Pageable pageable);


    @Query("select new com.udabe.dto.News.NewsClassDetailDTO(" +
            "n.newsId, n.newsTitle, n.newsType, n.generalContent, u.userID, u.fullName, n.updatedBy, n.isDisplay, n.createdAt, n.updatedAt  )" +
            "FROM News n " +
            "JOIN n.users u " +
            "WHERE n.newsId = ?1 ")
    NewsClassDetailDTO getSingleNews(Long newsId);

    @Query(nativeQuery = true,
    value = "SELECT n.news_id as newsId, n.news_title as newsTitle, n.news_type as newsType, n.general_content as generalContent, " +
            "u.user_id as userID, u.user_name as userName, u.full_name as fullName, n.is_display as isDisplay ,n.created_at as createdAt, n.updated_at as updatedAt " +
            "FROM news n " +
            "JOIN users u on n.user_id = u.user_id " +
            "WHERE n.is_display = 'N' AND u.user_id = ?1 ")
    List<NewsDTO> ListUnDisplay(Long userId);

    @Query(nativeQuery = true,
    value = "SELECT u.full_name as fullName " +
            "FROM users u " +
            "WHERE u.user_id = ?1")
    String updateFullName(Long userID);

    @Query(nativeQuery = true,
    value = "SELECT ni.news_image_id  " +
            "FROM news_image ni " +
            "WHERE ni.group_id not like 'utk:%' AND ni.group_id like %?1% ")
    Long getNewImageId(Long newsId);

    @Query(nativeQuery = true,
    value = "SELECT mc.main_content_id  " +
            "FROM main_content mc " +
            "WHERE mc.group_id not like 'utk:%' AND mc.group_id like %?1%")
    Long getContentId(Long contentId);

}
