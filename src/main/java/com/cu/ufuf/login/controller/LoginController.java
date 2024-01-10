package com.cu.ufuf.login.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

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
