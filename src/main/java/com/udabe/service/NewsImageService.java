package com.udabe.service;

import org.springframework.http.ResponseEntity;

public interface NewsImageService {

    ResponseEntity<?> checkDelete(Long seq);

    ResponseEntity<?> listByGroupId(String groupId);
}
