package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.Response;
import com.udabe.entity.NewsImage;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.NewsImageRepository;
import com.udabe.service.NewsImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsImageServiceImpl extends BaseCrudController<NewsImage, Long> implements NewsImageService {

    private final NewsImageRepository newsImageRepository;

    public NewsImageServiceImpl(NewsImageRepository newsImageRepository) {
        this.newsImageRepository = newsImageRepository;
    }

    public ResponseEntity<?> select() {
        List<NewsImage> result = newsImageRepository.findAll();
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

    public ResponseEntity<?> find(Long backgroundId) {
        Optional<NewsImage> entity = newsImageRepository.findById(backgroundId);
        if (entity.isPresent()) {
            return ResponseEntity.ok(new Response().setData(entity.get()).setMessage("Found!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> delete(Long seq) {
        newsImageRepository.deleteById(seq);
        return ResponseEntity.ok(new MessageResponse("Deleted!"));
    }

    public ResponseEntity<?> save(NewsImage entity) {
        try {
            NewsImage saveEntity = newsImageRepository.save(entity);
            return ResponseEntity.ok(new Response().setData(saveEntity).setMessage("Saved!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new MessageResponse("Can't not save entity!"));
        }
    }

    @Override
    public ResponseEntity<?> checkDelete(Long seq) {
        ResponseEntity<?> response = null;
        Optional<NewsImage> entity = newsImageRepository.findById(seq);
        if (entity.isPresent()) {
            NewsImage file = entity.get();
            file.setGroupId("utk:delete");
            newsImageRepository.save(file);
            response = ResponseEntity.ok(new Response().setMessage("Successfully!"));
        } else {
            response = ResponseEntity.ok(new Response().setMessage("Not found entity!"));
        }
        return response;
    }

    @Override
    public ResponseEntity<?> listByGroupId(String groupId) {
        return ResponseEntity.ok(newsImageRepository.findByGroupId(groupId));
    }

    public void updateGroupId(String uploadKey, String groupId) {
        List<NewsImage> fileList = newsImageRepository.findByGroupId(uploadKey);
        for (NewsImage file : fileList) {
            file.setGroupId(groupId);
            newsImageRepository.save(file);
        }
    }
}
