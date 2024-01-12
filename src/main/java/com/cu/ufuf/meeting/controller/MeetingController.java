package com.cu.ufuf.meeting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/meeting/*")
public class MeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;

    @RequestMapping("mainPage")
    public String mainPage(Model model, HttpSession session){
        
        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        if(sessionUserInfo == null){
            model.addAttribute("meetingProfileCheck", "guest");
            return "./meeting/mainPage";
        }else{
            int user_id = sessionUserInfo.getUser_id();        
            model.addAttribute("meetingProfileCheck", meetingService.checkExistMeetingProfile(user_id));        
            return "./meeting/mainPage";
        }

        
    }

    @RequestMapping("registerProfile")
    public String registerProfile(){
        return "./meeting/registerProfile";
    }

    @RequestMapping("searchPage")
    public String searchPage(){
        return "./meeting/searchPage";
    }

    @RequestMapping("createMeetingGroupPage")
    public String createMeetingGroupPage(){
        return "./meeting/createMeetingGroupPage";
    }


}
