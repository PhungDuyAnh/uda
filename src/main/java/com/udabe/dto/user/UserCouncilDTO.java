package com.udabe.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserCouncilDTO {
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

    private String disableCouncil;

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

    private Long councilType;

    private String organization;

    private String position;

    /**
     * Ngày tạo.
     */
    private LocalDateTime createdAt;

    public UserCouncilDTO(Long userID, String userName, String fullName, String email, String disable,
                          String disableCouncil, String phoneNumber, Long accountType, Long councilType,
                          String organization, String position, LocalDateTime createdAt) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.disable = disable;
        this.disableCouncil = disableCouncil;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.councilType = councilType;
        this.organization = organization;
        this.position = position;
        this.createdAt = createdAt;
    }
}
