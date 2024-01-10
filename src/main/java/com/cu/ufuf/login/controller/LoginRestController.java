package com.cu.ufuf.login.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginRestController {

    public RestResponseDto userRegister(UserInfoDto userInfoDto){

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setData(userInfoDto);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }

    public RestResponseDto saveIdToSesson(HttpSession session, String id, String password){
        
        RestResponseDto restResponseDto = new RestResponseDto();

        session.setAttribute("id", id);
        session.setAttribute("password", password);

        restResponseDto.setData(session);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }

    public RestResponseDto saveProfileToSesson(HttpSession session, 
                    String name, String gender, Date birth, String phone, String email, 
                    String address, String detailAddress){
        
        RestResponseDto restResponseDto = new RestResponseDto();

        String myAddress = address + detailAddress;

        session.setAttribute("name", name);
        session.setAttribute("gender", gender);
        session.setAttribute("birth", birth);
        session.setAttribute("phone", phone);
        session.setAttribute("email", email);
        session.setAttribute("address", myAddress);

        restResponseDto.setData(session);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }

    public RestResponseDto saveUniToSesson(HttpSession session, String searchword, String department, MultipartFile studentid_img){
        
        RestResponseDto restResponseDto = new RestResponseDto();

        if(studentid_img != null) {
				
			String rootPath = "C:/uploadFiles/productIMG/";
			
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
			
            session.setAttribute("studentid_img", studentid_img);
			
		}

        session.setAttribute("university", searchword);
        session.setAttribute("department", department);

        restResponseDto.setData(session);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }

    public RestResponseDto registerUser(HttpSession session){
        
        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto userInfoDto = new UserInfoDto();

        Object name = session.getAttribute("name");

        System.out.println(name);

        restResponseDto.setData(session);
        restResponseDto.setResult("Success");

        return restResponseDto;
    }



}


