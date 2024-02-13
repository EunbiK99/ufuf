package com.cu.ufuf.mission.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.mission.mapper.MissionChatSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionChatServiceImpl {

    @Autowired
    MissionChatSqlMapper missionChatSqlMapper;
    @Autowired
    MissionMapSqlMapper missionMapSqlMapper;

    // 일반 1; 사진 2; 알림 3;

    public void applyMission(MissionChatRoomDto missionChatRoomDto){

        missionChatSqlMapper.createMissionRoom(missionChatRoomDto);
        int chat_room_id = missionChatRoomDto.getChat_room_id();
        int user_id = missionChatRoomDto.getUser_id();

        UserInfoDto userInfoDto = missionMapSqlMapper.selectUserById(user_id);

        int mission_id = missionChatRoomDto.getMission_id();
        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);
        List<MissionCourseDto> missionCourseDtoList = missionMapSqlMapper.selectCourseByMission(mission_id);

        MissionCourseDto firstMissionCourse = missionCourseDtoList.get(0);

        BigDecimal lat = firstMissionCourse.getLat();
        BigDecimal lng = firstMissionCourse.getLng();
        String name = userInfoDto.getName();
        String title = missionInfoDto.getTitle();

        String text = String.format("""
            <div class="row">
                <div class="col px-0">
                    <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 14.85rem; height: 8rem;" onload="loadMap(%f, %f)">
                    </div>
                </div>
            </div>
            <div class="row py-3">
                <div class="col">
                    <div class="row">
                        <div class="col" style="word-wrap: break-word;">
                            ^alert!^%s님께서 "%s" 미션 수락을 신청하셨습니다!
                            ^alert!^
                        </div>
                    </div>
                </div>
            </div>
            """, lat, lng, name, title);
     
        MissionChatDto missionChatDto = new MissionChatDto();
        missionChatDto.setChat_room_id(chat_room_id);
        missionChatDto.setChat_category_id(3);
        missionChatDto.setUser_id(user_id);
        missionChatDto.setMessage(text);
        missionChatDto.setIs_read("N");

        missionChatSqlMapper.insertChat(missionChatDto);

    }

    public List<Map<String, Object>> getMyMissionRoomList(int user_id){
        
        List<Map<String, Object>> chatRoomInfoList = new ArrayList<>();

        List<MissionChatRoomDto> missionChatRoomList = missionChatSqlMapper.selectChatRoomList(user_id);

        for(MissionChatRoomDto missionChatRoomDto : missionChatRoomList){

            int chat_room_id = missionChatRoomDto.getChat_room_id();
            int mission_id = missionChatRoomDto.getMission_id();

            Map<String, Object> chatRoomInfo = new HashMap<>();

            MissionChatDto latestChatDto = missionChatSqlMapper.selectLatestChat(chat_room_id);
            int sender_id = latestChatDto.getUser_id();
            
            chatRoomInfo.put("missionChatRoomDto", missionChatRoomDto);
            chatRoomInfo.put("missionInfoDto", missionMapSqlMapper.selectMissionById(mission_id));
            chatRoomInfo.put("latestChatDto", latestChatDto);
            chatRoomInfo.put("chatSenderDto", missionMapSqlMapper.selectUserById(sender_id));
            chatRoomInfo.put("countUnreadChat", missionChatSqlMapper.countUnreadChat(chat_room_id, user_id));
            chatRoomInfo.put("countChatRoomByMission", missionChatSqlMapper.countChatRoomByMission(mission_id));

            chatRoomInfoList.add(chatRoomInfo);
        }

        return chatRoomInfoList;
    }

    public List<Map<String, Object>> getChatListByMission(int mission_id, int user_id){

        List<Map<String, Object>> chatRoomInfoList = new ArrayList();

        List<MissionChatRoomDto> missionChatRoomList = missionChatSqlMapper.selectChatRoomListByMission(mission_id);

        for(MissionChatRoomDto missionChatRoomDto : missionChatRoomList){

            int chat_room_id = missionChatRoomDto.getChat_room_id();
            int player_id = missionChatRoomDto.getUser_id();

            Map<String, Object> chatRoomInfo = new HashMap<>();

            MissionChatDto latestChatDto = missionChatSqlMapper.selectLatestChat(chat_room_id);
            int sender_id = latestChatDto.getUser_id();

            chatRoomInfo.put("missionChatRoomDto", missionChatRoomDto);
            chatRoomInfo.put("playerInfoDto", missionMapSqlMapper.selectUserById(player_id));
            chatRoomInfo.put("latestChatDto", latestChatDto);
            chatRoomInfo.put("chatSenderDto", missionMapSqlMapper.selectUserById(sender_id));
            chatRoomInfo.put("countUnreadChat", missionChatSqlMapper.countUnreadChat(chat_room_id, user_id));
            
            chatRoomInfoList.add(chatRoomInfo);
        }

        return chatRoomInfoList;
    }

    // 채팅 읽음 업데이트
    public void updateReadStatus(int chat_room_id, int user_id){
        missionChatSqlMapper.updateReadStatus(chat_room_id, user_id);
    }

    // 채팅방 아이디로 기본정보 가져오기
    public Map<String, Object> getBasicInfo(int chat_room_id, int user_id){

        Map<String, Object> basicInfo = new HashMap<>();

        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(chat_room_id);
        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id);

        int mission_id = missionInfoDto.getMission_id();

        if(user_id == missionInfoDto.getUser_id()){
            // 만약 내가 미션등록자면..
            basicInfo.put("chatMateInfoDto", missionMapSqlMapper.selectUserById(missionChatRoomDto.getUser_id()));
        }else{
            // 만약 내가 플레이어면..
            basicInfo.put("chatMateInfoDto", missionMapSqlMapper.selectUserById(missionInfoDto.getUser_id()));
        }

        basicInfo.put("missionChatRoomDto", missionChatRoomDto);
        basicInfo.put("missionInfoDto", missionInfoDto);
        basicInfo.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));

        return basicInfo;
    }

    // 채팅방 채팅들 다 가져오기
    public List<Map<String, Object>> getChatDialogue(int chat_room_id){

        List<Map<String, Object>> chatInfoList = new ArrayList<>();

        List<MissionChatDto> missionChatList = missionChatSqlMapper.selectChatListByChatRoom(chat_room_id);
        for(MissionChatDto missionChatDto : missionChatList){

            Map<String, Object> chatInfo = new HashMap<>();

            chatInfo.put("missionChatDto", missionChatDto);
            chatInfo.put("chatSenderDto", missionMapSqlMapper.selectUserById(missionChatDto.getUser_id()));

            chatInfoList.add(chatInfo);
        }

        return chatInfoList;
    }

    public int insertChat(MissionChatDto missionChatDto){
        missionChatSqlMapper.insertChat(missionChatDto);
        return missionChatDto.getChat_id();
    }

    public int insertImgChat(MissionChatDto missionChatDto){
        missionChatSqlMapper.insertChat(missionChatDto);
        return missionChatDto.getChat_id();
    }

    // 새로운 채팅
    public List<Map<String, Object>> getNewChat(MissionChatDto missionChatDto){

        List<Map<String, Object>> chatInfoList = new ArrayList<>();

        List<MissionChatDto> missionChatList = missionChatSqlMapper.selectNewChat(missionChatDto);
        for(MissionChatDto newMissionChatDto : missionChatList){

            Map<String, Object> chatInfo = new HashMap<>();

            chatInfo.put("missionChatDto", newMissionChatDto);
            chatInfo.put("chatSenderDto", missionMapSqlMapper.selectUserById(missionChatDto.getUser_id()));

            chatInfoList.add(chatInfo);
        }

        return chatInfoList;
    }


    

    







}
