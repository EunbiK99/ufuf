package com.cu.ufuf.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.InterestRoomDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.room.service.RoomServiceIpml;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/room")
public class RoomRestController {

    @Autowired
    private RoomServiceIpml roomService;


    @RequestMapping("toggleInterestRoom")
	public RestResponseDto toggleInterestRoom(HttpSession session, InterestRoomDto params,@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int user_id=sessionUserInfo.getUser_id();

		params.setUser_id(user_id);
		
		int room_info_id = Integer.parseInt(roomInfoId);
		params.setRoom_info_id(room_info_id);
	
		roomService.toggleInterestRoom(params);
		
		restResponseDto.setResult("success");
		
		return restResponseDto;
	}
	
	@RequestMapping("getTotalInterestCount")
	public RestResponseDto getTotalInterestCount(@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		int room_info_id = Integer.parseInt(roomInfoId);
		int count = roomService.getTotalInterestCount(room_info_id);
		
		restResponseDto.setResult("success");
		restResponseDto.setData(count);
		
		return restResponseDto;
	}
	
	@RequestMapping("isInterest")
	public RestResponseDto isInterest(HttpSession session, InterestRoomDto params,@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int user_id=sessionUserInfo.getUser_id();

		params.setUser_id(user_id);
		
		int room_info_id = Integer.parseInt(roomInfoId);
		params.setRoom_info_id(room_info_id);

		boolean isInterestRoom = roomService.isInterestRoom(params);
		
		restResponseDto.setResult("success");
		restResponseDto.setData(isInterestRoom);
		
		return restResponseDto;
	}

    public RestResponseDto templete() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
        //restResponseDto.setData(count);
        return restResponseDto;
	}

    @RequestMapping("getMyId")
	public RestResponseDto getMyId(HttpSession session) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");


		
		restResponseDto.setResult("success");
		
		if(sessionUserInfo != null) {
			restResponseDto.setData(sessionUserInfo.getUser_id());
		}
		
		return restResponseDto;
	}
}
