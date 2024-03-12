package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.BaseEntity;
import com.udabe.cmmn.base.ParamMap;
import com.udabe.dto.News.NewsClassDTO;
import com.udabe.dto.News.NewsClassDetailDTO;
import com.udabe.dto.News.NewsDTO;
import com.udabe.entity.News;
import com.udabe.entity.Users;
import com.udabe.repository.NewsRepository;
import com.udabe.repository.UsersRepository;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.NewsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsServiceImpl extends BaseCrudService<News, Long> implements NewsService {

    private static NewsRepository newsRepository;

    private final UsersRepository usersRepository;

    private final NewsImageServiceImpl newsImageServiceImpl;

    private final MainContentServiceImpl mainContentServiceImpl;

    public NewsServiceImpl(NewsRepository newsRepository, UsersRepository usersRepository, NewsImageServiceImpl newsImageServiceImpl, MainContentServiceImpl mainContentServiceImpl) {
        this.newsRepository = newsRepository;
        this.usersRepository = usersRepository;
        this.newsImageServiceImpl = newsImageServiceImpl;
        this.mainContentServiceImpl = mainContentServiceImpl;
    }


    @Override
    public ResponseEntity<?> listAllNews(News entity, Pageable pageable) {

        ParamMap param = ParamMap.init(entity, pageable);
        Integer newsType = param.getInteger("newsType");
        String newsTitle = param.getString("newsTitle");
        String isDisplay = param.getString("isDisplay");
        String fullName = param.getString("fullName");
        String searchDate = param.getString("searchDate");
        LocalDateTime timeMinOfDay = null;
        LocalDateTime timeMaxOfDay = null;
        if (pageable.getSort() == Sort.unsorted()) {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");
            Sort sort = Sort.by(order);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        if (!StringUtils.isEmpty(searchDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeMinOfDay = LocalDate.parse(searchDate, formatter).atStartOfDay();
            timeMaxOfDay = LocalDate.parse(searchDate, formatter).atTime(LocalTime.MAX);
        }
        Page<NewsClassDTO> listData = newsRepository.ListAllNews(newsType, newsTitle, isDisplay, fullName, timeMinOfDay,
                timeMaxOfDay, pageable);
        List<NewsClassDTO> news = listData.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("news", news);
        response.put("currentPage", listData.getNumber());
        response.put("totalItems", listData.getTotalElements());
        response.put("totalPages", listData.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getSingleNews(Long newsId) {
        NewsClassDetailDTO data = newsRepository.getSingleNews(newsId);
        return ResponseEntity.ok(data);
    }


    @Override
    public ResponseEntity<?> addNews(News news) {
        Users userFind = usersRepository.getById(getUserLoginId());
        news.setUsers(userFind);
        news.setUpdatedBy(null);
        newsRepository.save(news);
        upLoadFileImage(news);
        upLoadFileMainContent(news);
        return ResponseEntity.ok("Success");
    }


    @Override
    public ResponseEntity<?> updatedNews(Long newsId, News news) {

        News newsFind = newsRepository.getById(newsId);
        if(newsFind != null){
            if(news != null) {
                newsFind.setNewsTitle(news.getNewsTitle());
                newsFind.setNewsType(news.getNewsType());
                newsFind.setGeneralContent(news.getGeneralContent());
                news.setUsers(newsFind.getUsers());
                newsFind.setUpdatedBy(getUserLoginId());
                newsFind.setIsDisplay(news.getIsDisplay());
                newsRepository.save(newsFind);
                upLoadFileImage(news);
                upLoadFileMainContent(news);
                return ResponseEntity.ok("Update success");
            }
            return ResponseEntity.badRequest().body("Null value");
        }
        return ResponseEntity.badRequest().body("User not found");
    }


    @Override
    public ResponseEntity<?> checkUnDisplayNews(Long userId) {

        List<NewsDTO> listData = newsRepository.ListUnDisplay(userId);
        return ResponseEntity.ok(listData);
    }


    @Override
    public ResponseEntity<?> deleteNews(Long newsId) {

        newsRepository.deleteById(newsId);
        return ResponseEntity.ok("Deleted");
    }


    public void upLoadFileImage(News news) {
        //FILE:
        String uploadKey = news.getUploadKey();
        if (StringUtils.isNotEmpty(uploadKey)) {
            String groupId = String.format("%s-%s", news.getClass().getSimpleName(), news.getNewsId());
            newsImageServiceImpl.updateGroupId(uploadKey, groupId);
        }
    }

    public void upLoadFileMainContent(News news) {
        //FILE:
        String uploadKey = news.getUploadKey();
        if (StringUtils.isNotEmpty(uploadKey)) {
            String groupId = String.format("%s-%s", news.getClass().getSimpleName(), ((BaseEntity) news).getSeq());
            mainContentServiceImpl.updateGroupId(uploadKey, groupId);
        }
    }


    public static String getUpdateFullName(Long userID){

        return newsRepository.updateFullName(userID);
    }


    public static Long getUserLoginId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    public static Long getNewImageId(Long newsId){

        return newsRepository.getNewImageId(newsId);
    }

    public static Long getContentId(Long contentId){

        return newsRepository.getContentId(contentId);
    }
}
