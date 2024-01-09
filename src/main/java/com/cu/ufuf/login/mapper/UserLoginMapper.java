package com.cu.ufuf.login.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface UserLoginMapper {

    public void insertUser(UserInfoDto userInfoDto);
}
