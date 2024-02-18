package com.cu.ufuf.mission.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.dto.UserPointDto;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;
import com.cu.ufuf.mission.service.MissionPaymentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
public class MissionPaymentRestController {

    @Autowired
    MissionPaymentServiceImpl missionPaymentService;
    @Autowired
    MissionMapServiceImpl missionMapService;

    @PostMapping("cancelKakaoPayment")
    public void cancelKakaoPayment() throws JsonMappingException, JsonProcessingException {
        // KakaoPay API 호출을 위한 URL
        String apiUrl = "https://open-api.kakaopay.com/online/v1/payment/cancel";

        // KakaoPay API 호출을 위한 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();

        // KakaoPay API 호출에 필요한 헤더 설정 (KakaoAK: Admin Key)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEYfe6556cbcccecbec99f52226077803d7");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성 및 헤더 설정
        if (missionPaymentService.cancelKakaoPayment() != null) {
            for (KakaoPaymentCancelReqDto requestBody : missionPaymentService.cancelKakaoPayment()) {
                HttpEntity<KakaoPaymentCancelReqDto> entity = new HttpEntity<>(requestBody, headers);

                // KakaoPay API 호출 및 응답 받기
                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
                System.out.println(response.getBody());

                // 응답을 DTO로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                KakaoPaymentCancelResDto cancelResponse = objectMapper.readValue(response.getBody(), KakaoPaymentCancelResDto.class);

                // 변환된 DTO를 이용하여 정보 삽입
                missionPaymentService.insertKakaoPayCancelInfo(cancelResponse);
            }
        }
    }

    



    


}
