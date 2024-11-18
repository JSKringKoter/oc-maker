package com.ocmaker.server.mapper;

import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select * from oc_maker.user where account = #{account} and password = #{password}")
    public UserInfo getByAccountAndPassword(UserInfoDTO userInfoDTO);
}
