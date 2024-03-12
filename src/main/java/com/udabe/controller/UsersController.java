package com.udabe.controller;

import com.udabe.cmmn.base.BaseCrudController;
import com.udabe.cmmn.base.PageParam;
import com.udabe.cmmn.base.Response;
import com.udabe.entity.Users;
import com.udabe.payload.request.SignupRequest;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.impl.UsersServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsersController extends BaseCrudController<Users, Long> {

    private final UsersServiceImpl usersService;

    @Autowired
    public UsersController(UsersServiceImpl usersService) {
        this.usersService = usersService;
        super.setService(usersService);
    }


    /**
     * Controller xem chi tiết thông tin tài khoản:
     *
     * @param userId : ID tài khoản.
     * @return Thông tin chi tiết tài khoản.
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailUser(@PathVariable("id") Long userId) {
        return usersService.findById(userId);
    }


    /**
     * Controller tạo mới tài khoản:
     *
     * @return Thông tin tài khoản được tạo mới.
     */
    @PostMapping("/save")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return usersService.registerUser(signUpRequest);
    }


    /**
     * Controller Update tài khoản:
     *
     * @return Thông tin tài khoản được update.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long id, @RequestBody Users user) {
        return usersService.updateUser(user, id);
    }


    /**
     * Controller vô hiệu và kích hoạt tài khoản User:
     *
     * @param disable Tham số kích hoạt tài khoản(Y,N).
     * @return Vô hiệu hoá tài khoản(Chỉ Admin).
     */
    @PostMapping("/disable")
    public ResponseEntity<?> disable(@RequestParam String disable, @RequestParam Long userId) {
        return usersService.disable(disable, userId);
    }


    @GetMapping("/checkLogin")
    public ResponseEntity<?> checkLogin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAnonymous = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        if (isAnonymous) {
            return ResponseEntity.ok(new Response().setData(null).setMessage("Login session has expired!"));
        } else {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return usersService.findById(userDetails.getId());
        }
    }


    /**
     * Controller quên mật khẩu
     * @param email email user
     * @return
     */
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){

        return usersService.forgotPassword(email);
    }


    /**
     * Controller xác thực otp
     * @param email email user
     * @param otp mã xác nhận otp
     * @return
     */
    @PutMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp){
        return usersService.verifyOtp(email, otp);
    }


    /**
     * Controller đặt lại mật khẩu mới cho tài khoản user
     * @param email email user
     * @param newPassword mật khẩu mới cho tài khoản user
     * @return
     */
    @PutMapping("/setNewPassword")
    public ResponseEntity<?> setNewPassword(@RequestParam String email, @RequestHeader String newPassword){
        return usersService.setNewPassword(email, newPassword);
    }


    /**
     * Controller lấy toàn bộ các tình, thành phố trực thuộc trung ương
     * @return
     */
    @GetMapping("/getAllProvince")
    public ResponseEntity<?> getAllProvince(){
        return usersService.getAllProvince();
    }


    /**
     * Controller lấy toàn bộ các quận, huyện, thành phố trực thuộc tỉnh.
     * @param addressCodeId id của quận huyện
     * @return
     */
    @GetMapping("/getAllDistrict")
    public ResponseEntity<?> getAllDistrict(@RequestParam Long addressCodeId){
        return usersService.getAllDistrict(addressCodeId);
    }


    /**
     * Controller lấy danh sách các tài khoản là đô thị(Form quản lý kết quả đánh giá ADMIN).
     * @return  Danh sách các tài khoản là đô thị(Form quản lý kết quả đánh giá ADMIN).
     */
    @GetMapping("/getAllUrbanAcc")
    public ResponseEntity<?> getAllUrbanAcc(Users entity, PageParam pageParam, @RequestParam(required = false) String completeAuto,
                                            @RequestParam(required = false) Long userId){
        return usersService.getAllUrbanAcc(entity, pageParam.of(), completeAuto, userId);
    }


    /**
     * Controller lấy danh sách thành viên hội đồng(đã được thêm).
     */
    @GetMapping("/getAllCouncil")
    public ResponseEntity<?> getAllCouncil(Users entity, PageParam pageParam){
        return usersService.getAllCouncil(entity, pageParam.of());
    }


    /**
     * Controller lấy danh sách thành viên hội đồng trong danh sách được add vào đăng ký.
     * @param type :"add"    : Danh sách các thành viên chưa được thêm.
     *             :"delete" : Danh sách các thành viên đã được thêm.
     */
    @GetMapping("/getAllCouncilScore")
    public ResponseEntity<?> getAllCouncilScore(Users entity, PageParam pageParam, @RequestParam(required = false) Long evaluationVersionId,
                                                @RequestParam(required = false) String type){
        return usersService.getAllCouncilScore(entity, pageParam.of(), evaluationVersionId, type);
    }

    /**
     * Controller lấy danh sách thành viên hội đồng(chưa được thêm)
     */
    @GetMapping("/getAllCouncilNull")
    public ResponseEntity<?> getAllCouncilNull(Users entity, PageParam pageParam){
        return usersService.getAllCouncilNull(entity, pageParam.of());
    }


    /**
     * Controller thêm thành viên vào hội đồng
     */
    @PutMapping("/addToCouncil")
    public ResponseEntity<?> addToCouncil(@RequestBody List<Users> users){
        return usersService.addToCouncil(users);
    }


    /**
     * Controller thay đổi quyền tài khoản hội đồng
     */
    @PutMapping("/updateRoleCouncil/{userId}")
    public ResponseEntity<?> updateRoleCouncil(@PathVariable("userId") Long userId ,@RequestBody Users users){
        return usersService.updateCouncilRole(userId, users);
    }


    /**
     * Controller vô hiệu hóa hội đồng
     */
    @PutMapping("/disableCouncil")
    public ResponseEntity<?> disableCouncil(@RequestParam String disableCouncil, @RequestParam("userId") Long userId,
                                            @RequestParam(required = false) Long newChairmanId){
        return usersService.disableCouncil(disableCouncil, userId, newChairmanId);
    }


    @PutMapping("/updateCouncilType4")
    public ResponseEntity<?> updateCouncilType4(@RequestBody List<Users> users){

        return usersService.updateCouncilType4(users);
    }


    @PutMapping("/updateCouncilType4ToNull")
    public ResponseEntity<?> updateCouncilType4ToNull(@RequestBody List<Users> users){

        return usersService.updateCouncilType4ToNull(users);
    }


    /**
     * Controller lấy danh sách hội đồng chấm điểm phiên bản.
     * @return  Danh sách người dùng chấm điểm phiên bản.
     */
    @GetMapping("/getAllCouncilSend")
    public ResponseEntity<?> getAllCouncilSend(@RequestParam Long evaluationVersionId){
        return usersService.getAllCouncilSend(evaluationVersionId);
    }


    /**
     * Controller sửa thông tin tài khoản
     */
    @PutMapping("/updateAccount/{userId}")
    public ResponseEntity<?> updateAccount(@PathVariable("userId") Long userId, @RequestBody Users users){

        return usersService.updateAccount(userId, users);
    }


    /**
     * Controller thay đổi mật khẩu
     */
    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<?> updatePassword(@PathVariable("userId") Long userId, @RequestParam String oldPass,
                                            @RequestParam String newPassword){

        return usersService.updatePassword(userId, oldPass, newPassword);
    }


}
