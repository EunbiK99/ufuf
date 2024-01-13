package com.cu.ufuf.mission.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionInfoDto;
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



    


}
