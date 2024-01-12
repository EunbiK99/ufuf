package com.cu.ufuf.mission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;

@RestController
@RequestMapping("/mission/*")
public class MissionMapRestController {

    @Autowired
    private MissionMapServiceImpl missionMapService;

    @ResponseBody
    @PostMapping("/registerMissionProcess")
    public RestResponseDto registerMissionProcess(@RequestBody MissionInfoDto params){

        RestResponseDto restResponseDto = new RestResponseDto();

        missionMapService.registerMissionProcess(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @ResponseBody
    @PostMapping("/getItemAndOrderInfo")
    public RestResponseDto getItemAndOrderInfo(@RequestBody int mission_id){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setData(missionMapService.getItemAndOrderInfo(mission_id));

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    


}
