package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.MissionAcceptedDto;
import com.cu.ufuf.dto.MissionCompleteDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionNotificationCategoryDto;
import com.cu.ufuf.dto.MissionNotificationDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface MissionMapSqlMapper {

    public int createMissionPk();
    public int createMissionAccPk();
    public int createMissionComplPk();

    public void insertMission(MissionInfoDto missionInfoDto);
    public void updateStatus(@RequestParam(name="param1")int mission_id, @RequestParam(name="param2")String Status);

    public List<MissionInfoDto> selectAllMission();
    public MissionInfoDto selectMissionById(int mission_id);
    public MissionAcceptedDto selectAccMissionByAccId(int mission_accepted_id);
    public MissionAcceptedDto selectMissionAccInfoByMissionId(int mission_id);

    public UserInfoDto selectUserById(int user_id);

    public OrderInfoDto getOrderInfoByMissionId(int mission_id);
    public ItemInfoDto getItemInfo(int item_id);

    public OrderInfoDto getOrderInfo(String order_id);

    public void insertMissionAcc(MissionAcceptedDto missionAcceptedDto);

    public List<MissionAcceptedDto> selectMyAccMission(int user_id);

    public List<MissionInfoDto> selectMissionListByUserId(int user_id);
    public int[] getMissionNotAcc(int user_id);

    public void insertMissionComplete(MissionCompleteDto missionCompleteDto);

    public int getMissionIdByMissionAccId(int mission_accepted_id);
    
    public void insertNotification(MissionNotificationDto missionNotificationDto);
    public List<MissionNotificationDto> selectNotifcationByUser(int user_id);
    public MissionNotificationCategoryDto selectNotifcationCategory(int mission_notification_id);
    public int isExistNotification(int user_id);
    public void updateNotifReadStatus(int mission_notification_id);

}
