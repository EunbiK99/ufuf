package com.cu.ufuf.mission.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/mission/*")
public class MissionMapController {

    @RequestMapping("missionMap")
    public String missionMap(HttpSession session, Model model){

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        model.addAttribute("sessionUserInfo", sessionUserInfo);

        return "mission/missionMap";
    }

    @RequestMapping("map")
    public String map(HttpSession session){
        return "mission/map";
    }

    @RequestMapping("myMission")
    public String myMission(HttpSession session){
        return "mission/myMission";
    }

    @RequestMapping("missionRegistration")
    public String missionRegistration(HttpSession session, Model model){

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        model.addAttribute("sessionUserInfo", sessionUserInfo);

        return "mission/missionRegistration";
    }

    @RequestMapping("missionProcess")
    public String missionProcess(HttpSession session, @RequestParam(required = false) Optional<Integer> chat_room_id, @RequestParam(required = false) Optional<Integer> mission_id){
        return "mission/missionProcess";
    }

    @RequestMapping("myPage")
    public String myPage(HttpSession session){
        return "mission/myPage";
    }

    @RequestMapping("test1")
    public String test1(){
        return "mission/test1";
    }















    @RequestMapping("paymentSuccessPage")
    public String paymentSuccessPage(){

        return "mission/paymentSuccessPage";
    }

    @RequestMapping("paymentCancelPage")
    public String paymentCancelPage(){

        return "mission/paymentCancelPage";
    }

    @RequestMapping("paymentFailPage")
    public String paymentFailPage(){

        return "mission/paymentFailPage";
    }



}

