package com.cu.ufuf.mission.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.OrderInfoDto;

@Mapper
public interface MissionMapSqlMapper {

    public int createMissionPk();
    public void insertMission(MissionInfoDto missionInfoDto);

    public OrderInfoDto getOrderInfo(int mission_id);
    public ItemInfoDto getItemInfo(int item_id);

}
