package com.cu.ufuf.meeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
import com.cu.ufuf.meeting.mapper.MeetingSqlMapper;
import java.util.List;

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

    // * 미팅 프로필 셀렉트
    public MeetingProfileDto getMeetingProfileByUserId(int user_id){
        return meetingSqlMapper.selectMeetingProfileByUserId(user_id);
    }

    // * 미팅 모집글 PK 생성
    public int createGroupPk(){
        int groupPk = meetingSqlMapper.countGroupPk() + 1;
        return groupPk;
    }

    // * 미팅 모집글 인서트
    public void registerNewGroup(MeetingGroupDto meetingGroupDto){        
        meetingSqlMapper.insertNewGroup(meetingGroupDto);
    }

    // * 지역(대분류, 중분류) 카테고리 인서트
    public void registerLocationCategory(
        MeetingFirstLocationCategoryDto meetingFirstLocationCategoryDto,
        MeetingSecondLocationCategoryDto meetingSecondLocationCategoryDto
        ){
        meetingSqlMapper.insertFirstLocationCategory(meetingFirstLocationCategoryDto);
        meetingSqlMapper.insertSecondLocationCategory(meetingSecondLocationCategoryDto);        
    }

    // * 지역 대분류 PK 생성
    public int createFirstLocationCategoryPk(){
        return meetingSqlMapper.countFirstLocationCategoryForPk() + 1;
    }

    // * 지역 중분류 PK 생성
    public int createSecondLocationCategoryPk(){
        return meetingSqlMapper.countSecondLocationCategoryForPk() + 1;
    }

    // * 모집글 : 지역 카테고리 인서트
    public void registerGroupLocationCategory(
        MeetingGroupFirstLocationCategoryDto meetingGroupFirstLocationCategoryDto,
        MeetingGroupSecondLocationCategoryDto meetingGroupSecondLocationCategoryDto
        ){
            meetingSqlMapper.insertGroupFirstLocationCategory(meetingGroupFirstLocationCategoryDto);
            meetingSqlMapper.insertGroupSecondLocationCategory(meetingGroupSecondLocationCategoryDto);            
    }

    // * 미팅 태그 PK 생성
    public int createTagPk(){
        return meetingSqlMapper.countTagForPk() + 1;
    }

    // * 미팅 태그 인서트
    public void registerTag(MeetingTagDto meetingTagDto){
        meetingSqlMapper.insertTag(meetingTagDto);
    }

    // * 모집글 : 미팅 태그 인서트
    public void registerGroupTag(MeetingGroupTagDto meetingGroupTagDto){
        meetingSqlMapper.insertGroupTag(meetingGroupTagDto);
    }

    // * 미팅 모집글 리스팅
    public List<MeetingGroupDto> getGroupListAll(){
        return meetingSqlMapper.selectGroupListAll();
    }





}
