package com.cu.ufuf.login.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.service.LoginServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login/*")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @RequestMapping("registerIdForm")
    public String registerIdForm(){
        return "login/registerIdForm";
    }

    @RequestMapping("saveIdToSession")
    public String saveIdToSession(HttpSession session, 
            @RequestParam(name="userid") String userid, @RequestParam(name="password") String password){

        session.setAttribute("userid", userid);
        session.setAttribute("password", password);

        System.out.println(userid);
        Object id = session.getAttribute("userid");
        String idd = (String)session.getAttribute("userid");
        System.out.println(idd);

        return "redirect:./registerProfileForm";
    }

    @RequestMapping("registerProfileForm")
    public String registerProfileForm(){

        return "login/registerProfileForm";
    }

    @RequestMapping("saveProfileToSession")
    public String saveProfileToSession(HttpSession session, 
            @RequestParam(name="name")String name, @RequestParam(name="gender") String gender, 
            @RequestParam(name="birthYear") String birthYear, @RequestParam(name="birthMonth") String birthMonth, @RequestParam(name="birthDate") String birthDate,
            @RequestParam(name="phone") String phone, @RequestParam(name="email") String email, 
            @RequestParam(name="address") String address, @RequestParam(name="detailAddress") String detailAddress){

        String birth = birthYear +"-"+ birthMonth +"-"+ birthDate;
        System.out.println(birth);

        session.setAttribute("name", name);
        session.setAttribute("gender", gender);
        session.setAttribute("phone", phone);
        session.setAttribute("birth", birth);
        session.setAttribute("email", email);
        session.setAttribute("address", address + detailAddress);

        return "redirect:./registerUniForm";
    }
    
    @RequestMapping("registerUniForm")
    public String registerUniForm(){
        return "login/registerUniForm";
    }

    @RequestMapping("saveUniToSession")
    public String saveUniToSession(HttpSession session, 
            @RequestParam(name = "university") String university, @RequestParam(name = "department") String department,
            @RequestParam(name = "studentid_img") MultipartFile studentid_img){

        session.setAttribute("university", university);
        session.setAttribute("department", department);

        if(studentid_img != null) {
				
			String rootPath = "C:/uploadFiles/ufuf/userStudentIdImg";
			
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
			
            session.setAttribute("studentid_img", todayPath + fileName);
		}

        return "redirect:./registerProfileImgForm";
    }

    @RequestMapping("registerProfileImgForm")
    public String registerProfileImgForm(){
        return "login/registerProfileImgForm";
    }

    @RequestMapping("registerUser")
    public String registerUser(HttpSession session, @RequestParam(name = "profileImg") MultipartFile profileImg){

        UserInfoDto userInfoDto = new UserInfoDto();

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

        String userid = (String)session.getAttribute("userid");
        String password = (String)session.getAttribute("password");
        String name = (String)session.getAttribute("name");
        String gender = (String)session.getAttribute("gender");
        String birth = (String)session.getAttribute("birth");
        String phone = (String)session.getAttribute("phone");
        String email = (String)session.getAttribute("email");
        String address = (String)session.getAttribute("address");
        String university = (String)session.getAttribute("university");
        String department = (String)session.getAttribute("department");
        String studentid_img = (String)session.getAttribute("studentid_img");

        System.out.println(userid);
        System.out.println(birth);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date parsedDate = dateFormat.parse(birth);

            // DTO에 설정
            userInfoDto.setBirth(parsedDate);

            // 이후에 필요한 작업 수행
        } catch (ParseException e) {
            e.printStackTrace();
            // 예외 처리를 수행하거나 적절한 방식으로 오류를 처리하세요.
        }

        userInfoDto.setUserid(userid);
        userInfoDto.setPassword(password);
        userInfoDto.setName(name);
        userInfoDto.setGender(gender);
        userInfoDto.setPhone(phone);
        userInfoDto.setEmail(email);
        userInfoDto.setAddress(address);
        userInfoDto.setUniversity(university);
        userInfoDto.setDepartment(department);
        
        loginService.insertUser(userInfoDto, studentid_img);

        session.invalidate();

        return "redirect:./welcomePage";
    }

    @RequestMapping("welcomePage")
    public String welcomePage(){
        return "login/welcomePage";
    }

    @RequestMapping("loginPage")
    public String loginPage(){
        return "login/loginPage";
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
