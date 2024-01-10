package com.cu.ufuf.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.room.service.RoomServiceIpml;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomServiceIpml roomService;


	@RequestMapping("roomMainPage")
    public String roomMainPage(){
        
        return "room/roomMainPage";
    }

    @RequestMapping("roomRegisterPage")
    public String roomRegisterPage(){

        return "room/roomRegisterPage";
    }

    @RequestMapping("roomRegisterProcess")
    public String roomRegisterProcess(){

        
        
        return "redirect:./roomRegisterPage";
    }

    @RequestMapping("roomRegisterCompletePage")
    public String roomRegisterCompletePage(){
        
        return "room/roomRegisterCompletePage";
    }

}
