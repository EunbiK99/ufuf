package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.UserInfoDto;


@Mapper
public interface MissionMapSqlMapper {

    // 인서트 
    public void insertMission(MissionInfoDto missionInfoDto);
    public void insertMissionCourse(MissionCourseDto missionCourseDto);

    // 미션 아이디로 미션 셀렉트
    public MissionInfoDto selectMissionById(int mission_id);

    // 주문 아이디로 미션 셀렉트
    public MissionInfoDto selectMissionByOrderId(String order_id);

    // 미션 리스트 출력
    public List<MissionInfoDto> selectAllMission();
    // 미션 코스 리스트
    public List<MissionCourseDto> selectCourseByMission(int mission_id);

    // 미션 상태 업데이트
    public void updateStatus(@RequestParam(name="param1")int mission_id, @RequestParam(name="param2")String Status);

    // 유저가 이미 이 미션에 등록했는지 확인
    public int isUserApplied(MissionChatRoomDto missionChatRoomDto);




    // 유저 셀렉트
    public UserInfoDto selectUserById(int user_id);






    
//     
//     public MissionAcceptedDto selectAccMissionByAccId(int mission_accepted_id);
//     public MissionAcceptedDto selectMissionAccInfoByMissionId(int mission_id);

//     public UserInfoDto selectUserById(int user_id);

//     public OrderInfoDto getOrderInfoByMissionId(int mission_id);
//     public ItemInfoDto getItemInfo(int item_id);

//     public OrderInfoDto getOrderInfo(String order_id);

//     public void insertMissionAcc(MissionAcceptedDto missionAcceptedDto);

//     public List<MissionAcceptedDto> selectMyAccMission(int user_id);

//     public List<MissionInfoDto> selectMissionListByUserId(int user_id);
//     public int[] getMissionNotAcc(int user_id);

//     public void insertMissionComplete(MissionCompleteDto missionCompleteDto);

//     public int getMissionIdByMissionAccId(int mission_accepted_id);
    
//     public void insertNotification(MissionNotificationDto missionNotificationDto);
//     public List<MissionNotificationDto> selectNotifcationByUser(int user_id);
//     public MissionNotificationCategoryDto selectNotifcationCategory(int mission_notification_id);
//     public int isExistNotification(int user_id);
//     public void updateNotifReadStatus(int mission_notification_id);

}
