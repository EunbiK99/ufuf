package com.cu.ufuf.meeting.service;

import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MeetingApplyUserDto;
import com.cu.ufuf.dto.MeetingBothLikeDto;
import com.cu.ufuf.dto.MeetingChatRoomDto;
import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupMemberDto;
import com.cu.ufuf.dto.MeetingGroupReviewDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingKakaoApproveReqDto;
import com.cu.ufuf.dto.MeetingKakaoApproveResponseDto;
import com.cu.ufuf.dto.MeetingKakaoReadyResponseDto;
import com.cu.ufuf.dto.MeetingMemberReviewDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
import com.cu.ufuf.dto.MeetingVoteBestMemberDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.meeting.mapper.MeetingSqlMapper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        int groupPk = meetingGroupDto.getGroupId();
        meetingSqlMapper.insertItemInfo(groupPk);
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
        int notPaidMemberCount = meetingSqlMapper.countNotPaidGroupMemberCount(group_pk);
        if(notPaidMemberCount == 0){
            meetingGroupDto.setGroupMeetingStatus("Y");
        }
        
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
            MeetingGroupMemberDto meetingGroupMemberDto = meetingSqlMapper.selectGroupMemberDtoByGroupIdAndProfileId(group_pk, groupMemberProfileId);
            
            groupMemberMap.put("groupMemberProfileDto", groupMemberProfileDto);
            groupMemberMap.put("groupMemberUserDto", groupMemberUserDto);
            groupMemberMap.put("groupMemberDto", meetingGroupMemberDto);
            
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
        
        int groupId = meetingGroupMemberDto.getGroupId();        
        int curruntMemberCount = countMeetingGroupMember(groupId);
        
        MeetingGroupDto meetingGroupDto = meetingSqlMapper.selectGroupByGroupId(groupId);        
        if(curruntMemberCount < meetingGroupDto.getGroupHeadCount()){
            
            meetingSqlMapper.insertMeetingGroupMember(meetingGroupMemberDto);

            int afterMemberCount = countMeetingGroupMember(groupId);
            if(afterMemberCount == meetingGroupDto.getGroupHeadCount()){
                meetingSqlMapper.updateGroupApplyStatusByGroupId(groupId);                
                System.out.println(groupId + " 번 미팅그룹 이번 등록으로 모집정원 달성 N => Y");
            }
        }
        else{
            System.out.println(groupId + " 번 미팅그룹 이미 모집달성된 상태임");
        }        
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

    public MeetingKakaoReadyResponseDto kakaoPayReady(KakaoPaymentReqDto kakaoPaymentReqDto){

        meetingSqlMapper.insertKakaoPaymentReq(kakaoPaymentReqDto);
        String userId = kakaoPaymentReqDto.getPartner_user_id();
        String orderId = kakaoPaymentReqDto.getPartner_order_id();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", kakaoPaymentReqDto.getCid());
		parameters.add("partner_order_id", orderId);
		parameters.add("partner_user_id",userId);
		parameters.add("item_name", kakaoPaymentReqDto.getItem_name());
		parameters.add("item_code", kakaoPaymentReqDto.getItem_code());
		parameters.add("quantity", Integer.toString(kakaoPaymentReqDto.getQuantity()));
		parameters.add("total_amount", Integer.toString(kakaoPaymentReqDto.getTotal_amount()));
		parameters.add("tax_free_amount", Integer.toString(kakaoPaymentReqDto.getTax_free_amount()));
		
        // // 집 IP
        // parameters.add("approval_url", "https://220.120.230.170:8888/meeting/kakaoPayApproval?userId=" + userId + "&orderId=" + orderId); // 결제승인시 넘어갈 url
		// parameters.add("cancel_url", "https://220.120.230.170:8888/meeting/kakaoPayCancel?userId=" + userId + "&orderId=" + orderId); // 결제취소시 넘어갈 url
		// parameters.add("fail_url", "https://220.120.230.170:8888/meeting/kakaoPayFail?userId=" + userId + "&orderId=" + orderId); // 결제 실패시 넘어갈 url
        
        // 학원 IP
        parameters.add("approval_url", "https://172.30.1.36:8888/meeting/kakaoPayApproval?userId=" + userId + "&orderId=" +orderId); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "https://172.30.1.36:8888/meeting/kakaoPayCancel?userId=" + userId + "&orderId=" +orderId); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "https://172.30.1.36:8888/meeting/kakaoPayFail?userId=" + userId + "&orderId=" +orderId); // 결제 실패시 넘어갈 url
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        
        // PC용 테스트 진행 코드
        MeetingKakaoReadyResponseDto meetingKakaoReadyResponseDto = restTemplate.postForObject(url, requestEntity, MeetingKakaoReadyResponseDto.class);

        // KakaoPaymentResDto kakaoPaymentResDto = restTemplate.postForObject(url, requestEntity, KakaoPaymentResDto.class);
        // kakaoPaymentResDto.setCreated_at(LocalDateTime.now());

        // meetingSqlMapper.insertKakaoPaymentRes(kakaoPaymentResDto);        
        
        return meetingKakaoReadyResponseDto;
        
    }

    // 카카오페이 결제 승인요청 메서드
	public MeetingKakaoApproveResponseDto kakaoPayApprove(MeetingKakaoApproveReqDto meetingKakaoApproveReqDto) {
				
		// request값 담기.
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("tid", meetingKakaoApproveReqDto.getTid());
		parameters.add("partner_order_id", meetingKakaoApproveReqDto.getPartner_order_id()); // 주문명
		parameters.add("partner_user_id", meetingKakaoApproveReqDto.getPartner_user_id());
		parameters.add("pg_token", meetingKakaoApproveReqDto.getPg_token());
		
        // 하나의 map안에 header와 parameter값을 담아줌.
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
        // 외부url 통신
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
		MeetingKakaoApproveResponseDto meetingKakaoApproveResponseDto = template.postForObject(url, requestEntity, MeetingKakaoApproveResponseDto.class);
		System.out.println("결재승인 응답객체: " + meetingKakaoApproveResponseDto);
		
		return meetingKakaoApproveResponseDto;
	}
    
	
    
    // header() 셋팅
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK 326a50751a1bfac7c3ce3006c0ad9581");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}

    public OrderInfoDto registerOrderInfoProcess(int profileId, int groupId){

        System.out.println("registerOrderInfoProcess 실행됨");
        String order_id = "";
        ItemInfoDto itemInfoDto = meetingSqlMapper.selectItemInfoDtoByGroupId(groupId);
        int item_id = itemInfoDto.getItem_id();
        String itemId = Integer.toString(item_id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderDate = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,10);
        String meetingProductCode = "MT";

        order_id = meetingProductCode + itemId + orderDate + uuid;

        System.out.println("order_id : " + order_id);

        int user_id = meetingSqlMapper.selectMeetingProfileByProfileId(profileId).getUser_id();
        
        OrderInfoDto reqOrderInfoDto = new OrderInfoDto();
        reqOrderInfoDto.setOrder_id(order_id);
        reqOrderInfoDto.setItem_id(item_id);
        reqOrderInfoDto.setUser_id(user_id);

        meetingSqlMapper.insertOrderInfo(reqOrderInfoDto);

        OrderInfoDto resOrderInfoDto = meetingSqlMapper.selectOrderInfoByOrderId(order_id);        

        return resOrderInfoDto;
    }

    // * 결제 완료 후 미팅-선발멤버 테이블에서 결제내역 업데이트
    public void updateGroupMemberPaymentStatus(int groupId, int userId){
        MeetingProfileDto meetingProfileDto = meetingSqlMapper.selectMeetingProfileByUserId(userId);
        int profileId = meetingProfileDto.getProfileid();
        meetingSqlMapper.updateGroupMemberPaymentStatusByGroupIdAndProfileId(groupId, profileId);
    }

    // * 미팅그룹 후기 작성
    public void registerGroupReview(MeetingGroupReviewDto meetingGroupReviewDto){
        meetingSqlMapper.insertGroupReviewDto(meetingGroupReviewDto);
    }

    // * 선발멤버PK 기준 미팅그룹 리뷰 존재 확인
    public String checkExistGroupReviewByGroupMemberId(int groupMemberId){
        int result = meetingSqlMapper.countIsExistGroupReviewByGroupMemberId(groupMemberId);
        if(result > 0){
            return "Y";
        }
        else{
            return "N";
        }
        
    }

    // * 모집글PK 기준 해당 미팅 선발멤버가 남긴 리뷰데이터 조회
    public List<Map<String, Object>> getGroupMemberReviewList(int groupId){
        
        List<Map<String, Object>> groupMemberReviewDataList = new ArrayList<>();
        
        List<Map<String, Object>> list =  meetingSqlMapper.selectGroupMemberListByGroupIdForAJAX(groupId);
        for(Map<String, Object> map : list){            
            Map<String, Object> groupMemberReviewMap = new HashMap<>();            
            int groupMemberId = (int)map.get("groupMemberId");            
            MeetingGroupReviewDto meetingGroupReviewDto = meetingSqlMapper.selectGroupReviewByGroupMemberId(groupMemberId);
            groupMemberReviewMap.put("groupMemberDto", map);
            groupMemberReviewMap.put("groupMemberReviewDto", meetingGroupReviewDto);
            
            groupMemberReviewDataList.add(groupMemberReviewMap);
        }        
        return groupMemberReviewDataList;
    }

    // * 그룹멤버 누가/누구에게PK 기준 리뷰존재여부 확인
    public String checkExistGroupMemberReviewByFromIdAndToId(int groupMemberIdFrom, int groupMemberIdTo){
        
        int result = meetingSqlMapper.countIsExistGroupMemberReviewByFromIdAndToId(groupMemberIdFrom, groupMemberIdTo);
        if(result > 0){
            return "Y";
        }
        else{
            return "N";
        }
    }

    // * 그룹멤버 테이블 인서트
    public void registerGroupMemberReview(MeetingMemberReviewDto meetingMemberReviewDto){
        meetingSqlMapper.insertGroupMemberReviewDto(meetingMemberReviewDto);
    }

    // * 그룹멤버PK 기준 모집글리뷰,내가 선발한 (멤버리뷰,킹퀸선발내역,쌍방호감도(From, To)) 내역 조회
    public Map<String, Object> getUserReviewDataAll(int groupMemberId){
        
        Map<String, Object> userReviewDataMap = new HashMap<>();
        
        MeetingGroupReviewDto meetingGroupReviewDto = meetingSqlMapper.selectGroupReviewByGroupMemberId(groupMemberId);
        List<MeetingMemberReviewDto> meetingMemberReviewDtoList = meetingSqlMapper.selectgGroupMemberReviewFromByGroupMemberId(groupMemberId);
        List<MeetingVoteBestMemberDto> meetingVoteBestMemberDtoList = meetingSqlMapper.selectVoteBestMemberFromByGroupMemberId(groupMemberId);
        List<MeetingBothLikeDto> meetingBothLikeFromList = meetingSqlMapper.selectBothLikeFromByGroupMemberId(groupMemberId);
        List<MeetingBothLikeDto> meetingBothLikeToList = meetingSqlMapper.selectBothLikeToByGroupMemberId(groupMemberId);

        userReviewDataMap.put("meetingGroupReviewDto", meetingGroupReviewDto);
        userReviewDataMap.put("meetingMemberReviewDtoList", meetingMemberReviewDtoList);
        userReviewDataMap.put("meetingVoteBestMemberDtoList", meetingVoteBestMemberDtoList);
        userReviewDataMap.put("meetingBothLikeFromList", meetingBothLikeFromList);
        userReviewDataMap.put("meetingBothLikeToList", meetingBothLikeToList);
        
        return userReviewDataMap;
    }

    // * 모집글PK, 미팅프로필PK 기준 미팅확정멤버Dto 셀렉트
    public MeetingGroupMemberDto getGroupMemberDtoByGroupIdAndProfileId(int groupId, int profileId){
        return meetingSqlMapper.selectGroupMemberDtoByGroupIdAndProfileId(groupId, profileId);
    }

    // * 그룹멤버PK기준(누가, 누구에게) 쌍방호감도 인서트
    public void registerBothLike(int groupMemberIdTo, int groupMemberIdFrom){
        meetingSqlMapper.insertBothLikeDto(groupMemberIdTo, groupMemberIdFrom);
    }

    // * 접속유저PK 기준 SNS Dto 셀렉트
    public MeetingSNSDto getSNSDtoByProfileId(int profileId){
        return meetingSqlMapper.selectSNSDtoByProfileId(profileId);
    }

    // * 프로필PK기준 그룹멤버Dto 셀렉트 후 해당 그룹멤버PK 기준 리뷰 및 호감도데이터 자료화
    public List<Map<String, Object>> getGroupMemberDtoListByProfileId(int profileId){        
        
        List<Map<String, Object>> userInfoData = new ArrayList<>();
        
        List<MeetingGroupMemberDto> list = meetingSqlMapper.selectGroupMemberDtoListByProfileId(profileId);
        for(MeetingGroupMemberDto meetingGroupMemberDto : list){
            
            Map<String, Object> map = new HashMap<>();
            
            int groupMemberId = meetingGroupMemberDto.getGroupMemberId();
            Map<String, Object> map2 = getUserReviewDataAll(groupMemberId);
            
            map.put("meetingGroupMemberDto", meetingGroupMemberDto);
            map.put("userReviewDataMap", map2);
            userInfoData.add(map);
        }
        return userInfoData;
    }

    // * 그룹멤버PK기준 내가 보낸 시그널 정보 및 대상 프로필 셀렉트
    public List<List<Map<String, Object>>> getUserBothLikeInfo(int[] userGroupMemberIdList){

        List<List<Map<String, Object>>> userLikeDataList = new ArrayList<>();        
        
        for(int userGroupMemberId : userGroupMemberIdList){
            List<Map<String, Object>> mapList = new ArrayList<>();
        
            List<MeetingBothLikeDto> meetingBothLikeFromList = meetingSqlMapper.selectBothLikeFromByGroupMemberId(userGroupMemberId);
            
            for(MeetingBothLikeDto meetingBothLikeFromDto : meetingBothLikeFromList){
                
                Map<String, Object> map = new HashMap<>();

                int targetGroupMemberId = meetingBothLikeFromDto.getGroupMemberIdTo();
                
                MeetingGroupMemberDto targetGroupMemberDto = meetingSqlMapper.selectGroupMemberDtoByGroupMemberId(targetGroupMemberId);            
                int targetProfileId = targetGroupMemberDto.getProfileId();
                
                MeetingProfileDto targetProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(targetProfileId);

                List<MeetingBothLikeDto> targetBothLikeFromDtoList = meetingSqlMapper.selectBothLikeFromByGroupMemberId(targetGroupMemberId);

                for(MeetingBothLikeDto targetBothLikeFromDto : targetBothLikeFromDtoList){
                    int targetLikeToGroupMemberId = targetBothLikeFromDto.getGroupMemberIdTo();
                    if(targetLikeToGroupMemberId == userGroupMemberId){
                        int meLikeGroupMemberId = targetBothLikeFromDto.getGroupMemberIdFrom();
                        MeetingGroupMemberDto meLikeGroupMemberDto = meetingSqlMapper.selectGroupMemberDtoByGroupMemberId(meLikeGroupMemberId);
                        int meLikeGroupMemberProfileId = meLikeGroupMemberDto.getProfileId();
                        MeetingProfileDto meLikeGroupMemberProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(meLikeGroupMemberProfileId);

                        map.put("meLikeGroupMemberDto", meLikeGroupMemberDto);
                        map.put("meLikeGroupMemberProfileDto", meLikeGroupMemberProfileDto);
                    }
                }
                map.put("targetGroupMemberDto", targetGroupMemberDto);
                map.put("targetProfileDto", targetProfileDto);

                mapList.add(map);
            }
            userLikeDataList.add(mapList);
        }        
        return userLikeDataList;
    }



    // * 새 채팅방 생성
    public void registerChatRoom(int profileId, int targetProfileId){

        int chatRoomPk = meetingSqlMapper.createChatRoomPk();
        
        MeetingChatRoomDto meetingChatRoomDto = new MeetingChatRoomDto();
        meetingChatRoomDto.setChatRoomId(chatRoomPk);
        
        MeetingProfileDto targetMeetingProfileDto = meetingSqlMapper.selectMeetingProfileByProfileId(targetProfileId);
        String targetProfileNickname = targetMeetingProfileDto.getProfileNickname();
        
        meetingChatRoomDto.setChatRoomTitle(targetProfileNickname);

        meetingSqlMapper.insertChatRoomDto(meetingChatRoomDto);
        
    }

    }










}
