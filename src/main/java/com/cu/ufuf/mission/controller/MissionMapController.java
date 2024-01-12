package com.cu.ufuf.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/mission/*")
public class MissionMapController {

    @RequestMapping("missionMap")
    public String missionMap(HttpSession session){
        return "mission/missionMap";
    }

}

