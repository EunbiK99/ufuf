package com.cu.ufuf.meeting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/meeting/*")
public class MeetingController {

    @RequestMapping("mainPage")
    public String mainPage(){
        return "./meeting/mainPage";
    }

    @RequestMapping("registerProfile")
    public String registerProfile(){
        return "./meeting/registerProfile";
    }


}
