package com.cu.ufuf.mission.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;

@Mapper
public interface MissionChatSqlMapper {

    // 미션 채팅룸 인서트
    public void createMissionRoom(MissionChatRoomDto missionChatRoomDto);

    // 채팅 인서트
    public void insertChat(MissionChatDto missionChatDto);



}
