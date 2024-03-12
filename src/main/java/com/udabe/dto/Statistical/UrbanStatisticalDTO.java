package com.udabe.dto.Statistical;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UrbanStatisticalDTO {

    private double percentPass;

    private double percentNotPass;

    public UrbanStatisticalDTO(double percentPass, double percentNotPass){
        this.percentPass = percentPass;
        this.percentNotPass = percentNotPass;
    }


}
