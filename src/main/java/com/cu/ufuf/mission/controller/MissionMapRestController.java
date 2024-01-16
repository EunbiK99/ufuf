package com.cu.ufuf.mission.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/mission/*")
public class MissionMapRestController {

    @Autowired
    private MissionMapServiceImpl missionMapService;

    @ResponseBody
    @PostMapping("registerMissionProcess")
    public RestResponseDto registerMissionProcess(@RequestBody MissionInfoDto params){

        RestResponseDto restResponseDto = new RestResponseDto();

        missionMapService.registerMissionProcess(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @RequestMapping("loadMissionList")
    public RestResponseDto loadMissionList(){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setData(missionMapService.loadMissionList());
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("getMissionDetail")
    public RestResponseDto getMissionDetail(@RequestBody String mission_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (mission_id != null && !mission_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mission_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("mission_id")) {
                    // mission_id 필드 추출 및 정수로 변환
                    int missionId = jsonNode.get("mission_id").asInt();
                    restResponseDto.setData(missionMapService.getMissionDetail(missionId));
                    
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


    @ResponseBody
    @PostMapping("getItemAndOrderInfo")
    public RestResponseDto getItemAndOrderInfo(@RequestBody String mission_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (mission_id != null && !mission_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mission_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("mission_id")) {
                    // mission_id 필드 추출 및 정수로 변환
                    int missionId = jsonNode.get("mission_id").asInt();

                    missionMapService.getItemAndOrderInfo(missionId);
                    restResponseDto.setData(missionMapService.getItemAndOrderInfo(missionId));
                    
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

    @ResponseBody
    @PostMapping("insertKakaoPayReqInfo")
    public RestResponseDto insertKakaoPayReqInfo(@RequestBody KakaoPaymentReqDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionMapService.insertKakaoPayReqInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("insertKakaoPayResInfo")
    public RestResponseDto insertKakaoPayResInfo(@RequestBody KakaoPaymentResDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionMapService.insertKakaoPayResInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("insertkakaoPayAccReq")
    public RestResponseDto insertkakaoPayAccReq(@RequestBody KakaoPaymentAcceptReqDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionMapService.insertKakaoPayAccReqInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("insertKakaoPayAccRes")
    public RestResponseDto insertKakaoPayAccRes(@RequestBody GetKakaoPaymentAcceptResDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionMapService.insertKakaoPayAccResInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("getPaymentInfo")
    public RestResponseDto getPaymentInfo(@RequestParam String pg_token) {

        RestResponseDto restResponseDto = new RestResponseDto();
        
        // KakaoPay API 호출을 위한 URL
        String apiUrl = "https://kapi.kakao.com/v1/payment/approvals/" + pg_token;

        // KakaoPay API 호출을 위한 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();

        // KakaoPay API 호출에 필요한 헤더 설정 (KakaoAK: Admin Key)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK fe6556cbcccecbec99f52226077803d7");

        // HttpEntity 생성 및 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // KakaoPay API 호출 및 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        restResponseDto.setData(response.getBody());

        restResponseDto.setResult("Success");

        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("upDateOrderstatus")
    public RestResponseDto upDateOrderstatus(@RequestBody OrderInfoDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
    
        missionMapService.updateOrderStatus(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }
    


    




    


    @ResponseBody
    @GetMapping("getOrderInfo")
    public RestResponseDto getOrderInfo(String Order_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setData(missionMapService.getOrderInfo(Order_id));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }


}
