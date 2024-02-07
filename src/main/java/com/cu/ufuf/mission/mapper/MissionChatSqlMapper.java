package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionInfoDto;

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
    public int countUnreadChat(@RequestParam(name="param1")int chat_room_id, @RequestParam(name="param2")int user_id);
    // 해당 미션의 채팅방 갯수 가져오기 (만약 내 미션이면 리스트 출력..)
    public int countChatRoomByMission(int mission_id);
    // 해당 미션의 채팅방 목록 가져오기
    public List<MissionChatRoomDto> selectChatRoomListByMission(int mission_id);

    // 해당 채팅방의 채팅 다 가져오기
    public List<MissionChatDto> selectChatListByChatRoom(int chat_room_id);
    // 해당 채팅방의 미션 인포
    public MissionInfoDto selectMissionByChatRoom(int chat_room_id);
    // 채팅방 아이디로 채팅방인포 가져오기
    public MissionChatRoomDto selectChatRoomByChatRoom(int chat_room_id);

    // 읽음표시 업데이트
    public void updateReadStatus(@RequestParam(name="param1")int chat_room_id, @RequestParam(name="param2")int user_id);

    // 미션 아이디랑 유저 아이디로 챗룸 가져오기
    public MissionChatRoomDto selectChatRoomByMissionAndUser(MissionChatRoomDto missionChatRoomDto);

} 
