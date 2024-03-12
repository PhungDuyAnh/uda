package com.udabe.controller;

import com.udabe.dto.criteria.CriteriaClassDTO;
import com.udabe.entity.CriteriaClass1;
import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.service.impl.CriteriaClass1ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/criteriaClass")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CriteriaClass1Controller extends BaseCrudController<CriteriaClass1, Long> {

    private final CriteriaClass1ServiceImpl criteriaClass1Service;

    @Autowired
    public CriteriaClass1Controller(CriteriaClass1ServiceImpl criteriaClass1Service) {
        this.criteriaClass1Service = criteriaClass1Service;
        super.setService(criteriaClass1Service);
    }


    /**
     * Controller preview bộ chỉ số.
     * @return : Thông tin chi tiết toàn bộ bộ chỉ số.
     */
    @GetMapping("/preview")
    public ResponseEntity<?> previewAllClass(Long criteriaSetId) {
        return criteriaClass1Service.previewAllClass(criteriaSetId);
    }


    /**
     * Controller tìm bộ chỉ số theo ID lớp cha.
     * @param parentId :    ID lớp cha.
     * @param classNumber : Số hiệu class.
     */
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCriteriaClass(@RequestParam Long parentId, @RequestParam Long classNumber) {
        return criteriaClass1Service.getAllCriteriaClass(parentId, classNumber);
    }


    /**
     * Controller xem chi tiết bộ chỉ số theo số hiệu class.
     * @param id :          ID.
     * @param classNumber : Số hiệu class.
     * @return :            Thông tin chi tiết bộ chỉ số.
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findcriteriaClassById(@PathVariable(value = "id") Long id, @RequestParam Long classNumber) {
        return criteriaClass1Service.findCriteriaClassById(id, classNumber);
    }

    /**
     * Controller Update bộ chỉ số.
     * @return : Response bộ chỉ số Update.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCriteriaClass(@PathVariable(value = "id") Long id,
                                                  @RequestBody CriteriaClassDTO criteriaClassDTO) {
        return criteriaClass1Service.updateCriteriaClass(criteriaClassDTO, id);
    }

    /**
     * Controller Thêm bộ chỉ số.
     * @return : Response bộ chỉ số được thêm mới.
     */
    @PostMapping("/save")
    public ResponseEntity<?> createCriteriaClass(@RequestBody CriteriaClass1 criteriaClass1) {
        return criteriaClass1Service.saveCriteriaClass1(criteriaClass1);
    }

    /**
     * Controller Xoá bộ chỉ số theo số hiệu class .
     * @return : Xoá bộ chỉ số.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCriteriaClass(@PathVariable(value = "id") Long id, @RequestParam Long classNumber) {
        return criteriaClass1Service.deleteCriteriaClass(id, classNumber);
    }

}
