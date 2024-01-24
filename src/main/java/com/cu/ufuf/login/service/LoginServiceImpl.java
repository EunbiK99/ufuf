package com.cu.ufuf.login.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.KakaoLoginResDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.mapper.UserLoginMapper;

@Service
public class LoginServiceImpl {

    @Autowired
    private UserLoginMapper userLoginMapper;

    public void insertUser(UserInfoDto userInfoDto, String studentid_img){

        int userPk = userLoginMapper.createUserPk();
        userInfoDto.setUser_id(userPk);

        userLoginMapper.insertUser(userInfoDto);
        userLoginMapper.insertStudentIdImg(userPk, studentid_img);

    }

    public UserInfoDto isUserExist(UserInfoDto userInfoDto){
        return userLoginMapper.isUserExist(userInfoDto);
    }


    // 카카오 로그인 시 insert
    public void insertKakaoLoginUser(KakaoLoginResDto kakaoLoginResDto){

        int userPk = userLoginMapper.createUserPk();
        kakaoLoginResDto.setUser_id(userPk);
        // 랜덤 비밀번호 생성
        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString();
        String password = ramdomUUID.substring(0, 20);

        kakaoLoginResDto.setPassword(password);

        userLoginMapper.insertKakaoLoginUser(kakaoLoginResDto);
        
    }

    public KakaoLoginResDto getKakaoUserInfo(String userid){
       return userLoginMapper.selectKakaoUserInfo(userid);
    }

    public Map<String, Object> isUserExistForKakaoLogin(String userid){

        /* 카카오 로그인 시
            유저id 가 저장되어있지 않을 때 = 0
            유저id 가 저장은 되어있지만 프로필이 작성되지 않았을 때 = 1
            유저 프로필이 존재할 때 = 2
        */

        Map<String, Object> userCheck = new HashMap<>();

        String userId = userid.substring(userid.indexOf(":") + 1, userid.lastIndexOf("}")).trim();

        int userExist = userLoginMapper.isUserExistForKakaoLogin(userId);

        if(userExist == 0){

            userCheck.put("isUserExist", 0);
            userCheck.put("userInfo", null);

        }else if((userExist != 0) && userLoginMapper.isKakaoUserHasInfo(userId) == null){

            userCheck.put("isUserExist", 1);
            userCheck.put("userInfo", userLoginMapper.isKakaoUserHasInfo(userId));

        }else if((userExist != 0) && userLoginMapper.isKakaoUserHasInfo(userId) != null){

            userCheck.put("isUserExist", 2);
            userCheck.put("userInfo", userLoginMapper.isKakaoUserHasInfo(userId));

        }

        return userCheck;
    }

    public void updateKakaoUser(UserInfoDto userInfoDto, String studentid_img){

        int userPk = userInfoDto.getUser_id();

        userLoginMapper.insertStudentIdImg(userPk, studentid_img);
        userLoginMapper.updateKakaoUser(userInfoDto);
    }
    







    
    


}
