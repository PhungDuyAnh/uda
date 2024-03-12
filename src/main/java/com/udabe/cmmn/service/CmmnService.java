package com.udabe.cmmn.service;

import com.udabe.cmmn.converter.SeqNameValue;
import com.udabe.entity.Users;
import com.udabe.repository.UsersRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmmnService {

    private UsersRepository usersRepository;

    @Cacheable(value = "userValue", key = "#userID")
    public SeqNameValue findUserValue(Long userID) {

        SeqNameValue value = new SeqNameValue();
        Optional<Users> usersOptional = usersRepository.findById(userID);

        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            value.setSeq(users.getUserID());
            value.setCodeName(users.getUserName());
            value.setFullName(users.getFullName());
        }

        return value;
    }

}
