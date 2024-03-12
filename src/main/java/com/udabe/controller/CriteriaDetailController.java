package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.entity.CriteriaDetail;
import com.udabe.service.impl.CriteriaDetailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/criteriaDetail")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CriteriaDetailController extends BaseCrudController<CriteriaDetail, Long> {

    private final CriteriaDetailServiceImpl criteriaDetailService;

    @Autowired
    public CriteriaDetailController(CriteriaDetailServiceImpl criteriaDetailService) {
        this.criteriaDetailService = criteriaDetailService;
        super.setService(criteriaDetailService);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createCriteriaDetail(@RequestBody CriteriaDetail criteriaDetail) {
        return criteriaDetailService.saveCriteriaDetail(criteriaDetail);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCriteriaClass3(@PathVariable(value = "id") Long id, @RequestBody CriteriaDetail criteriaDetail) {
        return criteriaDetailService.updateCriteriaDetail(criteriaDetail, id);
    }

}
