package com.udabe.cmmn.util;

import com.udabe.entity.Users;
import com.udabe.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;


@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    private final UsersRepository usersRepository;

    private final OtpUtil otpUtil;


    public EmailUtil(UsersRepository usersRepository, OtpUtil otpUtil) {
        this.usersRepository = usersRepository;
        this.otpUtil = otpUtil;
    }

    public void setPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        Users users = usersRepository.findOtpByEmail(email).get();
        users.setOtp(otpUtil.generateOtp());
        users.setOtpGeneratedTime(LocalDateTime.now());
        usersRepository.save(users);
        String otp = users.getOtp();
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set password");
        mimeMessageHelper.setText("Your otp code: "  + otp + "<br> Your otp code will be available for 10 minutes <br>"
        .formatted(email), true);

        javaMailSender.send(mimeMessage);
    }
}
