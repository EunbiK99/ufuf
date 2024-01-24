package com.cu.ufuf.login.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.KakaoLoginResDto;
import com.cu.ufuf.dto.StudentidImgDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface UserLoginMapper {

    public int createUserPk();

    public void insertUser(UserInfoDto userInfoDto);
    public void insertStudentIdImg(int user_id, String studentid_img);

    public UserInfoDto isUserExist(UserInfoDto userInfoDto);

    public void insertKakaoLoginUser(KakaoLoginResDto kakaoLoginResDto);
    public int isUserExistForKakaoLogin(String userid);
    public UserInfoDto isKakaoUserHasInfo(String userid);
    public void updateKakaoUser(UserInfoDto userInfoDto);
    public KakaoLoginResDto selectKakaoUserInfo(String userid);
}
