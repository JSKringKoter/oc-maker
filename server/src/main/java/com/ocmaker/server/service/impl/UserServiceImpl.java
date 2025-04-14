package com.ocmaker.server.service.impl;

import com.ocmaker.common.result.ErrorTypes;
import com.ocmaker.dto.RegisterInfoDTO;
import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import com.ocmaker.server.exception.LoginFailException;
import com.ocmaker.server.exception.RegisterFailException;
import com.ocmaker.server.mapper.UserMapper;
import com.ocmaker.server.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JavaMailSender mailSender;


    /**
     * 登陆
     * @param userInfoDTO
     * @return
     */
    @Override
    public UserInfo login(UserInfoDTO userInfoDTO) {
        String username = userInfoDTO.getUsername();
        String password = userInfoDTO.getPassword();

        //处理用户不存在的异常
        UserInfo user = userMapper.getByUsername(username);
        if (user == null) {
            throw new LoginFailException();
        }

        //进行密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            throw new LoginFailException();
        }

        return user;
    }

    /**
     * 注册
     * @param info
     * @return
     */
    @Override
    public UserInfo register(RegisterInfoDTO info) {
        String username = info.getUsername();
        String email = info.getEmail();
        String code = info.getCode();
        String password = info.getPassword();

        //判断当前用户是否为新用户
        UserInfo user = userMapper.getByUsername(username);
        //如果是，验证验证码和邮件是否一致
        if(user == null) {
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            String key = "msg_" + email;
            String verifyCode = valueOperations.get(key);
            //如果邮箱对应的验证码正确，将密码加密存储进数据库
            if (verifyCode.equals(code)) {
                //md5加密
                password = DigestUtils.md5DigestAsHex(password.getBytes());
                user = UserInfo.builder()
                        .username(username)
                        .password(password)
                        .createTime(LocalDateTime.now())
                        .build();
                LocalDateTime createTime = LocalDateTime.now();
                userMapper.insertNewUser(user, createTime);
            }
        } else {
            throw new RegisterFailException(ErrorTypes.USER_EXIST);
        }
        return user;
    }

    /**
     * 发送验证邮件
     * @param email
     */
    @Override
    public void sendEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new RuntimeException("邮件为空");
        }

        //定义redis的key
        String key = "msg_" + email;
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();

        //判断当前邮箱是否已经存在验证码
        String verifyCode = valueOperations.get(key);
        if (verifyCode == null) {
            //生成六位验证码
            StringBuilder numCode = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                numCode.append(random.nextInt(10));
            }
            String code = numCode.toString();
            //构造邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Blues. OC Maker注册验证码");
            message.setText("您的验证码为：" + code);
            message.setTo(email);

            try {
                message.setFrom(new InternetAddress(MimeUtility.encodeText("Blues.")).toString());
                //发送邮件
                mailSender.send(message);
                //将验证码存入redis中，设置过期时间为五分钟
                valueOperations.set(key, code, 5L, TimeUnit.MINUTES);

                log.info("邮件发送成功");
            } catch (Exception e) {
                log.error("邮件发送失败");
                log.error(e.getMessage());
            }

        }
    }
}
