package com.udabe.service;

import com.udabe.dto.News.NewsClassDTO;
import com.udabe.entity.News;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NewsService {

    ResponseEntity<?> listAllNews(News entity, Pageable pageable);

    ResponseEntity<?> getSingleNews(Long newsId);

    ResponseEntity<?> addNews(News news);

    ResponseEntity<?> updatedNews(Long newsId, News news);

    ResponseEntity<?> checkUnDisplayNews(Long newsId);

    ResponseEntity<?> deleteNews(Long newsId);
}
