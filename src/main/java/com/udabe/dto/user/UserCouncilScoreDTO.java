package com.udabe.dto.user;

import com.udabe.service.impl.CriteriaSetServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserCouncilScoreDTO {

    /**
     * ID tài khoản.
     */
    private Long userID;

    private Long userIdReceive;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String fullName;

    /**
     * Tên người dùng, tên đô thị.
     */
    private String userName;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(Chức danh, chức vụ trong tổ chức).
     */
    private String position;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(thuộc tổ chức nào?).
     */
    private String organization;


    /**
     * Điểm số tổng của bản đánh giá hội đồng chấm.
     */
    private Float point;

    /**
     * Trạng thái chấm điểm của thành viên hội đồng.
     * Y  : Đã đánh giá.
     * N  : Chưa đánh giá.
     */
    private String statusEvaluate;

    /**
     * ID bộ chỉ số.
     */
    private Long criteriaSetId;

    /**
     * ID version chấm điểm.
     */
    private Long evaluationVersionId;

    /**
     * ID version chấm điểm User được giao.
     */
    private Integer evaluationVersionUserId;


    public UserCouncilScoreDTO(Long userID, Long userIdReceive, String fullName, String userName, String position, String organization, Float point, String statusEvaluate, Long criteriaSetId, Long evaluationVersionId) {
        this.userID = userID;
        this.userIdReceive = userIdReceive;
        this.fullName = fullName;
        this.userName = userName;
        this.position = position;
        this.organization = organization;
        this.point = point;
        this.statusEvaluate = statusEvaluate;
        this.criteriaSetId = criteriaSetId;
        this.evaluationVersionId = evaluationVersionId;
        this.evaluationVersionUserId = CriteriaSetServiceImpl.findEvaluationVersionUserId2(this.evaluationVersionId, this.userIdReceive);
    }
}
