package com.ocmaker.server.controller;

import java.util.HashMap;
import java.util.Map;

import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.JwtUtils;
import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import com.ocmaker.server.exception.LoginFailException;
import com.ocmaker.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登陆
     * @return
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("用户登陆" + userInfoDTO);
        UserInfo userInfo = userService.login(userInfoDTO);

        //登陆成功，生成令牌，下发令牌
        if (userInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_uid", userInfo.getUserUid());
            claims.put("username", userInfo.getUsername());

            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(Map.of("token", jwt, "username", userInfo.getUsername()));
        }

        //登陆失败，抛出错误信息，由全局异常处理器解决
        throw new LoginFailException();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("用户注册{}", userInfoDTO);
        UserInfo userInfo = userService.register(userInfoDTO);

        //如果注册成功
        return Result.success();
    }
}
