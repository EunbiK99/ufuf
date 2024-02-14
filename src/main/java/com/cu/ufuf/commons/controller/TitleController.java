package com.cu.ufuf.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/*")
@Controller
public class TitleController {

    @RequestMapping("/")
    public String commonsMainPage(){
        return "redirect:commons/mainPage";
    }
}
