package com.udabe.payload.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

    private String userName;

    private String fullName;

    @NotBlank
//    @Size(max = 50)
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


    private Long councilType;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng:
     */
    private String organization;

    private String position;

    /**
     * Thông tin về địa danh của tài khoản đô thị.
     */
    private Long addressCodeId;

}
