package com.ocmaker.server.service.impl;

import com.ocmaker.common.result.ErrorTypes;
import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import com.ocmaker.server.exception.LoginFailException;
import com.ocmaker.server.exception.RegisterFailException;
import com.ocmaker.server.mapper.UserMapper;
import com.ocmaker.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
    public UserInfo register(UserInfoDTO info) {
        String username = info.getUsername();
        String password = info.getPassword();

        //判断当前用户是否为新用户
        UserInfo user = userMapper.getByUsername(username);
        //如果是，完成注册
        if(user == null) {
            //md5加密
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            user = UserInfo.builder()
                    .username(username)
                    .password(password)
                    .createTime(LocalDateTime.now())
                    .build();
            LocalDateTime createTime = LocalDateTime.now();
            userMapper.insertNewUser(user, createTime);
        } else {
            throw new RegisterFailException(ErrorTypes.USER_EXIST);
        }
        return user;
    }
}
