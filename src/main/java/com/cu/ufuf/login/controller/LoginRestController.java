package com.cu.ufuf.login.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.KakaoLoginResDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.service.LoginServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login/*")
public class LoginRestController {

    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping("isUserExistForKakaoLogin")
    public RestResponseDto isUserExistForKakaoLogin(@RequestBody String userid){

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setData(loginService.isUserExistForKakaoLogin(userid));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }
   
    @PostMapping("insertKakaoLoginUser")
    public RestResponseDto insertKakaoLoginUser(@RequestBody KakaoLoginResDto params, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        loginService.insertKakaoLoginUser(params);

        System.out.println(params);

        session.setAttribute("needResUser", loginService.getKakaoUserInfo(params.getUserid()));

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("saveSessionUser")
    public RestResponseDto saveSessionUser(@RequestBody UserInfoDto userInfoDto, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        session.setAttribute("sessionUserInfo", userInfoDto);

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("saveSessionUserForRegister")
    public RestResponseDto saveSessionUserForRegister(@RequestBody String userid, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (userid != null && !userid.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(userid);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("userid")) {
                    // mission_id 필드 추출 및 정수로 변환
                    String userId = jsonNode.get("userid").asText();

                    KakaoLoginResDto kakaoLoginResDto = loginService.getKakaoUserInfo(userId);
                    session.setAttribute("needResUser", kakaoLoginResDto);
                    
                } else {
                    System.out.println("mission_id field not found in JSON.");
                }
            } else {
                System.out.println("Received empty or null JSON string.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }






}


