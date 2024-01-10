package com.cu.ufuf.meeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
