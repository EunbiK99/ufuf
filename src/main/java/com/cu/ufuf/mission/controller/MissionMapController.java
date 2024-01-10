package com.cu.ufuf.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class MissionMapController {

    @Controller
    @RequestMapping("/mission/*")
    public class MissionController {

        @RequestMapping("missionMap")
        public String missionMap(){
            return "/mission/missionMap";
        }

    }
}
