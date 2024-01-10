package com.cu.ufuf.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/commons")
public class CommonsController {

    @RequestMapping("mainPage")
        public String mainPage(){
        return "./commons/mainPage";
    }

}
