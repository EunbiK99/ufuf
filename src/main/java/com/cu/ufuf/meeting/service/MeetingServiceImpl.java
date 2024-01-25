package com.cu.ufuf.meeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.MeetingApplyUserDto;
import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupMemberDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.meeting.mapper.MeetingSqlMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //* 그룹PK 기준 미팅모집글 상세정보 통합해서 조회 */
    public Map<String, Object> getGroupDetailInfo(int group_pk){
        
        Map<String, Object> map = new HashMap<>();        
        
        MeetingGroupDto meetingGroupDto = meetingSqlMapper.selectGroupByGroupId(group_pk);
        List<MeetingGroupTagDto> meetingGroupTagDtoList = meetingSqlMapper.selectGroupTagListByGroupId(group_pk);
        
        int profileId = meetingGroupDto.getProfileId();
        MeetingProfileDto meetingProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(profileId);
        
        List<MeetingTagDto> tagDtoList = new ArrayList<>();
        for(MeetingGroupTagDto meetingGroupTagDto : meetingGroupTagDtoList){
            int tagId = meetingGroupTagDto.getTagId();
            tagDtoList.add(meetingSqlMapper.selectTagByTagId(tagId));
        }

        List<Map<String, Object>> applyUserMapList = new ArrayList<>();
        
        List<MeetingApplyUserDto> applyDtoList = meetingSqlMapper.selectGroupApplyUserList(group_pk);
        
        for(MeetingApplyUserDto meetingApplyUserDto : applyDtoList){
            
            Map<String, Object> applyUserMap = new HashMap<>();
            
            int applyUserProfileId = meetingApplyUserDto.getProfileId();
            
            MeetingProfileDto applyUserMeetingProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(applyUserProfileId);
            int userId = applyUserMeetingProfileDto.getUser_id();
            UserInfoDto applyUserInfoDto = meetingSqlMapper.selectUserInfoByUserId(userId);

            applyUserMap.put("applyUserProfileDto", applyUserMeetingProfileDto);            
            applyUserMap.put("applyDto", meetingApplyUserDto);
            applyUserMap.put("applyUserInfoDto", applyUserInfoDto);

            applyUserMapList.add(applyUserMap);
        }

        List<MeetingGroupMemberDto> groupMemberDtoList = meetingSqlMapper.selectMeetingGroupMemberListByGroupPk(group_pk);
        int groupMemberCountValue = meetingSqlMapper.countMeetingGroupMemberByGroupId(group_pk);
        int groupApplyUserCountValue = meetingSqlMapper.countMeetingGroupApplyUserByGroupId(group_pk);
        
        map.put("meetingGroupDto", meetingGroupDto);
        map.put("meetingProfileDto", meetingProfileDto);
        map.put("tagDtoList", tagDtoList);
        map.put("applyUserMapList", applyUserMapList);
        map.put("groupMemberDtoList", groupMemberDtoList);
        map.put("groupMemberCountValue", groupMemberCountValue);
        map.put("groupApplyUserCountValue", groupApplyUserCountValue);
        
        return map;    
    }

    // * 미팅 신청자 인서트
    public void registerMeetingApplyUser(MeetingApplyUserDto meetingApplyUserDto){
        meetingSqlMapper.insertMeetingApplyUser(meetingApplyUserDto);
    }

    // * 미팅 신청내역 존재 확인
    public int checkExistApplyUser(int profileId, int groupId){
        return meetingSqlMapper.countMeetingApplyUserByProfileId(profileId, groupId);
    }

    // * 접속유저 프로필PK 기준 미팅모집글 리스팅
    public List<MeetingGroupDto> getMeetingGroupListByProfilePk(int profileId){
        return meetingSqlMapper.selectMeetingGroupListByProfilePk(profileId);
    }

    // * 미팅 확정멤버 인서트
    public void registerGroupMember(MeetingGroupMemberDto meetingGroupMemberDto){
        meetingSqlMapper.insertMeetingGroupMember(meetingGroupMemberDto);
    }

    // * 모집글PK기준 확정 멤버 셀렉트(AJAX)
    public List<Map<String, Object>> getGroupMemberListForAJAX(int groupId){    
        return meetingSqlMapper.selectGroupMemberListByGroupIdForAJAX(groupId);
    }

    // * 미팅 확정멤버됨과 동시에 신청자 리스트에서 업데이트
    public void updateApplyUserApplyStatus(int groupId, int profileId){
        meetingSqlMapper.updateApplyUserApplyStatus(groupId, profileId);
    }

    // * 미팅 모집글PK 기준 확정멤버수 카운트
    public int countMeetingGroupMember(int groupId){
        return meetingSqlMapper.countMeetingGroupMemberByGroupId(groupId);
    }

    // * 프로필 PK기준 참여 및 신청중인 미팅에 대한 종합정보
    public List<Map<String, Object>> getApplyDataByProfileIdForAJAX(int profileId){

        List<Map<String, Object>> applyDataMapList = new ArrayList<>();

        List<MeetingApplyUserDto> list = meetingSqlMapper.selectApplyUserByProfileId(profileId);
        
        for(MeetingApplyUserDto e : list){
            
            Map<String, Object> map = new HashMap<>();
            int groupId =  e.getGroupId();
            MeetingGroupDto meetingGroupDto = meetingSqlMapper.selectGroupByGroupId(groupId);

            map.put("applyUserDto", e);
            map.put("applyGroupDto", meetingGroupDto);

            applyDataMapList.add(map);

        }

        return applyDataMapList;    
    
    }







}
