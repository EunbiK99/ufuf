package com.cu.ufuf.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.mapper.UserLoginMapper;

@Service
public class LoginServiceImpl {

    @Autowired
    private UserLoginMapper userLoginMapper;

    public void insertUser(UserInfoDto userInfoDto){
        userLoginMapper.insertUser(userInfoDto);
    }

    
}
