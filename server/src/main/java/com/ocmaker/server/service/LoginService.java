package com.ocmaker.server.service;

import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;

public interface LoginService {
    public UserInfo login(UserInfoDTO userInfoDTO);
}
