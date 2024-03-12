package com.udabe.cmmn.base;

import lombok.Data;

@Data
public class ResponseChart {

    private Long userID;

    private String fullName;

    private Float[] data;

    private int[] dataInt;

    private String color;

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setData(Float[] data) {
        this.data = data;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
