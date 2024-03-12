package com.udabe.service.impl;

import com.google.common.base.CaseFormat;
import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.ParamMap;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.util.EmailUtil;
import com.udabe.dto.Dashboard.DashboardAddressDTO;
import com.udabe.dto.user.*;
import com.udabe.entity.*;
import com.udabe.payload.request.SignupRequest;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.AddressCodeRepository;
import com.udabe.repository.RoleRepository;
import com.udabe.repository.UserRoleRepository;
import com.udabe.repository.UsersRepository;
import com.udabe.security.jwt.JwtUtils;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.UsersService;

import com.udabe.socket.NotifiServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;


@Service
public class UsersServiceImpl extends BaseCrudService<Users, Long> implements UsersService {

    private static UsersRepository usersRepository;

    private static AddressCodeRepository addressCodeRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final EmailUtil emailUtil;

    private final PasswordEncoder encoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserRoleRepository userRoleRepository,
                            RoleRepository roleRepository, EmailUtil emailUtil, PasswordEncoder encoder,
                            AddressCodeRepository addressCodeRepository) {
        UsersServiceImpl.usersRepository = usersRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.emailUtil = emailUtil;
        this.encoder = encoder;
        UsersServiceImpl.addressCodeRepository = addressCodeRepository;
        super.setRepository(usersRepository);
    }


    @Override
    public ResponseEntity<?> paging(Users entity, Pageable pageable) {
        ParamMap param = ParamMap.init(entity, pageable);
        String disable = param.getString("disable");
        Long accountType = param.getLong("accountType");
        String userName = param.getString("userName");
        Page<UserDTOAll> dataList = usersRepository.selectAllUsers(disable, accountType, userName, pageable);
        List<UserDTOAll> users = dataList.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> findById(Long userId) {
        Optional<Users> entity = usersRepository.findById(userId);
        if (entity.isPresent()) {
            return ResponseEntity.ok(new Response().setData(usersRepository.findByIdDTO(userId)).setMessage("Found!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + userId));
        }
    }


    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (usersRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: Tên người dùng đã được sử dụng!"));
        }

        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: Email đã được sử dụng!"));
        }

        if (signUpRequest.getAddressCodeId() != null) {
            if (usersRepository.existsByAddressCodeId(signUpRequest.getAddressCodeId())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Địa điểm của tài khoản đô thị đã được đăng ký!"));
            }
        }
        if (signUpRequest.getPhoneNumber() != "") {
            String[] phone = usersRepository.selectPhoneNumber(signUpRequest.getPhoneNumber());
            if (phone.length > 0) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Số điện thoại đã được sử dụng!"));
            }
        }


        // Create new user's account:
        Users user = new Users();
        user.setFieldsFromRequest(signUpRequest, encoder);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_URBAN)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
                        roles.add(adminRole);

                        break;
                    case "develop":
                        Role developRole = roleRepository.findByRoleName(ERole.ROLE_DEVELOP)
                                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
                        roles.add(developRole);

                        break;
                    case "manager-uda":
                        Role expertRole = roleRepository.findByRoleName(ERole.ROLE_MANAGER_UDA)
                                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
                        roles.add(expertRole);

                        break;
                    case "assessor":
                        Role assessorRole = roleRepository.findByRoleName(ERole.ROLE_ASSESSOR)
                                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
                        roles.add(assessorRole);

                        break;
                    case "urban":
                        Role urbanRole = roleRepository.findByRoleName(ERole.ROLE_URBAN)
                                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền."));
                        roles.add(urbanRole);

                        break;
                }
            });
        }

        Users userSave = usersRepository.save(user);
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(userSave);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return ResponseEntity.ok(new MessageResponse("Tạo tài khoản thành công!"));
    }


    @Override
    public ResponseEntity<?> updateUser(Users user, Long userId) {

        Optional<Users> userFind = usersRepository.findById(userId);
        if (userFind.isPresent()) {
            Long addressCodeId = user.getAddressCodeId();

            if (addressCodeId != null) {
                if (usersRepository.existsByAddressCodeIdAndIdNot(addressCodeId, userId)) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Lỗi: Địa điểm của tài khoản đô thị đã được đăng ký!"));
                }
            }
            if (user.getPhoneNumber() != "") {
                String[] phone = usersRepository.selectPhoneNumberByUserId(user.getPhoneNumber(), userId);
                if (phone.length > 0) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Lỗi: Số điện thoại đã được sử dụng!"));
                }
            }
            String[] userNames = usersRepository.checkUserName(user.getUserName(), userId);
            if (userNames.length > 0) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Tên người dùng đã được sử dụng!"));
            }
            String[] mails = usersRepository.checkByEmail(user.getEmail(), userId);
            if (mails.length > 0) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Email đã được sử dụng!"));
            }
            user.setPassword(userFind.get().getPassword());
            user.setUrbanStatus(userFind.get().getUrbanStatus());
            user.setIsVerify(userFind.get().getIsVerify());
            user.setCountNotify(userFind.get().getCountNotify());
            user.setStatusCouncil(userFind.get().getStatusCouncil());
            user.setDisableCouncil(userFind.get().getDisableCouncil());
            usersRepository.save(user);
            UserRole userRole = userRoleRepository.findByUser(userFind.get());
            ERole targetRole = mapTypeRoleMapping().get(user.getAccountType());
            userRole.setRole(roleRepository.findByRoleName(targetRole).orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy quyền.")));
            userRoleRepository.save(userRole);
            return ResponseEntity.ok("Cập nhật tài khoản thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Lỗi: Không thể lưu thông tin!");
        }
    }


    public ResponseEntity<?> disable(String disable, Long userId) {
        Optional<Users> entity = usersRepository.findById(userId);
        if (entity.isPresent()) {
            usersRepository.disable(disable, userId);
            return ResponseEntity.ok("Vô hiệu hóa tài khoản thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Lỗi: Không tìm thấy tài khoản với ID: " + userId));
        }
    }


    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy Email"));
        try {
            emailUtil.setPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Thất bại");
        }
        return ResponseEntity.ok("Vui lòng kiểm tra email của bạn");
    }


    public static Map<Long, ERole> mapTypeRoleMapping() {
        Map<Long, ERole> accountTypeRoleMapping = new HashMap<>();
        accountTypeRoleMapping.put(1L, ERole.ROLE_MANAGER_UDA);
        accountTypeRoleMapping.put(2L, ERole.ROLE_ASSESSOR);
        accountTypeRoleMapping.put(3L, ERole.ROLE_URBAN);
        return accountTypeRoleMapping;
    }


    @Override
    public ResponseEntity<?> verifyOtp(String email, String otp) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy email"));

        String createdOtp = users.getOtp();
        Long otpDuration = Duration.between(users.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds();

        if (createdOtp.equals(otp) && otpDuration < 600) {
            users.setIsVerify("Y");
            usersRepository.save(users);
            return ResponseEntity.ok("Xác nhận thành công");
        } else if (createdOtp.equals(otp) && otpDuration >= 600) {
            users.setIsVerify("N");
            return ResponseEntity.badRequest().body("Lỗi: Mã xác minh đã hết hạn");
        } else {
            return ResponseEntity.badRequest().body("Lỗi: Mã xác minh không hợp lệ");
        }
    }


    @Override
    public ResponseEntity<?> setNewPassword(String email, String newPassword) {

        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy email"));

        String verifyOtp = users.getIsVerify();
        Long otpDuration = Duration.between(users.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds();

        if (verifyOtp.equals("Y") && otpDuration < 600) {
            users.setPassword(encoder.encode(newPassword));
            users.setIsVerify("N");
            usersRepository.save(users);
            return ResponseEntity.ok("Thay đổi mật khẩu thành công");
        } else if (verifyOtp.equals("Y") && otpDuration >= 600) {
            users.setIsVerify("N");
            return ResponseEntity.badRequest().body("Lỗi: Mã xác minh đã hết hạn");
        } else {
            users.setIsVerify("N");
            return ResponseEntity.badRequest().body("Thay đổi mật khẩu thất bại");
        }
    }


    @Override
    public ResponseEntity<?> getAllProvince() {
        List<AddressCodeDTO> addressCodeDTOS = addressCodeRepository.findAllProvince();
        return new ResponseEntity<>(addressCodeDTOS, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAllDistrict(Long addressCodeId) {
        List<AddressCodeDTO> addressCodeDTOS = addressCodeRepository.findAllDistrict(addressCodeId);
        return new ResponseEntity<>(addressCodeDTOS, HttpStatus.OK);
    }


    public static List<AddressDetailDTO> findAddressUser(Long userId) {
        Long addressDistrict = usersRepository.findAddressUser(userId);
        Long addressProvince = addressCodeRepository.findAddressProvince(addressDistrict);
        List<AddressDetailDTO> addressDetailDTOS = addressCodeRepository.findAddressUserDetail(addressDistrict, addressProvince);
        return addressDetailDTOS;
    }

    public static List<DashboardAddressDTO> findAddressUserDashboard(Long userId) {
        Long addressDistrict = usersRepository.findAddressUser(userId);
        Long addressProvince = addressCodeRepository.findAddressProvince(addressDistrict);
        List<DashboardAddressDTO> DashboardAddressDTOS = addressCodeRepository.findAddressUserDetailDashboard(addressDistrict, addressProvince);
        return DashboardAddressDTOS;
    }

    public static String findDistrictDashboard(Long userId) {
        Long addressDistrictId = usersRepository.findAddressUser(userId);
        String addressDistrict = addressCodeRepository.findNameById(addressDistrictId);
        return addressDistrict;
    }

    public static String findProvinceDashboard(Long userId) {
        Long addressDistrictId = usersRepository.findAddressUser(userId);
        String addressDistrict = addressCodeRepository.findNameById(addressDistrictId);
        Long addressProvinceId = addressCodeRepository.findAddressProvince(addressDistrictId);
        String addressProvince = addressCodeRepository.findNameById(addressProvinceId);
        return addressProvince;
    }


    @Override
    public List<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> result = new ArrayList<>();
        List<UserRole> lstFindRole = userRoleRepository.getRoles(userDetails.getId());
        for (UserRole x : lstFindRole) {
            result.add(roleRepository.findById(x.getRole().getRoleID()).get().getRoleName().toString());
        }
        return result;
    }


    @Override
    public ResponseEntity<?> getAllUrbanAcc(Users entity, Pageable pageable, String completeAuto, Long userId) {
        ParamMap param = ParamMap.init(entity, pageable);
        String column = null;
        String sortDirection = null;
        if (pageable.getSort() != Sort.unsorted()) {
            column = StringUtils.substringBefore(pageable.getSort().toString(), ":");
            sortDirection = StringUtils.substringAfter(pageable.getSort().toString(), ": ");
            if (column.equals("versionName") || column.equals("updatedAt")) {
                Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDirection), "ev." + column);
                Sort sort = Sort.by(order);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
            if (column.equals("criteriaVersion")) {
                Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDirection), "cs." + column);
                Sort sort = Sort.by(order);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
        }
        String fullName = param.getString("fullName");
        String updatedAt = param.getString("searchDate");
        int urbanStatus = param.getIntValue("urbanStatus");
        String versionName = param.getString("versionName");
        String urbanStatusRe = Integer.toString(urbanStatus);
        if (urbanStatusRe.equals("0")) {
            urbanStatusRe = null;
        }
        LocalDateTime timeMinOfDay = null;
        LocalDateTime timeMaxOfDay = null;
        if (!StringUtils.isEmpty(updatedAt)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeMinOfDay = LocalDate.parse(updatedAt, formatter).atStartOfDay();
            timeMaxOfDay = LocalDate.parse(updatedAt, formatter).atTime(LocalTime.MAX);
        }
        Page<Object> dataList = null;
        if (completeAuto == null) {
            column = StringUtils.substringAfter(StringUtils.substringBefore(pageable.getSort().toString(), ":"), ".");
            sortDirection = StringUtils.substringAfter(pageable.getSort().toString(), ": ");
            if (column.equals("updatedAt")) {
                Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDirection), column);
                Sort sort = Sort.by(order);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
            dataList = usersRepository.selectAllUrbanAcc(fullName, timeMinOfDay, timeMaxOfDay, urbanStatusRe, pageable);
        } else {
            if (completeAuto.equals("admin")) {
                if (column.equals("statusEvaluate")) {
                    Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortDirection), "ev.statusRecognition");
                    Sort sort = Sort.by(order);
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
                }
                //Chủ tịch hội đồng:
                dataList = usersRepository.selectAllUrbanAccCouncil(fullName, timeMinOfDay, timeMaxOfDay, urbanStatusRe, versionName, pageable);
            } else {
                //Thành viên hội đồng:
                dataList = usersRepository.selectAllUrbanAccCouncilMember(fullName, timeMinOfDay, timeMaxOfDay, urbanStatusRe, versionName, userId, pageable);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("users", dataList.toList());
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAllCouncil(Users entity, Pageable pageable) {

        ParamMap param = ParamMap.init(entity, pageable);
        String disable = param.getString("disable");
        String disableCouncil = param.getString("disableCouncil");
        Long councilType = param.getLong("councilType");
        String userName = param.getString("userName");
        String fullName = param.getString("fullName");
        String organization = param.getString("organization");
        Page<UserCouncilDTO> dataList = usersRepository.selectAllCouncil(disable, disableCouncil, councilType, userName, fullName, organization, pageable);
        List<UserCouncilDTO> users = dataList.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAllCouncilScore(Users entity, Pageable pageable, Long evaluationVersionId, String type) {
        ParamMap param = ParamMap.init(entity, pageable);
        String disable = param.getString("disable");
        String disableCouncil = param.getString("disableCouncil");
        Long councilType = param.getLong("councilType");
        String userName = param.getString("userName");
        String fullName = param.getString("fullName");
        String organization = param.getString("organization");
        Page<UserCouncilDTO> dataList = usersRepository.selectAllCouncilScore(evaluationVersionId, type, disable, disableCouncil, councilType, userName, fullName, organization, pageable);
        List<UserCouncilDTO> users = dataList.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> getAllCouncilNull(Users entity, Pageable pageable) {

        ParamMap param = ParamMap.init(entity, pageable);
        String userName = param.getString("userName");
        String fullName = param.getString("fullName");
        Long councilType = param.getLong("councilType");
        Page<UserCouncilDTO> dataList = usersRepository.selectAllCouncilNull(userName, fullName, councilType, pageable);
        List<UserCouncilDTO> users = dataList.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> addToCouncil(List<Users> users) {

        List<Users> temp = new ArrayList<>();
        for (Users i : users) {
            Users usersUpdate = usersRepository.getById(i.getUserID());
            if (usersUpdate != null) {
                usersUpdate.setCouncilType(3L);
//                usersUpdate.setUserRoles(4L);
                temp.add(usersUpdate);
            } else {
                return ResponseEntity.badRequest().body("Null value");
            }
        }
        List<Users> usersSaveList = usersRepository.saveAll(temp);
        NotifiServiceImpl.councilNotify(usersSaveList, "add");
        return ResponseEntity.ok("Success");
    }


    @Override
    public ResponseEntity<?> updateCouncilRole(Long userId, Users users) {
        List<Users> temp = new ArrayList<>();
        Users usersUpdate = usersRepository.getById(userId);
        if (usersUpdate != null) {
            Long councilInput = users.getCouncilType();
            if (councilInput == 1) {
                if (usersRepository.existsByCouncilType()) {
                    return ResponseEntity.badRequest().body("Tài khoản chủ tịch hội đồng đã tồn tại!");
                }
                usersUpdate.setCouncilType(users.getCouncilType());
            } else if (councilInput == 2 && usersUpdate.getCouncilType() == 3) {
                if (usersRepository.checkCouncilScoreLeft(userId) > 0) {
                    return ResponseEntity.badRequest().body("Thành viên này vẫn còn bản đánh giá chưa chấm!");
                }
                usersUpdate.setCouncilType(users.getCouncilType());
            } else {
                usersUpdate.setCouncilType(users.getCouncilType());
            }
            Users userSave = usersRepository.save(usersUpdate);
            temp.add(userSave);
            NotifiServiceImpl.councilNotify(temp, "updateRole");
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.badRequest().body("Null value");
        }
    }


    @Override
    public ResponseEntity<?> updateCouncilType4(List<Users> users) {

        List<Users> temp = new ArrayList<>();
        for (Users i : users) {
            Users usersUpdate = usersRepository.getById(i.getUserID());
            if (usersUpdate != null) {
                usersUpdate.setCouncilType(4L);
                temp.add(usersUpdate);
            } else {
                return ResponseEntity.badRequest().body("Null value");
            }
        }
        usersRepository.saveAll(temp);
        return ResponseEntity.ok("Success");
    }


    @Override
    public ResponseEntity<?> updateCouncilType4ToNull(List<Users> users) {

        List<Users> temp = new ArrayList<>();
        for (Users i : users) {
            Users usersUpdate = usersRepository.getById(i.getUserID());
            if (usersUpdate != null) {
                usersUpdate.setCouncilType(null);
                temp.add(usersUpdate);
            } else {
                return ResponseEntity.badRequest().body("Null value");
            }
        }
        usersRepository.saveAll(temp);
        return ResponseEntity.ok("Success");
    }


    @Override
    public ResponseEntity<?> disableCouncil(String disableCouncil, Long userId, Long newChairmanId) {
        List<Users> temp = new ArrayList<>();
        Optional<Users> entity = usersRepository.findById(userId);
        if (usersRepository.checkCouncilScoreLeft(entity.get().getUserID()) > 0) {
            return ResponseEntity.badRequest().body("Thành viên này vẫn còn bản đánh giá chưa chấm!");
        }
        Long findCouncilType = usersRepository.findCouncilType(userId);
        if (entity.isPresent()) {
            temp.add(entity.get());
            if (disableCouncil.equals("N")) {
                NotifiServiceImpl.councilNotify(temp, "enable");
            } else {
                NotifiServiceImpl.councilNotify(temp, "disable");
            }
            if (findCouncilType == 1) {
                Users usersFind = usersRepository.getById(userId);
                if (usersFind != null) {
                    usersFind.setCouncilType(3L);
                    usersFind.setDisableCouncil("Y");
                }
                String disCouncil = usersRepository.findDisableCouncil(newChairmanId);
                if (disCouncil.equals("N")) {
                    usersRepository.updateCouncilType(newChairmanId);
                } else {
                    return ResponseEntity.badRequest().body("Chủ tịch mới đã bị vô hiệu hóa");
                }
                usersRepository.save(usersFind);
                return ResponseEntity.ok("Vô hiệu hóa và thay đổi chủ tịch mới thành công");
            }
            usersRepository.disableCouncil(disableCouncil, userId);
            return ResponseEntity.ok("Disable successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Không thể tìm thấy bằng ID: " + userId));
        }
    }


    @Override
    public ResponseEntity<?> getAllCouncilSend(Long evaluationVersionId) {
        return new ResponseEntity<>(usersRepository.getAllCouncilSend(evaluationVersionId), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> updateAccount(Long userId, Users users) {
        Users userFind = usersRepository.getById(userId);
        Long accTypeFind = userFind.getAccountType();
        if (userFind != null) {
            userFind.setFullName(users.getFullName());
            String phoneNum = users.getPhoneNumber();
            userFind.setPhoneNumber(phoneNum);
            userFind.setEmail(users.getEmail());
            if (accTypeFind != null) {
                if (accTypeFind == 2) {
                    userFind.setOrganization(users.getOrganization());
                    userFind.setPosition(users.getPosition());
                }
            }
            usersRepository.save(userFind);
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.badRequest().body("Null value");
        }
    }


    @Override
    public ResponseEntity<?> updatePassword(Long userId, String oldPass, String newPassword) {

        Users userFind = usersRepository.getById(userId);
        String password = userFind.getPassword();
        if (userFind != null) {
            if (encoder.matches(oldPass, password)) {
                if (encoder.matches(newPassword, password)) {
                    return ResponseEntity.badRequest().body("Mật khẩu mới phải khác với mật khẩu cũ!");
                }
                userFind.setPassword(encoder.encode(newPassword));
                usersRepository.save(userFind);
                return ResponseEntity.ok("Success");
            } else {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không hợp lệ!");
            }
        } else {
            return ResponseEntity.badRequest().body("Null Value");
        }
    }

}
