package com.ocmaker.server.controller;

import java.util.HashMap;
import java.util.Map;

import com.ocmaker.common.result.ErrorTypes;
import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.JwtUtils;
import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import com.ocmaker.server.exception.LoginFailException;
import com.ocmaker.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登陆
     * @return
     */
    @PostMapping("")
    public Result<?> login(@RequestBody UserInfoDTO userInfoDTO) {
        log.info("用户登陆" + userInfoDTO);
        UserInfo userInfo = loginService.login(userInfoDTO);

        //登陆成功，生成令牌，下发令牌
        if (userInfo != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_uid", userInfo.getUserUid());
            claims.put("name", userInfo.getName());
            claims.put("account", userInfo.getAccount());

            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(Map.of("token", jwt, "name", userInfo.getName()));
        }

        //登陆失败，抛出错误信息，由全局异常处理器解决
        throw new LoginFailException(ErrorTypes.ACCOUNT_OR_PASSWORD_ERROR);
    }
}
