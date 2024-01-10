package com.cu.ufuf.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/mission/*")
public class MissionMapController {

    @RequestMapping("missionMap")
    public String missionMap(){
        return "mission/missionMap";
    }

}

