package com.ocmaker.server.service.impl;

import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import com.ocmaker.server.mapper.LoginMapper;
import com.ocmaker.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    @Override
    public UserInfo login(UserInfoDTO userInfoDTO) {
        return loginMapper.getByAccountAndPassword(userInfoDTO);
    }
}
