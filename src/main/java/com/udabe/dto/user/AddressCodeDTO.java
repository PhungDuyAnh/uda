package com.udabe.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressCodeDTO {

    private Long addressCodeId;

    private String addressName;

    public AddressCodeDTO (Long addressCodeId, String addressName){
        this.addressCodeId = addressCodeId;
        this.addressName = addressName;
    }

}
