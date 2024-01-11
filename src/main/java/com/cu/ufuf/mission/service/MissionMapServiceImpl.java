package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;

import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapsqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;

    public void insertOrder(){

        String order_id = "MI1";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");

        ramdomUUID = ramdomUUID.substring(0, 15);

        order_id = order_id + today + ramdomUUID;

    }

    public void registerMission(MissionInfoDto missionInfoDto){

        // 결제 및 주문번호 발급코드 필요

        int missionPk = missionMapsqlMapper.createMissionPk();
        missionInfoDto.setMission_id(missionPk);

        merchanSqlMapper.insertItemInfo("mi" + missionPk);


        missionMapsqlMapper.insertMission(missionInfoDto);

    }


}
