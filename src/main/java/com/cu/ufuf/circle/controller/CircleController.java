package com.cu.ufuf.circle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.circle.service.CircleService;

@Controller
@RequestMapping("/circle")
public class CircleController {
    
    @Autowired
    private CircleService circleService;

    @RequestMapping("test")
    public String test(){

        return "circle/test";
    }

    @RequestMapping("circleMainPage")
    public String circleMainPage(){

        return "circle/circleMainPage";
    }
}
