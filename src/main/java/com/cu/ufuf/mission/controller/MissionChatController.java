package com.cu.ufuf.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mission/*")
public class MissionChatController {

    @RequestMapping("chatList")
    public String chatList(HttpSession session){
        return "mission/chatList";
    }

    @RequestMapping("chatRoom")
    public String chatRoom(HttpSession session, int chat_room_id){
        return "mission/chatRoom";
    }


}
