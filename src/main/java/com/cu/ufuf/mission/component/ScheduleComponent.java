package com.cu.ufuf.mission.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cu.ufuf.mission.controller.MissionPaymentRestController;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class ScheduleComponent {

    @Autowired
    MissionMapServiceImpl missionMapService;
    @Autowired
    MissionPaymentRestController missionPaymentRestController;
    
    @Scheduled(cron = "0 0 0 * * *")
    public void updateFailMission() {
        missionMapService.updateMissionFail();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateSuccessAfter3days() {
        missionMapService.updateSuccessAfter3days();
    }



}
