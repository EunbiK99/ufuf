package com.cu.ufuf.mission.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.MissionInfoDto;

@Mapper
public interface MissionMapSqlMapper {

    public int createMissionPk();
    public void insertMission(MissionInfoDto missionInfoDto);


}
