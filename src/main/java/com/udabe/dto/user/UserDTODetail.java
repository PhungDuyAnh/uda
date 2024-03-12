package com.udabe.dto.user;

import com.udabe.entity.Role;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTODetail {

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
     * Thông tin thêm cho loại tài khoản khu đô thị(loại đô thị (loại 1->6)).
     */
    private Long urbanType;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(thuộc tổ chức nào?).
     */
    private String organization;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(Chức danh, chức vụ trong tổ chức).
     */
    private String position;

    /**
     * Ngày tạo.
     */
    private LocalDateTime createdAt;

    /**
     * Ngày cập nhật.
     */
    private LocalDateTime updatedAt;

    private Long roleID;

    private String disableCouncil;

    private Long councilType;

    private List<AddressDetailDTO> addressDetailDTOS ;

    public UserDTODetail(Long userID, String userName, String fullName, String email, String disable,
                         String phoneNumber, Long accountType, Long urbanType, String organization, String position,
                         LocalDateTime createdAt, LocalDateTime updatedAt, Long roleID, String disableCouncil,
                         Long councilType) {
        this.userID = userID;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.disable = disable;
        this.phoneNumber = phoneNumber;
        this.accountType = accountType;
        this.urbanType = urbanType;
        this.organization = organization;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roleID = roleID;
        this.disableCouncil = disableCouncil;
        this.councilType = councilType;
        this.addressDetailDTOS = UsersServiceImpl.findAddressUser(userID);
    }
}
