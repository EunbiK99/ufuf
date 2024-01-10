package com.cu.ufuf.login.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.login.service.LoginServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    

    // @RequestMapping("saveProfileToSesson")
    // public RestResponseDto saveProfileToSesson(HttpSession session, 
    //                 String name, String gender, String birthYear, String birthMonth, String birthDate,
    //                 String phone, String email, String myAddress){
        
    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     String birth = birthYear + birthMonth + birthDate;

    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    //     try {
    //         Date date = dateFormat.parse(birth);
    //         session.setAttribute("birth", date);
    //         System.out.println("변환된 Date: " + date);
    //     } catch (ParseException e) {
    //         e.printStackTrace();
    //         // 변환 실패 시 예외 처리
    //     }

    //     session.setAttribute("name", name);
    //     session.setAttribute("gender", gender);
    //     session.setAttribute("phone", phone);
    //     session.setAttribute("email", email);
    //     session.setAttribute("address", myAddress);


    //     restResponseDto.setResult("Success");

    //     return restResponseDto;
    // }

    // @RequestMapping("saveUniToSesson")
    // public RestResponseDto saveUniToSesson(HttpSession session, String searchword, String department){
        
    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     session.setAttribute("university", searchword);
    //     session.setAttribute("department", department);

    //     restResponseDto.setResult("Success");

    //     return restResponseDto;
    // }

    // @RequestMapping("saveStudentIdImgToSession")
    // public RestResponseDto saveStudentIdImgToSession(HttpSession session, MultipartFile file){

    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     if(file != null) {
				
	// 		String rootPath = "C:/uploadFiles/ufuf/userStudentIdImg";
			
	// 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
	// 		String todayPath = sdf.format(new Date());
			
	// 		File todayFolderForCreate = new File(rootPath + todayPath);
				
	// 		if(!todayFolderForCreate.exists()) {
	// 			todayFolderForCreate.mkdirs();
	// 		}
			
	// 		String originalFileName = file.getOriginalFilename();
			
	// 		String uuid = UUID.randomUUID().toString();
	// 		long currentTime = System.currentTimeMillis();
	// 		String fileName = uuid + "_" + currentTime;
			
	// 		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
	// 		fileName += ext;
			
	// 		try {
	// 			file.transferTo(new File(rootPath + todayPath + fileName));
	// 		}catch(Exception e) {
	// 			e.printStackTrace();
	// 		}
			
    //         session.setAttribute("studentid_img", file);
			
	// 	}

    //     restResponseDto.setResult("Success");

    //     return restResponseDto;

    // }

    // @RequestMapping("saveProfileImgToSession")
    // public RestResponseDto saveProfileImgToSession(HttpSession session, MultipartFile file){

    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     if(file != null) {
				
	// 		String rootPath = "C:/uploadFiles/ufuf/userProfile";
			
	// 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
	// 		String todayPath = sdf.format(new Date());
			
	// 		File todayFolderForCreate = new File(rootPath + todayPath);
				
	// 		if(!todayFolderForCreate.exists()) {
	// 			todayFolderForCreate.mkdirs();
	// 		}
			
	// 		String originalFileName = file.getOriginalFilename();
			
	// 		String uuid = UUID.randomUUID().toString();
	// 		long currentTime = System.currentTimeMillis();
	// 		String fileName = uuid + "_" + currentTime;
			
	// 		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
	// 		fileName += ext;
			
	// 		try {
	// 			file.transferTo(new File(rootPath + todayPath + fileName));
	// 		}catch(Exception e) {
	// 			e.printStackTrace();
	// 		}
			
    //         session.setAttribute("profile_img", file);
			
	// 	}

    //     restResponseDto.setResult("Success");

    //     return restResponseDto;
    // }

    // @RequestMapping("registerUser")
    // public RestResponseDto registerUser(HttpSession session){

    //     System.out.println("실행");
        
    //     RestResponseDto restResponseDto = new RestResponseDto();

    //     UserInfoDto userInfoDto = new UserInfoDto();

    //     String userid = (String)session.getAttribute("userid");
    //     String password = (String)session.getAttribute("password");
    //     String name = (String)session.getAttribute("name");
    //     String gender = (String)session.getAttribute("gender");
    //     Date birth = (Date)session.getAttribute("birth");
    //     String phone = (String)session.getAttribute("phone");
    //     String email = (String)session.getAttribute("email");
    //     String address = (String)session.getAttribute("address");
    //     String university = (String)session.getAttribute("university");
    //     String department = (String)session.getAttribute("department");
    //     String profile_img = (String)session.getAttribute("profile_img");
    //     String studentid_img = (String)session.getAttribute("studentid_img");

    //     userInfoDto.setUserid(userid);
    //     userInfoDto.setPassword(password);
    //     userInfoDto.setName(name);
    //     userInfoDto.setGender(gender);
    //     // userInfoDto.setBirth(birth);
    //     userInfoDto.setPhone(phone);
    //     userInfoDto.setEmail(email);
    //     userInfoDto.setAddress(address);
    //     userInfoDto.setUniversity(university);
    //     userInfoDto.setDepartment(department);
    //     userInfoDto.setProfile_img(profile_img);
        
    //     loginService.insertUser(userInfoDto, studentid_img);

    //     restResponseDto.setResult("Success");

    //     System.out.println("인서트 완료");

    //     return restResponseDto;
    // }

    


}


