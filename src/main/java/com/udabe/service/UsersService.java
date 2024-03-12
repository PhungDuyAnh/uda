package com.udabe.service;

import com.udabe.entity.Users;
import com.udabe.payload.request.SignupRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface UsersService {

    ResponseEntity<?> findById(Long userId);

    ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest);

    ResponseEntity<?> updateUser(Users user, Long userId);

    ResponseEntity<?> disable(String disable, Long spaceId);

    ResponseEntity<?> forgotPassword(String email);

    ResponseEntity<?> verifyOtp(String email, String otp);

    ResponseEntity<?> setNewPassword(String email, String newPassword);

    ResponseEntity<?> getAllProvince();

    ResponseEntity<?> getAllDistrict(Long addressCodeId);

    List<String> getRoles();

    ResponseEntity<?> getAllUrbanAcc(Users entity, Pageable pageable, String completeAuto, Long userId);

    ResponseEntity<?> getAllCouncil(Users entity, Pageable pageable);

    ResponseEntity<?> getAllCouncilScore(Users entity, Pageable pageable, Long evaluationVersionId, String type);

    ResponseEntity<?> getAllCouncilNull(Users entity, Pageable pageable);

    ResponseEntity<?> addToCouncil(List<Users> users);

    ResponseEntity<?> updateCouncilRole(Long userId, Users users);

    ResponseEntity<?> updateCouncilType4(List<Users> users);

    ResponseEntity<?> updateCouncilType4ToNull(List<Users> users);

    ResponseEntity<?> disableCouncil(String disableCouncil, Long userId, Long newChairmanId);

    ResponseEntity<?> getAllCouncilSend(Long evaluationVersionId);

    ResponseEntity<?> updateAccount(Long userId, Users users);

    ResponseEntity<?> updatePassword(Long userId, String oldPass, String newPassword);
}
