package com.udabe.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "address_code")
public class AddressCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_code_id")
    private Long addressCodeId;

    @Column(name = "address_name", length = 55, nullable = false)
    private String addressName;

    @Column(name = "upper_address_seq", nullable = false)
    private Long upperAddressSeq;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;
}
