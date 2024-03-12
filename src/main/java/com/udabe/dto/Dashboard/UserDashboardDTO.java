package com.udabe.dto.Dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDashboardDTO {

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
     * Trạng thái tài khoản:
     * Y : Bị ẩn.
     * N : Không bị ẩn.
     */
    private String disable;

    /**
     * Loại tài khoản:
     * 1. Cục phát triển đô thị.
     * 2. Hội đồng đánh giá.
     * 3. Đô thị.
     */
    private Long accountType;

    public UserDashboardDTO(Long userID ,String userName, String fullName, String disable, Long accountType) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.disable = disable;
        this.accountType = accountType;
    }
}
