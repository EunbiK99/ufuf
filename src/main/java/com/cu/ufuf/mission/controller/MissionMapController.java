package com.cu.ufuf.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

