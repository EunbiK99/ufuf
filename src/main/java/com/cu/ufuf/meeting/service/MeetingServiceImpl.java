package com.cu.ufuf.meeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.meeting.mapper.MeetingSqlMapper;

@Service
public class MeetingServiceImpl {

    @Autowired
    private MeetingSqlMapper meetingSqlMapper;    
    
    // * 프로필PK 생성
    public int createProfilePk(){
        int profilePk = meetingSqlMapper.countProfilePk() + 1;
        return profilePk;
    }

    // * 개인 SNS URL 중복여부 확인
    public int checkDuplicateSNSUrl(String value){
        return meetingSqlMapper.countSNSUrlByInputUrlValue(value);
    }

    // * 새 미팅 프로필 등록
    public void registerNewMeetingProfile(MeetingProfileDto meetingProfileDto){
        meetingSqlMapper.insertMeetingProfile(meetingProfileDto);
    }

    // * 새 SNS(URL) 등록
    public void resgisterNewSNS(MeetingSNSDto meetingSNSDto){
        meetingSqlMapper.insertNewSNS(meetingSNSDto);
    }

    // * 미팅 프로필 존재 여부 확인
    public int checkExistMeetingProfile(int user_id){
        return meetingSqlMapper.countMeetingProfileByUserId(user_id);
    }



}
