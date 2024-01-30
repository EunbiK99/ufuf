package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.mission.mapper.MissionChatSqlMapper;

@Service
public class MissionChatServiceImpl {

    @Autowired
    MissionChatSqlMapper missionChatSqlMapper;

    // 일반 1; 사진 2; 알림 3;

    public void applyMission(MissionChatRoomDto missionChatRoomDto){

        missionChatSqlMapper.createMissionRoom(missionChatRoomDto);
        int chat_room_id = missionChatRoomDto.getChat_room_id();
        
        MissionChatDto missionChatDto = new MissionChatDto();
        missionChatDto.setChat_room_id(chat_room_id);
        missionChatDto.setChat_category_id(3);
        missionChatDto.setUser_id(missionChatRoomDto.getUser_id());
        missionChatDto.setMessage(null);
        missionChatDto.setIs_read("N");




    }

    

    





}
