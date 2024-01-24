package com.cu.ufuf.meeting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/meeting/*")
public class MeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;

    @RequestMapping("mainPage")
    public String mainPage(Model model, HttpSession session){
        
        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        if(sessionUserInfo == null){            
            session.setAttribute("userType", "guest");
            return "./meeting/mainPage";
        }else{
            session.setAttribute("userType", "user");
            session.setAttribute("sessionUserInfo", sessionUserInfo);
            
            int user_id = sessionUserInfo.getUser_id();        
            int profileCheckValue = meetingService.checkExistMeetingProfile(user_id);            

            if(profileCheckValue > 0){                
                session.setAttribute("meetingProfileExist", "Y");
                session.setAttribute("meetingProfileInfo", meetingService.getMeetingProfileByUserId(user_id));
                return "./meeting/mainPage";
            }
            else{
                session.setAttribute("meetingProfileExist", "N");
                return "./meeting/mainPage";
            }
        }
    }

    @RequestMapping("registerProfile")
    public String registerProfile(){
        return "./meeting/registerProfile";
    }

    @RequestMapping("searchPage")
    public String searchPage(HttpSession session){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        
        if(meetingProfileDto == null){
            return "./commons/loginRequierd";
        }
        else{
            session.setAttribute("meetingProfileInfo", meetingProfileDto);
            System.out.println(meetingProfileDto.getProfileid());
            System.out.println(meetingProfileDto.getProfileNickname());
            return "./meeting/searchPage";
        }        
    }

    @RequestMapping("createNewGroupPage")
    public String createNewGroupPage(HttpSession session){

        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        return "./meeting/createNewGroupPage";
    }

    @RequestMapping("myPage")
    public String myPage(HttpSession session, Model model){
        
        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        if(sessionUserInfo == null){            
            session.setAttribute("userType", "guest");
            return "./meeting/myPage";
        }else{
            session.setAttribute("userType", "user");
            session.setAttribute("sessionUserInfo", sessionUserInfo);
            
            int user_id = sessionUserInfo.getUser_id();        
            int profileCheckValue = meetingService.checkExistMeetingProfile(user_id);
            System.out.println(profileCheckValue);            

            if(profileCheckValue > 0){                
                session.setAttribute("meetingProfileExist", "Y");
                session.setAttribute("meetingProfileInfo", meetingService.getMeetingProfileByUserId(user_id));
                return "./meeting/myPage";
            }
            else{
                session.setAttribute("meetingProfileExist", "N");
                return "./meeting/myPage";
            }
        }        
    }

    @RequestMapping("myRecruitGroupPage")
    public String myRecruitGroupPage(HttpSession session, int profileId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        return "./meeting/myRecruitGroupPage";
    }

    @RequestMapping("myRecruidGroupDetailPage")
    public String myRecruidGroupDetailPage(HttpSession session, Model model, int groupId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        model.addAttribute("groupDetailInfo", meetingService.getGroupDetailInfo(groupId));

        return "./meeting/myRecruidGroupDetailPage";
    }

    @RequestMapping("myApplyGroupPage")
    public String myApplyGroupPage(HttpSession session, int profileId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        System.out.println("profileId : " + profileId);
        System.out.println("profileNickname : " + meetingProfileDto.getProfileNickname());

        return "./meeting/myApplyGroupPage";
    }

}
