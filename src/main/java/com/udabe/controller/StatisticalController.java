package com.udabe.controller;

import com.udabe.cmmn.base.PageParam;
import com.udabe.dto.Statistical.StatisticalDTO;
import com.udabe.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${apiPrefix}/statistical")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatisticalController {

    private final StatisticalService statisticalService;

    @Autowired
    public StatisticalController(StatisticalService statisticalService) {
        this.statisticalService = statisticalService;
    }

    /**
     * Controller thống kê tình trạng phát triển của 1 đô thị (theo lớp tiêu chí).
     */
    @GetMapping("/urban/statusGrow")
    public ResponseEntity<?> getStatusGrowClass(@RequestParam Long userId, @RequestParam String year) {
        return statisticalService.getStatusGrowClass(userId, year);
    }


    /**
     * Controller lấy danh sách các đô thị của 1 tỉnh/ thành phố trực thuộc Trung Ương
     */
    @GetMapping("/urban/getUrbanList")
    public ResponseEntity<?> getUrbanList(@RequestParam Long addressCodeId, @RequestParam(required = false) String urbanName) {
        return statisticalService.getUrbanList(addressCodeId, urbanName);
    }

    /**
     * Controller Thống kê tình trạng phát triển của 1 đô thị (theo điểm hoặc theo tỉ lệ phần trăm đánh giá tự động).
     */
    @PostMapping("/urban/getUrbanDevStatus")
    public ResponseEntity<?> getStatusGrow(@RequestBody StatisticalDTO statisticalDTO) {
        return statisticalService.getUrbanDevStatus(statisticalDTO);
    }

    /**
     * Controller Thống kê tình trạng đăng ký đánh giá
     */
    @PostMapping("/urban/getUrbanRegisterStatus")
    public ResponseEntity<?> getUrbanRegisterStatus(@RequestBody StatisticalDTO statisticalDTO) {
        return statisticalService.getUrbanRegisterStatus(statisticalDTO);
    }

    /**
     * Controller Tìm kiếm tỉnh thành phố trực thuộc trung ương
     *
     * @return
     */
    @GetMapping("/urban/searchProvince")
    public ResponseEntity<?> searchProvince(@RequestParam(required = false) String provinceName) {
        return statisticalService.searchProvince(provinceName);
    }


    /**
     * Controller Thống kê cơ bản tình trạng phát triển dô thị
     * Tổng số đô thị
     * Số đô thị trên 50 điểm/50%
     * Số đô thị đạt 100 điểm/100%
     *
     * @return
     */
    @GetMapping("/getBasicInfo")
    public ResponseEntity<?> getBasicInfo(@RequestParam(required = true) Byte unitChart) {
        return statisticalService.getBasicInfo(unitChart);
    }

    /**
     * Controller Thống kê cơ bản tình trạng đăng ký đánh giá
     * Tổng số đăng ký đánh giá
     * Số đô lượng đô thị tham gia đăng ký đánh giá
     * Xu hướng phát triển so vơi năm trước
     *
     * @return
     */
    @PostMapping("/getBasicInfoEvaluated")
    public ResponseEntity<?> getBasicInfoEvaluated(@RequestBody StatisticalDTO statisticalDTO) {
        return statisticalService.getBasicInfoEvaluated(statisticalDTO);
    }

    @GetMapping("/getBasicInfoUrban")
    public ResponseEntity<?> getBasicInfoUrban(@RequestParam Byte unitChart, @RequestParam Long userId) {
        return statisticalService.getBasicInfoUrban(unitChart, userId);
    }

    @PostMapping("/getStaUrbanList")
    public ResponseEntity<?> getStaUrbanList(@RequestBody StatisticalDTO statisticalDTO, @RequestParam(required = false) Long addressCodeId, PageParam pageParam) {
        return statisticalService.getStaUrbanList(statisticalDTO, addressCodeId, pageParam.of());
    }


    @GetMapping("/getCriteriaClass1")
    public ResponseEntity<?> getCriteriaClass1() {
        return statisticalService.getCriteriaClass1();
    }

    @GetMapping("/staCriteriaClass1")
    public ResponseEntity<?> getStaCriteriaClass1(@RequestParam(required = true) Long criteriaClass1Id, @RequestParam(required = false) int year) {
        return statisticalService.getStaCriteriaClass1(criteriaClass1Id, year);
    }

    @GetMapping("/staCriteriaClass2")
    public ResponseEntity<?> getStaCriteriaClass2(@RequestParam(required = true) Long criteriaClass2Id, @RequestParam(required = false) int year) {
        return statisticalService.getStaCriteriaClass2(criteriaClass2Id, year);
    }

    @GetMapping("/staCriteriaClass3")
    public ResponseEntity<?> getStaCriteriaClass3(@RequestParam(required = true) Long criteriaClass3Id, @RequestParam(required = false) int year) {
        return statisticalService.getStaCriteriaClass3(criteriaClass3Id, year);
    }

    @GetMapping("/staCriteriaDetail")
    public ResponseEntity<?> getStaCriteriaDetail(@RequestParam(required = true) Long criteriaDetailId, @RequestParam(required = false) int year) {
        return statisticalService.getStaCriteriaDetail(criteriaDetailId, year);
    }
}
