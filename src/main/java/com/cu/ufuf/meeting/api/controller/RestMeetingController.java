package com.cu.ufuf.meeting.api.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MeetingApplyUserDto;
import com.cu.ufuf.dto.MeetingChatMessageDto;
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
import com.cu.ufuf.dto.MeetingRestResponseDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
import com.cu.ufuf.dto.MeetingVoteBestMemberDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/meeting/api/*")
public class RestMeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;


    @GetMapping("checkDuplicateSNSUrl")
    public MeetingRestResponseDto checkDuplicateSNSUrl(String value){
        
        int checkedValue = meetingService.checkDuplicateSNSUrl(value);
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(checkedValue);        

        return meetingRestResponseDto;
    }

    @PostMapping("registerProfile")
    public MeetingRestResponseDto registerProfile(
        @RequestParam(name="profile_Img") MultipartFile profile_Img,
        MeetingProfileDto meetingProfileDto,
        MeetingSNSDto meetingSNSDto
    ){
        if(profile_Img != null){
            String originalFilename = profile_Img.getOriginalFilename();
            System.out.println(originalFilename);

            String rootPath = "c:/uploadFiles/ufuf/meeting/profileImage/";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
            String todayPath = sdf.format(new Date());

            File todayFolderForCreate = new File(rootPath + todayPath);
            if(!todayFolderForCreate.exists()){
                todayFolderForCreate.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            long curruntTime = System.currentTimeMillis();
            String fileName = uuid + "_" + curruntTime;

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName += ext;

            try {
                profile_Img.transferTo(new File(rootPath + todayPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            meetingProfileDto.setProfileImg(todayPath + fileName);
        }
        int meetingProfilePk = meetingService.createProfilePk();
        meetingProfileDto.setProfileid(meetingProfilePk);
        meetingSNSDto.setProfileid(meetingProfilePk);

        meetingService.registerNewMeetingProfile(meetingProfileDto);
        meetingService.resgisterNewSNS(meetingSNSDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        
        return meetingRestResponseDto;
    }

    @GetMapping("checkMeetingProfile")
    public MeetingRestResponseDto checkMeetingProfile(int user_id){
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.checkExistMeetingProfile(user_id));
        
        return meetingRestResponseDto;
    }

    @PostMapping("registerGroupProcess")
    public MeetingRestResponseDto registerGroupProcess(
        HttpSession session,
        MeetingGroupDto meetingGroupDto,
        MeetingFirstLocationCategoryDto meetingFirstLocationCategoryDto,
        MeetingSecondLocationCategoryDto meetingSecondLocationCategoryDto,        
        String[] tagNameList,
        MultipartFile group_image
    ){

        int groupPk = meetingService.createGroupPk();        
        meetingGroupDto.setGroupId(groupPk);
        // meetingGroupDto.setProfileId((int)groupData.get("profileId"));
        // meetingGroupDto.setGroupTitle((String)(groupData.get("groupTitle")));
        // meetingGroupDto.setGroupContent((String)groupData.get("groupContent"));
        // meetingGroupDto.setGroupHeadCount(Integer.parseInt((String)(groupData.get("groupHeadCount"))));
        // meetingGroupDto.setGroupMeetingDate(LocalDateTime.parse((String)groupData.get("groupMeetingDate")));
        // meetingGroupDto.setGroupApplyStart(LocalDate.parse((String)groupData.get("groupApplyStart")));
        // meetingGroupDto.setGroupApplyEnd(LocalDate.parse((String)groupData.get("groupApplyEnd")));
        // meetingGroupDto.setGroupApplyCharge(Integer.parseInt((String)groupData.get("groupApplyCharge")));
        // meetingGroupDto.setGroupMeetingPlace((String)groupData.get("groupMeetingPlace"));
        // meetingGroupDto.setGroupGenderOption((String)groupData.get("groupGenderOption"));
        
        
        
        int firstLocationCategoryId = meetingService.createFirstLocationCategoryPk();
        meetingFirstLocationCategoryDto.setFirstLocationCategoryId(firstLocationCategoryId);                
        
        int secondLocationCategoryId = meetingService.createSecondLocationCategoryPk();
        meetingSecondLocationCategoryDto.setSecondLocationCategoryId(secondLocationCategoryId);        
        
        meetingService.registerLocationCategory(meetingFirstLocationCategoryDto, meetingSecondLocationCategoryDto);        
        
        MeetingGroupFirstLocationCategoryDto meetingGroupFirstLocationCategoryDto = new MeetingGroupFirstLocationCategoryDto();
        meetingGroupFirstLocationCategoryDto.setGroupId(groupPk);
        meetingGroupFirstLocationCategoryDto.setFirstLocationCategoryId(firstLocationCategoryId);
        
        MeetingGroupSecondLocationCategoryDto meetingGroupSecondLocationCategoryDto = new MeetingGroupSecondLocationCategoryDto();
        meetingGroupSecondLocationCategoryDto.setGroupId(groupPk);
        meetingGroupSecondLocationCategoryDto.setSecondLocationCategoryId(secondLocationCategoryId);

        meetingService.registerGroupLocationCategory(meetingGroupFirstLocationCategoryDto, meetingGroupSecondLocationCategoryDto);
        
        for(String tagName : tagNameList){            
            int tagPk = meetingService.createTagPk();
            MeetingTagDto meetingTagDto = new MeetingTagDto();            
            meetingTagDto.setTagName(tagName);
            meetingTagDto.setTagId(tagPk);

            meetingService.registerTag(meetingTagDto);
            
            MeetingGroupTagDto meetingGroupTagDto = new MeetingGroupTagDto();
            meetingGroupTagDto.setGroupId(groupPk);
            meetingGroupTagDto.setTagId(tagPk);
            
            meetingService.registerGroupTag(meetingGroupTagDto);
        }

        if(group_image != null){
            String originalFilename = group_image.getOriginalFilename();
            System.out.println(originalFilename);

            String rootPath = "c:/uploadFiles/ufuf/meeting/groupImage/";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
            String todayPath = sdf.format(new Date());

            File todayFolderForCreate = new File(rootPath + todayPath);
            if(!todayFolderForCreate.exists()){
                todayFolderForCreate.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            long curruntTime = System.currentTimeMillis();
            String fileName = uuid + "_" + curruntTime;

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName += ext;

            try {
                group_image.transferTo(new File(rootPath + todayPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            meetingGroupDto.setGroupImage(todayPath + fileName);
        }


        meetingService.registerNewGroup(meetingGroupDto);
        
        MeetingGroupMemberDto meetingGroupMemberDto = new MeetingGroupMemberDto();
        meetingGroupMemberDto.setGroupId(groupPk);
        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileDto");
        meetingGroupMemberDto.setProfileId(meetingProfileDto.getProfileid());
        meetingService.registerGroupMember(meetingGroupMemberDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        
        return meetingRestResponseDto;
    }

    @GetMapping("getGroupList")
    public MeetingRestResponseDto getGroupList(@RequestParam(name="searchKeyword", defaultValue = "") String searchKeyword){
        
        System.out.println("searchKeyword : " + searchKeyword);
        
        if(searchKeyword.equals("")){
            System.out.println("검색키워드 없이 그룹리스트 가져옴");
            List<MeetingGroupDto> groupList = meetingService.getGroupListAll();
            
            MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();
    
            meetingRestResponseDto.setResult("success");
            meetingRestResponseDto.setData(groupList);
            return meetingRestResponseDto;
        }
        else{
            System.out.println("검색키워드로 검색한 그룹리스트 가져옴");
            List<MeetingGroupDto> searchWordGroupList = meetingService.searchMeetingGroupBySearchKeyword(searchKeyword);
            for(MeetingGroupDto group : searchWordGroupList){
                System.out.println("검색결과 모집글 타이틀 : " + group.getGroupTitle());
            }{

            }
            MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();
    
            meetingRestResponseDto.setResult("success");
            meetingRestResponseDto.setData(searchWordGroupList);
            return meetingRestResponseDto;
        }

    }

    @GetMapping("getGroupDetailInfo")
    public MeetingRestResponseDto getGroupDetailInfo(int groupId){
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getGroupDetailInfo(groupId));
        return meetingRestResponseDto;
    }

    @PostMapping("registerApplyUser")
    public MeetingRestResponseDto registerApplyUser(@RequestBody MeetingApplyUserDto meetingApplyUserDto){
        // System.out.println("registerApplyUser 실행됨");
        // System.out.println(meetingApplyUserDto.getGroupId());
        // System.out.println(meetingApplyUserDto.getProfileId());
        // System.out.println(meetingApplyUserDto.getApplyComment());

        meetingService.registerMeetingApplyUser(meetingApplyUserDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        
        return meetingRestResponseDto;
    }

    @GetMapping("applyCheck")
    public MeetingRestResponseDto applyCheck(int profileId, int groupId){
        // System.out.println("applyCheck 실행됨");
        // System.out.println("profileId : " + profileId);
        // System.out.println("groupId : " + groupId);

        int result = meetingService.checkExistApplyUser(profileId, groupId);
        // System.out.println("result : " + result);
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(result);
        return meetingRestResponseDto;
    }

    @GetMapping("getMyRecruitMeetingList")
    public MeetingRestResponseDto getMyRecruitMeetingList(HttpSession session){

        MeetingProfileDto meetingProfileDto = (MeetingProfileDto)session.getAttribute("meetingProfileDto");
        
        int profileId = meetingProfileDto.getProfileid();
        List<Map<String, Object>> list = new ArrayList<>();
        
        List<MeetingGroupDto> myMeetingGroupDtoList = meetingService.getMeetingGroupListByProfilePk(profileId);
        
        for(MeetingGroupDto myMeetingGroupDto : myMeetingGroupDtoList){
            Map<String, Object> map = new HashMap<>();
            int meetingGroupId = myMeetingGroupDto.getGroupId();
            List<Map<String, Object>> groupMemberList = meetingService.getGroupMemberListForAJAX(meetingGroupId);

            map.put("meetingGroupDto", myMeetingGroupDto);
            map.put("groupMemberList", groupMemberList);

            list.add(map);
        }

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(list);
        return meetingRestResponseDto;
    }

    @GetMapping("getMeetingGroupMemberList")
    public MeetingRestResponseDto getMeetingGroupMemberList(int groupId){

        meetingService.getGroupMemberListForAJAX(groupId);
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getGroupMemberListForAJAX(groupId));
        return meetingRestResponseDto;
    }

    @PostMapping("addGroupMember")
    public MeetingRestResponseDto addGroupMember(MeetingGroupMemberDto params){

        // System.out.println("addGroupMember 실행됨");
        int groupId = params.getGroupId();
        int profileId = params.getProfileId();        

        meetingService.registerGroupMember(params);
        meetingService.updateApplyUserApplyStatus(groupId, profileId);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        
        return meetingRestResponseDto;
    }

    @GetMapping("countMeetingGroupMember")
    public MeetingRestResponseDto countMeetingGroupMember(int groupId){        

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.countMeetingGroupMember(groupId));
        return meetingRestResponseDto;
    }

    @GetMapping("getMyApplyGroupData")
    public MeetingRestResponseDto getMyApplyGroupData(int profileId){        

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getApplyDataByProfileIdForAJAX(profileId));
        return meetingRestResponseDto;
    }

    @PostMapping("kakaoPayReady")
    public MeetingRestResponseDto kakaoPayReady(HttpSession session,@RequestBody KakaoPaymentReqDto kakaoPaymentReqDto){
        System.out.println("kakaoPayReady 실행됨");
        
        System.out.println("Cid : " + kakaoPaymentReqDto.getCid());
        System.out.println("Partner_user_id : " + kakaoPaymentReqDto.getPartner_user_id());
        System.out.println("Partner_order_id : " + kakaoPaymentReqDto.getPartner_order_id());
        System.out.println("Item_code : " + kakaoPaymentReqDto.getItem_code());
        System.out.println("Item_name : " + kakaoPaymentReqDto.getItem_name());
        System.out.println("Quantity : " + kakaoPaymentReqDto.getQuantity());
        System.out.println("Total_amount : " + kakaoPaymentReqDto.getTotal_amount());
        System.out.println("Tax_free_amount : " + kakaoPaymentReqDto.getTax_free_amount());        
        
        // PC 테스트용 코드
        MeetingKakaoReadyResponseDto meetingKakaoReadyResponseDto = meetingService.kakaoPayReady(kakaoPaymentReqDto);
        session.setAttribute("tid", meetingKakaoReadyResponseDto.getTid());
		System.out.println("결재고유 번호: " + meetingKakaoReadyResponseDto.getTid());
        
        // 모바일용 코드        
        // KakaoPaymentResDto kakaoPaymentResDto = meetingService.kakaoPayReady(kakaoPaymentReqDto);
        // model.addAttribute("tid", kakaoPaymentResDto.getTid());
		// System.out.println("결재고유 번호: " + kakaoPaymentResDto.getTid());
		// // Order정보를 모델에 저장

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        // meetingRestResponseDto.setData(kakaoPaymentResDto);
        meetingRestResponseDto.setData(meetingKakaoReadyResponseDto);
        return meetingRestResponseDto;		
    }

    @GetMapping("registerOrderProcess")
    public MeetingRestResponseDto registerOrderProcess(int profileId, int groupId){

        System.out.println("registerOrderProcess 실행됨");

        OrderInfoDto resOrderInfoDto = meetingService.registerOrderInfoProcess(profileId, groupId);        

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(resOrderInfoDto);
        return meetingRestResponseDto;
    }

    @PostMapping("kakaoPaymentAcceptRequestProcess")
    public MeetingRestResponseDto kakaoPaymentAcceptRequestProcess(@RequestBody MeetingKakaoApproveReqDto meetingKakaoApproveReqDto){

        System.out.println("kakaoPaymentAcceptRequestProcess 실행됨");

        System.out.println("tid : " + meetingKakaoApproveReqDto.getTid());
        System.out.println("pg_token : " + meetingKakaoApproveReqDto.getPg_token());
        System.out.println("Partner_order_id : " + meetingKakaoApproveReqDto.getPartner_order_id());
        System.out.println("Partner_user_id : " + meetingKakaoApproveReqDto.getPartner_user_id());

        MeetingKakaoApproveResponseDto meetingKakaoApproveResponseDto = meetingService.kakaoPayApprove(meetingKakaoApproveReqDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingKakaoApproveResponseDto);
        return meetingRestResponseDto;
    }

    @GetMapping("changePaymentStatus")
    public MeetingRestResponseDto changePaymentStatus(int groupId, int userId){

        meetingService.updateGroupMemberPaymentStatus(groupId, userId);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        return meetingRestResponseDto;
    }

    @GetMapping("isGroupReviewExist")
    public MeetingRestResponseDto isGroupReviewExist(int groupMemberId){

        String resultValue = meetingService.checkExistGroupReviewByGroupMemberId(groupMemberId);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(resultValue);
        return meetingRestResponseDto;
    }

    @PostMapping("registerGroupReviewProcess")
    public MeetingRestResponseDto registerGroupReviewProcess(MeetingGroupReviewDto meetingGroupReviewDto){
        int groupMemberId = meetingGroupReviewDto.getGroupMemberId();
        String checkedValue = meetingService.checkExistGroupReviewByGroupMemberId(groupMemberId);
        if(checkedValue == "Y"){
            MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();
            meetingRestResponseDto.setResult("fail");
            return meetingRestResponseDto;
        }
        else{
            meetingService.registerGroupReview(meetingGroupReviewDto);            
            MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();
            meetingRestResponseDto.setResult("success");            
            return meetingRestResponseDto;
        }
    }

    @GetMapping("getGroupMemberReview")
    public MeetingRestResponseDto getGroupMemberReview(int groupId){

        List<Map<String, Object>> groupMemberReviewList = meetingService.getGroupMemberReviewList(groupId);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(groupMemberReviewList);
        return meetingRestResponseDto;
    }

    @GetMapping("isMemberReviewExist")
    public MeetingRestResponseDto isMemberReviewExist(int groupMemberIdFrom, int groupMemberIdTo){
        String reseult = meetingService.checkExistGroupMemberReviewByFromIdAndToId(groupMemberIdFrom, groupMemberIdTo);
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(reseult);
        return meetingRestResponseDto;
    }

    @PostMapping("registerGroupMemberReview")
    public MeetingRestResponseDto registerGroupMemberReview(@RequestBody MeetingMemberReviewDto meetingMemberReviewDto){

        meetingService.registerGroupMemberReview(meetingMemberReviewDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        return meetingRestResponseDto;
    }

    @GetMapping("getSessionUserReviewDataAll")
    public MeetingRestResponseDto getSessionUserReviewDataAll(int groupMemberId){

        Map<String, Object> map = meetingService.getUserReviewDataAll(groupMemberId);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(map);
        return meetingRestResponseDto;
    }

    @GetMapping("registerBothLike")
    public MeetingRestResponseDto registerBothLike(int groupMemberIdTo, int groupMemberIdFrom){

        meetingService.registerBothLike(groupMemberIdTo, groupMemberIdFrom);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        return meetingRestResponseDto;
    }

    @PostMapping("getUserMySignalToList")
    public MeetingRestResponseDto getUserMySignalToList(@RequestBody int[] userGroupMemberIdList){

        // for(int x : userGroupMemberIdList){
        //     System.out.println("그룹멤버id : " + x);
        // }

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getUserBothLikeInfo(userGroupMemberIdList));
        return meetingRestResponseDto;
    }

    @GetMapping("createChatRoom")
    public MeetingRestResponseDto createChatRoom(int profileId, int targetProfileId){

        meetingService.registerChatRoom(profileId, targetProfileId);
        
        // 채팅방 생성 이후 다른 서비스 호출로 알람? 메세지? 보내주는거 구현?
        // targetProfileId 사용

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getUserChatData(profileId));
        return meetingRestResponseDto;
    }

    @GetMapping("checkExistChatRoom")
    public MeetingRestResponseDto checkExistChatRoom(int profileId, int targetProfileId){
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.checkExistChatRoom(profileId, targetProfileId));
        return meetingRestResponseDto;
    }

    @GetMapping("getUserChatRoomData")
    public MeetingRestResponseDto getUserChatRoomData(int profileId){
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getUserChatData(profileId));
        return meetingRestResponseDto;
    }

    @GetMapping("getChatMessageData")
    public MeetingRestResponseDto getChatMessageData(int chatRoomId){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getChatMessageList(chatRoomId));
        return meetingRestResponseDto;
    }

    @PostMapping("registerChatMessage")
    public MeetingRestResponseDto registerChatMessage(@RequestBody MeetingChatMessageDto params){

        // System.out.println(params.getChatRoomId());
        // System.out.println(params.getChatRoomUserId());
        // System.out.println(params.getChatComment());
        
        meetingService.registerChatMessage(params);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        
        return meetingRestResponseDto;
    }

    @PostMapping("updateGroupMeetingStatus")
    public MeetingRestResponseDto updateGroupMeetingStatus(@RequestBody int[] groupIdList){
        // for(int x : groupIdList){
        //     System.out.println("groupId : " + x);
        // }
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.updateMeetingStatus(groupIdList));        
        return meetingRestResponseDto;
    }

    @PostMapping("updateApplyStatus")
    public MeetingRestResponseDto updateApplyStatus(@RequestBody int[] groupIdList){        

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.updateApplyStatus(groupIdList));
        return meetingRestResponseDto;
    }

    @GetMapping("increaseReadCount")
    public MeetingRestResponseDto increaseReadCount(int groupId){

        meetingService.updateGroupReadCount(groupId);
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        return meetingRestResponseDto;
    }

    @GetMapping("getHotMeetingList")
    public MeetingRestResponseDto getHotMeetingList(){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getHotMeetingGroup());
        return meetingRestResponseDto;
    }

    @GetMapping("getNewMeetingList")
    public MeetingRestResponseDto getNewMeetingList(){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.getNewMeetingGroup());
        return meetingRestResponseDto;
    }

    @GetMapping("checkExistVoteBestMember")
    public MeetingRestResponseDto checkExistVoteBestMember(int groupMemberId){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.checkIsExistVoteBestMemberByGroupMemberId(groupMemberId));
        return meetingRestResponseDto;
    }

    @GetMapping("voteBestMember")
    public MeetingRestResponseDto voteBestMember(int groupMemberIdFrom, int groupMemberIdTo){
        
        MeetingVoteBestMemberDto meetingVoteBestMemberDto = new MeetingVoteBestMemberDto();
        meetingVoteBestMemberDto.setGroupMemberIdFrom(groupMemberIdFrom);
        meetingVoteBestMemberDto.setGroupMemberIdTo(groupMemberIdTo);
        meetingService.registerVoteBestMember(meetingVoteBestMemberDto);
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        return meetingRestResponseDto;
    }




    
    // 템플릿 코드
    public MeetingRestResponseDto templete(){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(1);
        return meetingRestResponseDto;
    }
}
