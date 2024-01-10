package com.cu.ufuf.meeting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.meeting.service.MeetingServiceImpl;

@Controller
@RequestMapping("/meeting/*")
public class MeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;

    @RequestMapping("mainPage")
    public String mainPage(){
        return "./meeting/mainPage";
    }

    @RequestMapping("registerProfile")
    public String registerProfile(){
        return "./meeting/registerProfile";
    }

    @RequestMapping("searchPage")
    public String searchPage(){
        return "./meeting/searchPage";
    }


}
