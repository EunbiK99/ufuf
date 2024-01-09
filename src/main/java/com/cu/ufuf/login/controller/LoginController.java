package com.cu.ufuf.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login/*")
public class LoginController {

    @RequestMapping("registerIdForm")
    public String registerIdForm(){
        return "login/registerIdForm";
    }

    @RequestMapping("registerProfileForm")
    public String registerProfileForm(){
        return "login/registerProfileForm";
    }

    @RequestMapping("registerUniForm")
    public String registerUniForm(){
        return "login/registerUniForm";
    }

}
