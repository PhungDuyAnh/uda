package com.udabe.dto.Dashboard;

import com.udabe.entity.AddressCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddressCodeDTODashboard {

    private List<List<String>> addressName;

    private List<List<AddressCode>> addressCodeList;

//    public AddressCodeDTODashboard(List<String> addressName, List<AddressCode> addressCodeList) {
//        this.addressName = addressName;
//        this.addressCodeList = addressCodeList;
//    }


    public AddressCodeDTODashboard(List<List<String>> addressName, List<List<AddressCode>> addressCodeList) {
        this.addressName = addressName;
        this.addressCodeList = addressCodeList;
    }
}
