package com.cu.ufuf.mission.controller;

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
    public String map(HttpSession session, Model model){

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        model.addAttribute("sessionUserInfo", sessionUserInfo);

        return "mission/map";
    }

















    @RequestMapping("paymentSuccessPage")
    public String paymentSuccessPage(@RequestParam(name="order_id") String order_id,
            @RequestParam(name="pg_token") String pg_token, Model model){

        System.out.println(order_id);

        model.addAttribute("order_id", order_id);
        model.addAttribute("pg_token", pg_token);

        return "mission/paymentSuccessPage";
    }

    @RequestMapping("paymentCancelPage")
    public String paymentCancelPage(@RequestParam(name="order_id") String order_id,
            @RequestParam(name="pg_token") String pg_token, Model model){

        model.addAttribute("order_id", order_id);
        model.addAttribute("pg_token", pg_token);

        return "mission/paymentCancelPage";
    }

    @RequestMapping("paymentFailPage")
    public String paymentFailPage(@RequestParam(name="order_id") String order_id,
            @RequestParam(name="pg_token") String pg_token, Model model){
        model.addAttribute("order_id", order_id);
        model.addAttribute("pg_token", pg_token);

        return "mission/paymentFailPage";
    }



}

