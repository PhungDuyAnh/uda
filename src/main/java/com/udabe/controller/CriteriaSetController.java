package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.PageParam;
import com.udabe.entity.CouncilScore;
import com.udabe.entity.CriteriaSet;
import com.udabe.entity.EvaluationVersion;
import com.udabe.entity.EvaluationVersionUser;
import com.udabe.service.impl.CriteriaSetServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/criteriaSet")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CriteriaSetController extends BaseCrudController<CriteriaSet, Long> {

    private final CriteriaSetServiceImpl criteriaSetService;

    @Autowired
    public CriteriaSetController(CriteriaSetServiceImpl criteriaSetService) {
        this.criteriaSetService = criteriaSetService;
        super.setService(criteriaSetService);
    }

    /**
     * Controller thêm mới bộ chỉ số.
     */
    @PostMapping("/save")
    public ResponseEntity<?> createCriteriaSet(@RequestBody CriteriaSet criteriaSet) {
        return criteriaSetService.saveCriteriaSet(criteriaSet);
    }


    /**
     * Controller xem chi tiết thông tin bộ chỉ số.
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getCriteriaSetDetail(@PathVariable(value = "id") Long id) {
        return criteriaSetService.findCriteriaSetById(id);
    }


    /**
     * Controller update thông tin bộ chỉ số.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCriteriaSetDetail(@PathVariable(value = "id") Long id, @RequestBody CriteriaSet criteriaSet) {
        return criteriaSetService.updateCriteriaSet(criteriaSet, id);
    }


    /**
     * Controller check validate bộ chỉ số.
     */
    @GetMapping("/checkNullForm")
    public ResponseEntity<?> checkNullForm(@RequestParam Long criteriaSetId) {
        return criteriaSetService.checkNullForm(criteriaSetId);
    }


    /**
     * Controller ban hành phiên bản dự thảo.
     */
    @PutMapping("/updateStatusApplied")
    public ResponseEntity<?> updateStatusApplied(@RequestParam Long criDraftId, @RequestParam String criteriaVersion) {
        return criteriaSetService.updateStatusApplied(criDraftId, criteriaVersion);
    }


    /**
     * Controller thu hồi bản hiện hành và lựa chọn bản dự thảo thay thế.
     */
    @PutMapping("/updateStatusRecall")
    public ResponseEntity<?> updateStatusRecall(@RequestParam Long criDraftId, @RequestParam String criteriaVersion) {
        return criteriaSetService.updateStatusRecall(criDraftId, criteriaVersion);
    }


    /**
     * Controller tìm kiếm, lọc, phân trang phiên bản bộ chỉ số.
     */
    @GetMapping("/servicePaging")
    public ResponseEntity<?> versionCriteriaFilter(CriteriaSet entity, PageParam pageParam) {
        return criteriaSetService.versionCriteriaFilter(entity,pageParam.of());
    }


    /**
     * Controller tìm kiếm các version dự thảo.
     */
    @GetMapping("/allDraft")
    public ResponseEntity<?> selectAllDraft() {
        return criteriaSetService.selectAllDraft();
    }


    /**
     * Controller lấy form đăng kí đánh giá (theo bộ chỉ số hiện hành)
     */
    @GetMapping("/getApplied")
    public ResponseEntity<?> getApplied(@RequestParam Long typeClass, @RequestParam(required = false) Long criteriaClass1Id,
                                        @RequestParam(required = false) Long criteriaSetId,
                                        @RequestParam(required = false) Long evaluationVersionId,
                                        @RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) Long userReceiveId){
        return criteriaSetService.getApplied(typeClass, criteriaClass1Id, criteriaSetId, evaluationVersionId, userId, userReceiveId);
    }


    /**
     * Controller lấy danh sách nhận các kết quả đánh giá của User.
     */
    @GetMapping("/getReceive")
    public ResponseEntity<?> getReceiveForm(EvaluationVersion entity, PageParam pageParam, @RequestParam Long userId){
        return criteriaSetService.getReceiveForm(entity, pageParam.of(), userId);
    }

    /**
     * Controller lấy danh sách nhận các kết quả đánh giá của User.
     */
    @GetMapping("/getReceiveFirst")
    public ResponseEntity<?> getReceiveFormFirst(@RequestParam Long userId){
        return criteriaSetService.getReceiveFormFirst(userId);
    }

    /**
     * Controller xem chi tiết kết quả đánh giá của User:
     */
    @GetMapping("/getReceive/detail/{id}")
    public ResponseEntity<?> getReceiveFormDetail(@PathVariable("id") Long evaluationVersionId){
        return criteriaSetService.getReceiveFormDetail(evaluationVersionId);
    }


    /**
     * Controller lấy danh sách ID câu hỏi (theo bộ chỉ số hiện hành)
     */
    @GetMapping("/getCriteriaDetailIdList")
    public ResponseEntity<?> getCriteriaDetailIdList(@RequestParam(required = false) Long criteriaClass1Id){
        return criteriaSetService.getCriteriaDetailIdList(criteriaClass1Id);
    }

    /**
     * Controller add, xoá thành viên hội đồng vào chấm điểm.
     * @param  evaluationVersionId : Phiên bản chấm điểm.
     * @param  userIds              : ID thành viên hội đồng.
     * @param  type                : "add"(thêm), "delete"(xoá).
     */
    @PostMapping("/addCouncilMark")
    public ResponseEntity<?> addCouncilMark(@RequestParam Long evaluationVersionId, @RequestBody List<Long> userIds,
                                            String type){
        return criteriaSetService.addCouncilMark(evaluationVersionId, userIds, type);
    }


    /**
     * Controller chấm điểm hội đồng.
     * @param  councilScores     : List ID câu hỏi và điểm từng câu.
     */
    @PostMapping("/councilScore")
    public ResponseEntity<?> councilScore(@RequestBody List<CouncilScore> councilScores){
        return criteriaSetService.councilScore(councilScores);
    }


    /**
     * Controller lấy tổng điểm hội đồng chấm theo version phiên bản.
     * @param  evaluationVersionUserId     : ID version.
     */
    @GetMapping("/getCouncilScore")
    public ResponseEntity<?> getCouncilScore(@RequestParam Integer evaluationVersionUserId){
        return criteriaSetService.getCouncilScore(evaluationVersionUserId);
    }


    /**
     * Controller gửi kết quả đánh giá của hội đồng sau khi chấm xong.
     */
    @PostMapping("/sendResultCouncil")
    public ResponseEntity<?> sendResultCouncil(@RequestParam Integer evaluationVersionUserId){
        return criteriaSetService.sendResultCouncil(evaluationVersionUserId);
    }


    /**
     * Controller Công nhận, từ chối(chủ tịch HĐ).
     */
    @PostMapping("/recognitionCouncil")
    public ResponseEntity<?> recognition(@RequestBody EvaluationVersion evaluationVersion, @RequestParam Long evaluationVersionId,
                                         @RequestParam(required = false) Integer evaluationVersionUserId){
        return criteriaSetService.recognitionCouncil(evaluationVersion, evaluationVersionId, evaluationVersionUserId);
    }


    /**
     * Controller nhận kết quả đánh giá(Đô thị).
     */
    @GetMapping("/receiveResult")
    public ResponseEntity<?> receiveResult(@RequestParam Long userID, PageParam pageParam){
        return criteriaSetService.receiveResult(userID, pageParam.of());
    }


    /**
     * Controller chi tiết nhận kết quả đánh giá(đô thị).
     */
    @GetMapping("/detailReceiveResult")
    public ResponseEntity<?> detailReceiveResult(@RequestParam Long userID, @RequestParam Long evaluationVersionId){
        return criteriaSetService.detailReceiveResult(userID, evaluationVersionId);
    }

}
