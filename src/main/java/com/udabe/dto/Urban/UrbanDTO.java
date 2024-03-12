package com.udabe.dto.Urban;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UrbanDTO {
    private Long userID;

    private String fullName;

    public UrbanDTO(Long userID, String fullName) {
        this.userID = userID;
        this.fullName = fullName;
    }
}
