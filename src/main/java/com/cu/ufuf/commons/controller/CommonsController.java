package com.cu.ufuf.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/commons")
public class CommonsController {

    @RequestMapping("mainPage")
        public String mainPage(){
        return "./commons/mainPage";
    }

    @RequestMapping("loginRequierd")
    public String loginRequierd(){
        return "./commons/loginRequierd";
    }

    @RequestMapping("myPage")
    public String myPage(HttpSession session){

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
        
        return "./commons/myPage";
    }
}
