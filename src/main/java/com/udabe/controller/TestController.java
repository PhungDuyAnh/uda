package com.udabe.controller;

import com.udabe.cmmn.util.GeoCoderUtil;
import com.udabe.cmmn.util.TimeZoneUtils;
import com.udabe.entity.AddressCode;
import com.udabe.repository.AddressCodeRepository;
import com.udabe.repository.StatisticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final AddressCodeRepository addressCodeRepository;

    @Autowired
    public TestController(AddressCodeRepository addressCodeRepository) {
        this.addressCodeRepository = addressCodeRepository;
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/superAdmin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String superAdminAccess() {
        return "User SUPER_ADMIN.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAcces() {
        return "admin Board.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('SUPER_ADMIN')")
    public String managerAccess() {
        return "manager Board.";
    }

    @GetMapping("/getLongLa")
    public void getLongLa() throws Exception {
//        String addr = "Thành phố Tuyên Quang, Tỉnh Tuyên Quang";
//        com.google.maps.model.LatLng location = GeoCoderUtil.parseLocation(addr);
        List<AddressCode> addressCodeList = addressCodeRepository.findAll();
        int i = 0;
        while (i < addressCodeList.size()) {
            com.google.maps.model.LatLng location = GeoCoderUtil.parseLocation(addressCodeList.get(i).getAddressName());
            addressCodeList.get(i).setLatitude(location.lat);
            addressCodeList.get(i).setLongitude(location.lng);
            addressCodeRepository.save(addressCodeList.get(i));
            i++;
        }

    }
}
