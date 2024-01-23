package com.cu.ufuf.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.mapper.UserLoginMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    







    public String reqKakaoLogin(){

        return "https://kauth.kakao.com" + "/oauth/authorize"
                + "?client_id=" + "2f4f6f75265cd70ebabfb64c519f9843"
                + "&redirect_uri=" + "https://172.30.1.95:8888/login/getKakaoLogin"
                + "&response_type=code"
                + "&scope=profile_nickname profile_image";
    }

    public String returnCode(String code){
        return code;
    }

    public void parseToJson(String response){

         try {
            // ObjectMapper를 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // 문자열을 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(response);

            // JsonNode에서 필요한 값을 추출
            String access_token = jsonNode.get("access_token").asText();
            String token_type = jsonNode.get("token_type").asText();
            String refresh_token = jsonNode.get("refresh_token").asText();
            String expires_in = jsonNode.get("expires_in").asText();
            String scope = jsonNode.get("scope").asText();
            String refresh_token_expires_in = jsonNode.get("refresh_token_expires_in").asText();
            

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
