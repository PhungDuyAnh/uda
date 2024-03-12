package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udabe.cmmn.base.BaseEntity;

import com.udabe.payload.request.SignupRequest;
import com.udabe.socket.Notify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    /**
     * Tên tài khoản.
     */
    @Column(name = "user_name", length = 55, unique = true)
    private String userName;

    /**
     * Tên người dùng, tên đô thị.
     */
    @Column(name = "full_name", length = 55)
    private String fullName;

    /**
     * Mật khẩu tài khoản.
     */
    @Size(max = 120)
    @Column(name = "password")
    private String password;

    /**
     * Email người dùng.
     */
    @Column(name = "email", length = 55, unique = true)
    private String email;

    /**
     * Trạng thái tài khoản:
     * Y : Bị ẩn.
     * N : Không bị ẩn.
     */
    @Column(name = "disable", columnDefinition = "varchar(1) default 'N'")
    private String disable;

    /**
     * Trạng thái tài khoản hội đồng( áp dụng với các chức năng hội đồng)
     * N: Đang hoạt động
     * Y: Vô hiệu hóa
     */
    @Column(name = "disable_council", columnDefinition = "varchar(1) default 'N'")
    private String disableCouncil;

    /**
     * Số điện thoại.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Loại tài khoản:
     * 1. Cục phát triển đô thị.
     * 2. Hội đồng đánh giá.
     * 3. Đô thị.
     */
    @Column(name = "account_type")
    private Long accountType;

    /**
     * Thông tin thêm cho loại tài khoản khu đô thị(loại đô thị (loại 1->6)).
     */
    @Column(name = "urban_type")
    private Long urbanType;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng:
     * 1. Chủ tịch hội đồng
     * 2. Thư kí hội đồng
     * 3. Thành viên hội đồng
     */
    @Column(name = "council_type")
    private Long councilType;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(thuộc tổ chức nào?).
     */
    @Column(name = "organization", length = 255)
    private String organization;

    /**
     * Thông tin thêm cho loại tài khoản hội đồng(Chức danh, chức vụ trong tổ chức).
     */
    @Column(name = "position", length = 255)
    private String position;

    /**
     * Thông tin về địa danh của tài khoản đô thị.
     */
    @Column(name = "address_code_id")
    private Long addressCodeId;

    /**
     * Trạng thái khu đô thị.
     * 1. Đang phát triển.
     * 2. Đô thị thông minh.
     */
    @Column(name = "urban_status")
    private Integer urbanStatus;

    /**
     * Trạng thái đánh giá khu đô thị của hội đồng.
     * N  : Hội đồng đã đánh giá.
     * Y  : Hội đồng chưa đánh giá.
     */
    @Column(name = "status_council", columnDefinition = "varchar(1) default 'N'")
    private String statusCouncil;

    /**
     * Trạng thái xác thực mã OTP:
     * N: Chưa xác thực
     * Y: Đã xác thực
     */
    @Column(columnDefinition = "varchar(1) default 'N'")
    private String isVerify;

    /**
     * Mã OTP
     */
    private String otp;

    /**
     * Thời gian tạo ra mã OTP
     */
    private LocalDateTime otpGeneratedTime;

    @Column(columnDefinition = "int default 0")
    private Integer countNotify;

    @Transient
    private String versionName;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<EvaluationVersion> evaluationVersions;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<EvaluationVersionUser> evaluationVersionUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notify> notifies;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<UserFunction> userFunctions;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<News> news;


    public Users() {
    }

    public Users(String username, String email, String password) {
        this.userName = username;
        this.email = email;
        this.password = password;
    }

    public void setFieldsFromRequest(SignupRequest request, PasswordEncoder encoder) {
        this.userName = request.getUserName();
        this.email = request.getEmail();
        this.password = encoder.encode(request.getPassword());
        this.fullName = request.getFullName();
        this.phoneNumber = request.getPhoneNumber();
        this.urbanType = request.getUrbanType();
        this.councilType = request.getCouncilType();
        this.organization = request.getOrganization();
        this.position = request.getPosition();
        this.accountType = request.getAccountType();
        this.addressCodeId = request.getAddressCodeId();
    }

    @Override
    public Long getSeq() {
        return this.userID;
    }

    @PostPersist
    public void setDefaultValue() {
        this.disable = "N";
        this.isVerify = "N";
        this.disableCouncil = "N";
        this.statusCouncil = "N";
        this.countNotify = 0;
    }

}