package com.udabe.cmmn.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    /*THÔNG BÁO CHO CẬP NHẬT PHIÊN BẢN BỘ CHỈ SỐ:*/
    public static final String criteriaSetApplied = "Bộ chỉ số <span>%s</span> <span>%s</span> phiên bản <span>%s</span> đã được ban hành.";
    public static final String criteriaSetRefused = "Bộ chỉ số <span>%s</span> <span>%s</span> phiên bản <span>%s</span> đã bị thu hồi và thay thế bằng bộ chỉ số <span>%s</span> <span>%s</span> phiên bản <span>%s</span>.";

    /*THÔNG BÁO ĐĂNG KÝ ĐÁNH GIÁ THÀNH CÔNG:*/
    public static final String criteriaSetAppliedSuccess = "Đăng ký bản đánh giá có số hiệu <span>%s</span> thành công.";
    public static final String criteriaSetSuccessLink = "/receive-assessment-results";

    /*THÔNG BÁO KHI HỆ THỐNG TỰ ĐỘNG ĐÁNH GIÁ THÀNH CÔNG VÀ CẬP NHẬT TIẾN TRÌNH ĐIỂM.*/
    public static final String caculateScoreSuccess = "Kết quả tự động đánh giá của bản đăng ký có số hiệu <span>%s</span> hoàn tất. Đã cập nhật tiến trình phát triển và điểm của đô thị.";

    /*Thông báo khi bản đăng ký đánh giá đạt 100%/điểm và gửi lên Hội đồng*/
    public static final String caculateScore100 = "Bản đăng ký đánh giá có số hiệu <span>%s</span> đã đạt 100 điểm và đã được gửi tới Hội đồng đánh giá.";

    /*Thông báo khi hội đồng công nhận bản đăng ký đánh giá và cập nhật tiến trình, điểm, trạng thái đô thị*/
    public static final String recognitionCouncilAgree = "Với bản đăng ký đánh giá có số hiệu <span>%s</span>, đô thị của bạn đã được công nhận là Đô thị thông minh.";

    /*Thông báo khi hội đồng từ chối bản đăng ký đánh giá và cập nhật tiến trình, điểm, trạng thái đô thị*/
    public static final String recognitionCouncilRefuse = "Với bản đăng ký đánh giá có số hiệu <span>%s</span>, đô thị của bạn đã bị từ chối công nhận.";

    /*Thông báo khi được thêm vào hội đồng*/
    public static final String addToCouncil = "Bạn đã được thêm vào hội đồng đánh giá với quyền <span>%s</span>.";

    /*Thông báo khi cập nhật quyền của thành viên trong hội đồng*/
    public static final String updateRoleCouncil = "Quyền trong hội đồng của bạn đã được cập nhật thành <span>%s</span>.";

    /*Thông báo khi tài khoản trong hội đồng bị vô hiệu hoá*/
    public static final String disableCouncilAcc = "Bạn đã bị vô hiệu hóa quyền truy cập trong hội đồng.";

    /*Thông báo khi tài khoản trong hội đồng kích hoạt*/
    public static final String enableCouncilAcc = "Bạn đã được kích hoạt lại quyền truy cập trong hội đồng.";
//---------------------------------
    /*Thông báo khi được thêm vào tham gia đánh giá đô thị*/
    public static final String addToCouncilScore = "Bạn đã được tham gia đánh giá đô thị <span>%s</span> với bản đăng ký đánh giá có số hiệu <span>%s</span>.";

    /*Thông báo khi bị xoá khỏi tham gia đánh giá đô thị*/
    public static final String deleteToCouncilScore = "Bạn đã rời khỏi việc tham gia đánh giá đô thị <span>%s</span> với bản đăng ký đánh giá có số hiệu <span>%s</span>.";

    /*Thông báo khi thành viên hội đồng gửi kết quả đánh giá thành công*/
    public static final String sendCouncilScoreSuccess = "Bạn đã đánh giá thành công bản đăng ký đánh giá có số hiệu <span>%s</span> của đô thị <span>%s</span>.";

    /*Thông báo khi tất cả thành viên hội đồng đánh giá hoàn tất*/
    public static final String completeAllCouncilScore = "Tất cả thành viên hội đồng đã đánh giá xong bản đăng ký đánh giá có số hiệu <span>%s</span> của đô thị <span>%s</span>.";

    /*Thông báo khi có bản đăng ký đánh giá của đô thị đạt điều kiện (100%) và gửi lên hội đồng*/
    public static final String complete100AndSendToCouncil = "Đô thị <span>%s</span> đã đạt đủ điều kiện để hội đồng đánh giá với bản đăng ký đánh giá có số hiệu <span>%s</span>.";

    /*Link*/
    //Đường dẫn tới trang phiên bản hiện hành:
    public static final String criteriaSetVerLink = "/criteria-set-version";

    //Đường dẫn tới trang tình trạng phát triển đô thị:
    public static final String statusUrbanLink = "/receive-assessment-results";

    //Đường dẫn tới trang nhận kết quả đánh giá:
    public static final String receiveResultLink = "/get-assessment-results";

    //Đường dẫn tới trang danh sách đánh giá đô thị:
    public static final String listEvaluateUrban = "/council-evaluate";

}
