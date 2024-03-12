package com.udabe.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDTOAll {

    /**
     * ID tài khoản.
     */
    private Long userID;

    /**
     * Tên tài khoản.
     */
    private String userName;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String fullName;

    /**
     * Email người dùng.
     */
    private String email;

    /**
     * Trạng thái tài khoản:
     * Y : Bị ẩn.
     * N : Không bị ẩn.
     */
    private String disable;

    /**
     * Số điện thoại.
     */
    private String phoneNumber;

    /**
     * Loại tài khoản:
     * 1. Cục phát triển đô thị.
     * 2. Hội đồng đánh giá.
     * 3. Đô thị.
     */
    private Long accountType;

    /**
     * Ngày tạo.
     */
    private LocalDateTime createdAt;


    public UserDTOAll(Long userID, String userName, String fullName, String email,
                      String disable, String phoneNumber, Long accountType, LocalDateTime createdAt) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.disable = disable;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

}
