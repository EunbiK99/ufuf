package com.cu.ufuf.login.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;

@RestController
public class LoginRestController {

    public RestResponseDto userRegister(UserInfoDto userInfoDto){

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setData(userInfoDto);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }

}
