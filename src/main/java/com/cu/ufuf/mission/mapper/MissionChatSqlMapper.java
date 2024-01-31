package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;

@Mapper
public interface MissionChatSqlMapper {

    // 미션 채팅룸 인서트
    public void createMissionRoom(MissionChatRoomDto missionChatRoomDto);
    // 채팅 인서트
    public void insertChat(MissionChatDto missionChatDto);
    
    // 채팅방 목록 가져오기
    public List<MissionChatRoomDto> selectChatRoomList(int user_id);
    // 가장 최신 채팅 하나 가져오기
    public MissionChatDto selectLatestChat(int chat_room_id);
    // 안읽은 갯수 가져오기
    public int countUnreadChat(int chat_room_id);
    // 해당 미션의 채팅방 갯수 가져오기 (만약 내 미션이면 리스트 출력..)
    public int countChatRoomByMission(int mission_id);

} 
