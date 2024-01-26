package com.cu.ufuf.meeting.service;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cu.ufuf.dto.MeetingApplyUserDto;
import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupMemberDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingKakaoApproveResponseDto;
import com.cu.ufuf.dto.MeetingKakaoReadyResponseDto;
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
        int groupMemberCountValue = meetingSqlMapper.countMeetingGroupMemberByGroupId(group_pk);
        int groupApplyUserCountValue = meetingSqlMapper.countMeetingGroupApplyUserByGroupId(group_pk);

        List<Map<String, Object>> groupMemberDataList = new ArrayList<>();

        List<MeetingGroupMemberDto> list = meetingSqlMapper.selectMeetingGroupMemberListByGroupPk(group_pk);
        for(MeetingGroupMemberDto e : list){
            
            Map<String, Object> groupMemberMap = new HashMap<>();
            
            int groupMemberProfileId =  e.getProfileId();
            MeetingProfileDto groupMemberProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(groupMemberProfileId);
            int groupMemberUserId = groupMemberProfileDto.getUser_id();
            UserInfoDto groupMemberUserDto =  meetingSqlMapper.selectUserInfoByUserId(groupMemberUserId);
            
            groupMemberMap.put("groupMemberProfileDto", groupMemberProfileDto);
            groupMemberMap.put("groupMemberUserDto", groupMemberUserDto);
            
            groupMemberDataList.add(groupMemberMap);
        }
        
        
        map.put("meetingGroupDto", meetingGroupDto);
        map.put("meetingProfileDto", meetingProfileDto);
        map.put("tagDtoList", tagDtoList);
        map.put("applyUserMapList", applyUserMapList);
        map.put("groupMemberDataList", groupMemberDataList);
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

    public MeetingKakaoReadyResponseDto kakaoPayReady(){

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
		parameters.add("partner_order_id", "1");
		parameters.add("partner_user_id", "test00");
		parameters.add("item_name", "meeting1");
		parameters.add("quantity", "1");
		parameters.add("total_amount", "20000");
		parameters.add("tax_free_amount", "18000");
		parameters.add("approval_url", "https://172.30.1.36:8888/meeting/kakaoPayApproval"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "https://172.30.1.36:8888/meeting/kakaoPayCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "https://172.30.1.36:8888/meeting/kakaoPayFail"); // 결제 실패시 넘어갈 url

        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        MeetingKakaoReadyResponseDto meetingKakaoReadyResponseDto = restTemplate.postForObject(url, requestEntity, MeetingKakaoReadyResponseDto.class);
        meetingKakaoReadyResponseDto.setPartner_order_id("1");
        
        return meetingKakaoReadyResponseDto;
    }

    // // 결제 승인요청 메서드
	// public MeetingKakaoApproveResponseDto kakaoPayApprove(String tid, String pgToken) {
				
	// 	// request값 담기.
	// 	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
	// 	parameters.add("cid", "TC0ONETIME");
	// 	parameters.add("tid", tid);
	// 	parameters.add("partner_order_id", order_id); // 주문명
	// 	parameters.add("partner_user_id", "회사명");
	// 	parameters.add("pg_token", pgToken);
		
    //     // 하나의 map안에 header와 parameter값을 담아줌.
	// 	HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
    //     // 외부url 통신
	// 	RestTemplate template = new RestTemplate();
	// 	String url = "https://kapi.kakao.com/v1/payment/approve";
    //     // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
	// 	ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
	// 	log.info("결재승인 응답객체: " + approveResponse);
		
	// 	return approveResponse;
	// }
	
    
    // header() 셋팅
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK 326a50751a1bfac7c3ce3006c0ad9581");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}







}
