package com.cu.ufuf.mission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.mission.service.MissionChatServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mission/*")
public class MissionChatRestController {

    @Autowired
    MissionChatServiceImpl missionChatService;

    @GetMapping("loadChatList")
    public RestResponseDto loadChatList(HttpSession session){

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        int user_id = sessionUserInfo.getUser_id();

        responseDto.setData(missionChatService.getMyMissionRoomList(user_id));
        responseDto.setResult("Success");

        return responseDto;
    }



}
