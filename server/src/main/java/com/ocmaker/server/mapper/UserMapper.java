package com.ocmaker.server.mapper;

import com.ocmaker.dto.UserInfoDTO;
import com.ocmaker.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {
    @Select("select * from oc_maker.user where username = #{username} and password = #{password}")
    public UserInfo getByUsernameAndPassword(UserInfoDTO userInfoDTO);

    @Select("select * from oc_maker.user where username = #{username}")
    public UserInfo getByUsername(String username);

    @Insert("INSERT INTO oc_maker.user(username, password, create_time) values(#{info.username}, #{info.password}, #{createTime})")
    public void insertNewUser(UserInfo info, LocalDateTime createTime);
}
