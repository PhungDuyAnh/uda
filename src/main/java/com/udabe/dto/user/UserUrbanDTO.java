package com.udabe.dto.user;

import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserUrbanDTO {

    /**
     * ID tài khoản.
     */
    private Long userID;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String fullName;

    /**
     * Trạng thái khu đô thị.
     */
    private Integer urbanStatus;

    /**
     * Thời gian cập nhật.
     */
    private LocalDateTime updatedAt;


    private List<AddressDetailDTO> addressDetailDTOS ;


    public UserUrbanDTO(Long userID, String fullName, Integer urbanStatus, LocalDateTime updatedAt) {
        this.userID = userID;
        this.fullName = fullName;
        this.urbanStatus = urbanStatus;
        this.updatedAt = updatedAt;
        this.addressDetailDTOS = UsersServiceImpl.findAddressUser(userID);
    }

}
