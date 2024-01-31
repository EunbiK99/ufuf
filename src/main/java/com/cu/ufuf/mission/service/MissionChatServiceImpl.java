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

        String text = """
                    <div class="row">
                        <div class="col px-0">
                            <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 15.86rem; height: 8rem;">
                                <script>
                                    var markerImg = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption)
                                    var markerPosition = new kakao.maps.LatLng(${lat}, ${lng});
                                    var marker = new kakao.maps.Marker({
                                        position: markerPosition,
                                        image: markerImg
                                    });
                                    var staticMapContainer  = document.getElementById('staticMap'),
                                        staticMapOption = { 
                                            center: new kakao.maps.LatLng(${lat}, ${lng}),
                                            level: 4,
                                            marker: marker
                                        };
                                    var staticMap = new kakao.maps.StaticMap(staticMapContainer, staticMapOption);
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="row py-3">
                        <div class="col">
                            <div class="row">
                                <div class="col" style="word-wrap: break-word;">
                                    ^alert!^${name}님께서 "${title}" 미션 수락을 신청하셨습니다
                                    아래 버튼을 클릭하셔서 미션을 수락해주세요!^alert!^
                                </div>
                            </div>
                            <div class="row mt-2">
                                <div class="col d-grid">
                                    <button id="acceptMissionPlayerBtn" class="btn py-1 px-3 rounded-1 text-white fw-semibold"
                                        style="font-size: 1.0rem; background-color: #FF8827;" onclick="acceptMissionPlayer()">
                                        <span class="me-2"><i class="bi bi-envelope-open"></i></span>
                                        <span class="" style="font-size: 0.9rem;">미션 수락</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    """;
     
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
            chatRoomInfo.put("countUnreadChat", missionChatSqlMapper.countUnreadChat(chat_room_id));
            chatRoomInfo.put("countChatRoomByMission", missionChatSqlMapper.countChatRoomByMission(mission_id));

            chatRoomInfoList.add(chatRoomInfo);
        }

        return chatRoomInfoList;
    }

    

    







}
