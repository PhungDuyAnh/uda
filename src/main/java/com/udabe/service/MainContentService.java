package com.udabe.service;

import org.springframework.http.ResponseEntity;

public interface MainContentService {

    ResponseEntity<?> checkDelete(Long seq);

    ResponseEntity<?> listByGroupId(String groupId);
}
