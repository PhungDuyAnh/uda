package com.udabe.dto.Statistical;

import lombok.Data;

import java.security.PrivateKey;
import java.time.LocalDate;

@Data
public class StatisticalDTO {

    /**
     *  Phân loại biểu đồ
     *  line : biểu đồ đường
     *  pie : biểu đồ tròn
     *  area : biểu đồ miền
     */
    private String chartType;

    /**
     * Đơn vị biểu đồ
     * 1: Điểm
     * 2: Phần trăm
     * 3: Số lương
     */
    private Byte unitChart;

    /**
     * Danh sách đô thị
     *
     */
    private Long[] urbanList;

    /**
     * Danh sách năm
     */
    private Number[]  year;



}
