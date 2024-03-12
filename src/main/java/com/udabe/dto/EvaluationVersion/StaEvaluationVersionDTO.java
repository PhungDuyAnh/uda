package com.udabe.dto.EvaluationVersion;

import com.udabe.dto.user.AddressDetailDTO;
import com.udabe.service.impl.UsersServiceImpl;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public interface StaEvaluationVersionDTO {

    public Long getUserID();

    public String getFullName();

    public float getSumScore();

    public float getSumPercent();

    public String getDistrict();

    public String getProvince();

}
