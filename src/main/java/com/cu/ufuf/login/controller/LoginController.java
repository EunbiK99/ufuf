package com.cu.ufuf.login.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.service.LoginServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login/*")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @RequestMapping("registerIdForm")
    public String registerIdForm(HttpSession session){
        return "login/registerIdForm";
    }

    @RequestMapping("registerProfileForm")
    public String registerProfileForm(HttpSession session){

        return "login/registerProfileForm";
    }
    
    @RequestMapping("registerUniForm")
    public String registerUniForm(HttpSession session){
        return "login/registerUniForm";
    }

    @RequestMapping("registerProfileImgForm")
    public String registerProfileImgForm(HttpSession session){
        return "login/registerProfileImgForm";
    }

    @RequestMapping("aaa")
    public String aaa(HttpSession session){
        System.out.println("ㅎㅇ");
        return "login/aaa";
    }

    @RequestMapping("testUserRegister")
    public String testUserRegister(){
        return "login/testUserRegister";
    }

    @RequestMapping("testUserRegisterProcess")
    public String testUserRegisterProcess(UserInfoDto userInfoDto,  
        @RequestParam(name = "profileImg") MultipartFile profileImg, 
        @RequestParam(name = "studentid_img") MultipartFile studentid_img)
        {

        System.out.println("aa");
        
        String studentidImg = "null";

        if(profileImg != null) {
				
			String rootPath = "C:/uploadFiles/ufuf/userProfile";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
			String todayPath = sdf.format(new Date());
			
			File todayFolderForCreate = new File(rootPath + todayPath);
				
			if(!todayFolderForCreate.exists()) {
				todayFolderForCreate.mkdirs();
			}
			
			String originalFileName = profileImg.getOriginalFilename();
			
			String uuid = UUID.randomUUID().toString();
			long currentTime = System.currentTimeMillis();
			String fileName = uuid + "_" + currentTime;
			
			String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
			fileName += ext;
			
			try {
				profileImg.transferTo(new File(rootPath + todayPath + fileName));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
            userInfoDto.setProfile_img(todayPath + fileName);
			
		}

        if(studentid_img != null) {
				
			String rootPath = "C:/uploadFiles/ufuf/userProfile";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
			String todayPath = sdf.format(new Date());
			
			File todayFolderForCreate = new File(rootPath + todayPath);
				
			if(!todayFolderForCreate.exists()) {
				todayFolderForCreate.mkdirs();
			}
			
			String originalFileName = studentid_img.getOriginalFilename();
			
			String uuid = UUID.randomUUID().toString();
			long currentTime = System.currentTimeMillis();
			String fileName = uuid + "_" + currentTime;
			
			String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
			fileName += ext;
			
			try {
				studentid_img.transferTo(new File(rootPath + todayPath + fileName));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
            studentidImg = (todayPath + fileName);
			
		}

        loginService.insertUser(userInfoDto, studentidImg);

        return "login/aaa";
    }

    @RequestMapping("testloginPage")
    public String testloginPage(){
        return "login/testloginPage";
    }

    @RequestMapping("testloginProcess")
    public String testloginProcess(HttpSession session, UserInfoDto params){

        if(loginService.isUserExist(params) != null){

            session.setAttribute("sessionUserInfo", loginService.isUserExist(params));
            return "redirect:../commons/mainPage";
            
        }else{
            return "redirect:./aaa";
        }
    }

}
