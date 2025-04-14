package com.ocmaker.server.service;

import com.ocmaker.dto.RegisterInfoDTO;
import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;

public interface UserService {
    public UserInfo login(UserInfoDTO userInfoDTO);

    public UserInfo register(RegisterInfoDTO info);

    public void sendEmail(String email);
}
