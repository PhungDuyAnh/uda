package com.udabe.repository;

import com.udabe.dto.News.NewsImageDTO;
import com.udabe.entity.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImage, Long> {

    List<NewsImage> findByGroupId(String groupId);

    @Query("SELECT NEW com.udabe.dto.News.NewsImageDTO(" +
            "ni.newsImageId, ni.imageNm, ni.saveNm, ni.groupId, ni.imageSize, ni.saveDt )" +
            "FROM NewsImage ni " +
            "WHERE ni.groupId = ?1")
    List<NewsImageDTO> listByGroupId(String groupId);
}
