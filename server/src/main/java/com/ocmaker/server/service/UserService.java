package com.ocmaker.server.service;

import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;

public interface UserService {
    public UserInfo login(UserInfoDTO userInfoDTO);

    public UserInfo register(UserInfoDTO info);
}
