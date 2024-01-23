package com.cu.ufuf.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.login.service.LoginServiceImpl;



@RestController
@RequestMapping("/login/*")
public class LoginRestController {

    @Autowired
    private LoginServiceImpl loginService;

    // @PostMapping("/saveIdToSession")
    // public RestResponseDto saveIdToSession(HttpServletRequest request, String userid, String password) {

    //     HttpSession session = request.getSession();

    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     session.setAttribute("userid", userid);
    //     session.setAttribute("password", password);

    //     System.out.println("세션 이동");

    //     restResponseDto.setResult("Success");

    //     return restResponseDto;
    // }
    
    @ResponseBody
    @RequestMapping(value = "getKakaoLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public RestResponseDto getKakaoLogin(@RequestParam(name="code") String code) {

        RestResponseDto restResponseDto = new RestResponseDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 폼 데이터 설정
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", "2f4f6f75265cd70ebabfb64c519f9843");
        formData.add("redirect_uri", "https://172.30.1.95:8888/login/getKakaoLogin");
        formData.add("code", code);

        // HttpEntity를 사용하여 헤더와 폼 데이터를 설정
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, headers);

        // RestTemplate을 사용하여 서버로 HTTP 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        // 서버로 POST 요청 전송
        String response = restTemplate.postForObject("https://kauth.kakao.com/oauth/token", httpEntity, String.class);

        restResponseDto.setData(response);

        restResponseDto.setResult("Success");

        return restResponseDto;
    }
   




}


