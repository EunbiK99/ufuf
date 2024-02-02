package com.cu.ufuf.mission.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.mission.service.MissionChatServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PostMapping("loadMyMissionPlayer")
    public RestResponseDto loadMyMissionPlayer(@RequestBody String mission_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (mission_id != null && !mission_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mission_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("mission_id")) {

                    UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

                    int missionId = jsonNode.get("mission_id").asInt();
                    restResponseDto.setData(missionChatService.getChatListByMission(missionId, sessionUserInfoDto.getUser_id()));

                    restResponseDto.setResult("Success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return restResponseDto;
    }

    @PostMapping("getBasicInfo")
    public RestResponseDto getBasicInfo(@RequestBody String chat_room_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (chat_room_id != null && !chat_room_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(chat_room_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("chat_room_id")) {

                    UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

                    int chatRoomId = jsonNode.get("chat_room_id").asInt();

                    restResponseDto.setData(missionChatService.getBasicInfo(chatRoomId, sessionUserInfoDto.getUser_id()));
                    restResponseDto.setResult("Success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return restResponseDto;
    }

    @PostMapping("loadChatDialogue")
    public RestResponseDto loadChatDialogue(@RequestBody String chat_room_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (chat_room_id != null && !chat_room_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(chat_room_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("chat_room_id")) {

                    int chatRoomId = jsonNode.get("chat_room_id").asInt();
                    restResponseDto.setData(missionChatService.getChatDialogue(chatRoomId));
                    restResponseDto.setResult("Success");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return restResponseDto;
    }

    @PostMapping("sendMessage")
    public RestResponseDto sendMessage(@RequestBody MissionChatDto params, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

        params.setUser_id(sessionUserInfoDto.getUser_id());

        missionChatService.insertChat(params);
        
        return restResponseDto;
    }




}
