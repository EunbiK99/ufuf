package com.cu.ufuf.mission.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionReviewDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.mission.component.ParseJson;
import com.cu.ufuf.mission.service.MissionChatServiceImpl;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mission/*")
public class MissionChatRestController {

    @Autowired
    MissionChatServiceImpl missionChatService;
    @Autowired
    MissionMapServiceImpl missionMapService;
    @Autowired
    ParseJson parseJson;

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
            e.printStackTrace();
        }
        return restResponseDto;
    }

    @PostMapping("sendMessage")
    public RestResponseDto sendMessage(@RequestBody MissionChatDto params, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

        params.setUser_id(sessionUserInfoDto.getUser_id());

        int chat_id = missionChatService.insertChat(params);
        
        restResponseDto.setData(chat_id);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("sendImg")
    public RestResponseDto sendImg(@RequestParam(name="sendImg") MultipartFile sendImg, 
        @RequestParam(name="chat_room_id") int chat_room_id,
        HttpSession session) {

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        MissionChatDto missionChatDto = new MissionChatDto();

        if(sendImg != null) {
				
			String rootPath = "C:/uploadFiles/ufuf/chatImg";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
			String todayPath = sdf.format(new Date());
			
			File todayFolderForCreate = new File(rootPath + todayPath);
				
			if(!todayFolderForCreate.exists()) {
				todayFolderForCreate.mkdirs();
			}
			
			String originalFileName = sendImg.getOriginalFilename();
			
			String uuid = UUID.randomUUID().toString();
			long currentTime = System.currentTimeMillis();
			String fileName = uuid + "_" + currentTime;
			
			String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
			fileName += ext;
			
			try {
				sendImg.transferTo(new File(rootPath + todayPath + fileName));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
            missionChatDto.setMessage(todayPath + fileName);
		}

        missionChatDto.setChat_room_id(chat_room_id);
        missionChatDto.setChat_category_id(2);
        missionChatDto.setUser_id(sessionUserInfoDto.getUser_id());
        missionChatDto.setIs_read("N");

        int chat_id = missionChatService.insertChat(missionChatDto);
        
        restResponseDto.setData(chat_id);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("updateReadStatus")
    public RestResponseDto updateReadStatus(@RequestBody String chat_room_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);
        UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

        missionChatService.updateReadStatus(chatRoomId, sessionUserInfoDto.getUser_id());

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("accMissionPlayer")
    public RestResponseDto accMissionPlayer(@RequestBody String chat_room_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);
        UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");

        missionMapService.accMissionPlayer(chatRoomId, sessionUserInfoDto.getUser_id());

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    




}
