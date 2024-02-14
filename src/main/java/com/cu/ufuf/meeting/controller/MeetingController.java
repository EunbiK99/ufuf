package com.cu.ufuf.meeting.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.cu.ufuf.dto.MeetingChatMessageDto;
import com.cu.ufuf.dto.MeetingGroupMemberDto;
import com.cu.ufuf.dto.MeetingKakaoReadyResponseDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;

import jakarta.servlet.http.HttpSession;
import java.util.List;

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

        UserInfoDto sessionUser = (UserInfoDto)session.getAttribute("sessionUserInfo");
        if(sessionUser == null){
            return "../commons/loginRequierd";
        }
        else{            
            MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
            
            if(meetingProfileDto == null){
                return "./meeting/mainPage";
            }
            else{
                session.setAttribute("meetingProfileInfo", meetingProfileDto);
                // System.out.println(meetingProfileDto.getProfileid());
                // System.out.println(meetingProfileDto.getProfileNickname());
                return "./meeting/searchPage";
            }
        }
    }

    @RequestMapping("createNewGroupPage")
    public String createNewGroupPage(HttpSession session){

        UserInfoDto sessionUser = (UserInfoDto)session.getAttribute("sessionUserInfo");
        if(sessionUser == null){
            return "../commons/loginRequierd";
        }
        else{
            MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
            session.setAttribute("meetingProfileDto", meetingProfileDto);
    
            return "./meeting/createNewGroupPage";
        }
    }

    @RequestMapping("myPage")
    public String myPage(HttpSession session, Model model){
        
        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        if(sessionUserInfo == null){
            model.addAttribute("userType", "guest");
            return "./meeting/myPage";
        }else{
            model.addAttribute("userType", "user");
            
            int user_id = sessionUserInfo.getUser_id();        
            int profileCheckValue = meetingService.checkExistMeetingProfile(user_id);
            System.out.println(profileCheckValue);            

            if(profileCheckValue > 0){
                
                MeetingProfileDto meetingProfileDto = meetingService.getMeetingProfileByUserId(user_id);
                
                int profileId = meetingProfileDto.getProfileid();
                
                MeetingSNSDto userSNSDto = meetingService.getSNSDtoByProfileId(profileId);
                
                model.addAttribute("meetingProfileExist", "Y");
                model.addAttribute("meetingProfileInfo", meetingProfileDto);
                model.addAttribute("userSNSDto", userSNSDto);
                model.addAttribute("groupMemberDtoList", meetingService.getGroupMemberDtoListByProfileId(profileId));
                return "./meeting/myPage";
            }
            else{
                model.addAttribute("meetingProfileExist", "N");
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

    @RequestMapping("myRecruitGroupDetailPage")
    public String myRecruitGroupDetailPage(HttpSession session, Model model, int groupId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        model.addAttribute("groupDetailInfo", meetingService.getGroupDetailInfo(groupId));
        
        System.out.println("groupId : " + groupId);
        System.out.println("profileNickname : " + meetingProfileDto.getProfileNickname());
        System.out.println("profileid : " + meetingProfileDto.getProfileid());
        

        return "./meeting/myRecruitGroupDetailPage";
    }

    @RequestMapping("myApplyGroupPage")
    public String myApplyGroupPage(HttpSession session, int profileId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        System.out.println("profileId : " + profileId);
        System.out.println("profileNickname : " + meetingProfileDto.getProfileNickname());

        return "./meeting/myApplyGroupPage";
    }

    @RequestMapping("myApplyGroupDetailPage")
    public String myApplyGroupDetailPage(HttpSession session, Model model, int groupId){

        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        
        session.setAttribute("meetingProfileDto", meetingProfileDto);

        model.addAttribute("groupDetailInfo", meetingService.getGroupDetailInfo(groupId));

        return "./meeting/myApplyGroupDetailPage";
    }

    @RequestMapping("kakaoPayCancel")
    public String kakaoPayCancel(){
        return "./meeting/kakaoPayCancel";
    }

    @RequestMapping("kakaoPayApproval")
    public String kakaoPayApproval(){
        return "./meeting/kakaoPayApproval";
    }

    @RequestMapping("kakaoPayFail")
    public String kakaoPayFail(){
        return "./meeting/kakaoPayFail";
    }

    @RequestMapping("groupReviewPage")
    public String groupReviewPage(HttpSession session, Model model, int groupId){
        
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        int profileId = meetingProfileDto.getProfileid();

        MeetingGroupMemberDto meetingGroupMemberDto = meetingService.getGroupMemberDtoByGroupIdAndProfileId(groupId, profileId);
        int groupMemberId = meetingGroupMemberDto.getGroupMemberId();

        Map<String, Object> userReviewData = meetingService.getUserReviewDataAll(groupMemberId);
        
        session.setAttribute("meetingProfileDto", meetingProfileDto);
        session.setAttribute("userInfoDto", userInfoDto);

        model.addAttribute("groupDetailInfo", meetingService.getGroupDetailInfo(groupId));
        model.addAttribute("meetingGroupMemberDto", meetingGroupMemberDto);
        model.addAttribute("userReviewData", userReviewData);

        return "./meeting/groupReviewPage";
    }

    @RequestMapping("chatListPage")
    public String chatListPage(HttpSession session, Model model){        
        return "./meeting/chatListPage";
    }

    @RequestMapping("chatRoomPage")
    public String chatRoomPage(HttpSession session, Model model, int chatRoomId){

        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileInfo");
        int profileId = meetingProfileDto.getProfileid();
        Map<String, Object> chatRoomData = meetingService.getChatRoomData(chatRoomId, profileId);
        model.addAttribute("chatRoomData", chatRoomData);

        return "./meeting/chatRoomPage";    
    }

    @RequestMapping("errorPage")
    public String errorPage(){
        return "./meeting/errorPage";
    }
    
}
