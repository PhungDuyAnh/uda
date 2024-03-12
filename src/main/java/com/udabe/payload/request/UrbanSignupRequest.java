package com.udabe.payload.request;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.PostPersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UrbanSignupRequest {

    private String userName;

    private String fullName;

    @NotBlank
    @Email
    private String email;

    private Set<String> role;

    private String password;

    private String phoneNumber;

    private Long accountType;

    /**
     * Thông tin thêm cho loại tài khoản khu đô thị:
     */
    private Long urbanType;

    /**
     * Thông tin về địa danh của tài khoản đô thị.
     */
    private Long addressCodeId;

}
