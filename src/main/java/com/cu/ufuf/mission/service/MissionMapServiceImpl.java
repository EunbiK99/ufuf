package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapsqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;

    public void registerMission(MissionInfoDto missionInfoDto){

        // 결제 및 주문번호 발급코드 필요

        int missionPk = missionMapsqlMapper.createMissionPk();
        missionInfoDto.setMission_id(missionPk);

        merchanSqlMapper.insertItemInfo("mi" + missionPk);


        missionMapsqlMapper.insertMission(missionInfoDto);

    }


}
