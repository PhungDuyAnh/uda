package com.udabe.controller;

import com.udabe.cmmn.util.OtpUtil;
import com.udabe.entity.ERole;
import com.udabe.entity.Role;
import com.udabe.entity.UserRole;
import com.udabe.entity.Users;
import com.udabe.payload.request.LoginRequest;
import com.udabe.payload.request.UrbanSignupRequest;
import com.udabe.payload.response.JwtResponse;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.RoleRepository;
import com.udabe.repository.UserRoleRepository;
import com.udabe.repository.UsersRepository;
import com.udabe.security.jwt.JwtUtils;
import com.udabe.security.service.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UsersRepository userRepository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final OtpUtil otpUtil;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UsersRepository userRepository,
                          JwtUtils jwtUtils, PasswordEncoder encoder, RoleRepository roleRepository, UserRoleRepository userRoleRepository, OtpUtil otpUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.otpUtil = otpUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<Users> users = userRepository.findByUserName(loginRequest.getUserName());
        if (users.isPresent()) {
            if (users.get().getDisable().equals("N")) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                return ResponseEntity.ok(new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail()
                ));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Tài khoản đã bị vô hiệu hóa"));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Không tìm thấy người dùng"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UrbanSignupRequest urbanSignupRequest){
        if (userRepository.existsByUserName(urbanSignupRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Tên đăng nhập đã tồn tại"));
        }

        if (userRepository.existsByEmail(urbanSignupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email đăng kí đã tồn tại"));
        }

        if(userRepository.existsByAddressCodeId(urbanSignupRequest.getAddressCodeId())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Tài khoản đô thị đã được đăng ký định danh"));
        }


        // Create new user's account:
        Users user = new Users();
        user.setUserName(urbanSignupRequest.getUserName());
        user.setEmail(urbanSignupRequest.getEmail());
        user.setPassword(encoder.encode(urbanSignupRequest.getPassword()));
        user.setFullName(urbanSignupRequest.getFullName());
        user.setPhoneNumber(urbanSignupRequest.getPhoneNumber());
        user.setUrbanType(urbanSignupRequest.getUrbanType());
        user.setAccountType(3L);
        user.setAddressCodeId(urbanSignupRequest.getAddressCodeId());

        Set<String> strRoles = urbanSignupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_URBAN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }

        Users userSave = userRepository.save(user);
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(userSave);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return ResponseEntity.ok(new MessageResponse("Đăng kí tài khoản thành công"));
    }

}
