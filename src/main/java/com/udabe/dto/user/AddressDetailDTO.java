package com.udabe.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDetailDTO {
    private Long addressCodeId;

    private String addressName;

    private Long upperSeq;

    public AddressDetailDTO(Long addressCodeId, String addressName, Long upperSeq){
        this.addressCodeId = addressCodeId;
        this.addressName = addressName;
        this.upperSeq = upperSeq;
    }
}
