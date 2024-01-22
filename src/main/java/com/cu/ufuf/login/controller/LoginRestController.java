package com.cu.ufuf.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.login.service.LoginServiceImpl;



@RestController
@RequestMapping("/login/*")
public class LoginRestController {

    @Autowired
    private LoginServiceImpl loginService;

    // @PostMapping("/saveIdToSession")
    // public RestResponseDto saveIdToSession(HttpServletRequest request, String userid, String password) {

    //     HttpSession session = request.getSession();

    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     session.setAttribute("userid", userid);
    //     session.setAttribute("password", password);

    //     System.out.println("세션 이동");

    //     restResponseDto.setResult("Success");

    //     return restResponseDto;
    // }
    
    @ResponseBody
    @PostMapping("getKakaoLogin")
    public RestResponseDto getKakaoLogin(@RequestParam String code){

        RestResponseDto restResponseDto = new RestResponseDto();

        

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }
   




}


