package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.entity.MainContent;
import com.udabe.entity.NewsImage;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.MainContentRepository;
import com.udabe.service.MainContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainContentServiceImpl extends BaseCrudService<MainContent, Long> implements MainContentService {

    private final MainContentRepository mainContentRepository;

    public MainContentServiceImpl(MainContentRepository mainContentRepository) {
        this.mainContentRepository = mainContentRepository;
    }

    public ResponseEntity<?> select() {
        List<MainContent> result = mainContentRepository.findAll();
        return ResponseEntity.ok(new Response().setDataList(result).setMessage("Successfully!"));
    }

    public ResponseEntity<?> find(Long backgroundId) {
        Optional<MainContent> entity = mainContentRepository.findById(backgroundId);
        if (entity.isPresent()) {
            return ResponseEntity.ok(new Response().setData(entity.get()).setMessage("Found!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> delete(Long seq) {
        mainContentRepository.deleteById(seq);
        return ResponseEntity.ok(new MessageResponse("Deleted!"));
    }

    public ResponseEntity<?> save(MainContent entity) {
        try {
            MainContent saveEntity = mainContentRepository.save(entity);
            return ResponseEntity.ok(new Response().setData(saveEntity).setMessage("Saved!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new MessageResponse("Can't not save entity!"));
        }
    }

    @Override
    public ResponseEntity<?> checkDelete(Long seq) {
        ResponseEntity<?> response = null;
        Optional<MainContent> entity = mainContentRepository.findById(seq);
        if (entity.isPresent()) {
            MainContent file = entity.get();
            file.setGroupId("utk:delete");
            mainContentRepository.save(file);
            response = ResponseEntity.ok(new Response().setMessage("Successfully!"));
        } else {
            response = ResponseEntity.ok(new Response().setMessage("Not found entity!"));
        }
        return response;
    }

    public void updateGroupId(String uploadKey, String groupId) {
        List<MainContent> fileList = mainContentRepository.findByGroupId(uploadKey);
        for (MainContent file : fileList) {
            file.setGroupId(groupId);
            mainContentRepository.save(file);
        }
    }

    @Override
    public ResponseEntity<?> listByGroupId(String groupId) {
        return ResponseEntity.ok(mainContentRepository.findByGroupId(groupId));
    }
}
