package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.OrderInfoDto;

@Mapper
public interface MissionMapSqlMapper {

    public int createMissionPk();
    public void insertMission(MissionInfoDto missionInfoDto);

    public List<MissionInfoDto> selectAllMission();

    public OrderInfoDto getOrderInfoByMissionId(int mission_id);
    public ItemInfoDto getItemInfo(int item_id);

    public OrderInfoDto getOrderInfo(String order_id);

}
