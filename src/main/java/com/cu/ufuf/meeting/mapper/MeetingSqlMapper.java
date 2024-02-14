package com.cu.ufuf.meeting.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MeetingApplyUserDto;
import com.cu.ufuf.dto.MeetingBothLikeDto;
import com.cu.ufuf.dto.MeetingChatMessageDto;
import com.cu.ufuf.dto.MeetingChatRoomDto;
import com.cu.ufuf.dto.MeetingChatRoomUserDto;
import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupMemberDto;
import com.cu.ufuf.dto.MeetingGroupReviewDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingMemberReviewDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
import com.cu.ufuf.dto.MeetingVoteBestMemberDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface MeetingSqlMapper {

    // * PK 생성용
    public int countProfilePk();
    public int countGroupPk();
    public int countFirstLocationCategoryForPk();
    public int countSecondLocationCategoryForPk();
    public int countTagForPk();

    
    public int countSNSUrlByInputUrlValue(String value);

    public void insertMeetingProfile(MeetingProfileDto meetingProfileDto);

    public void insertNewSNS(MeetingSNSDto meetingSNSDto);

    public int countMeetingProfileByUserId(int user_id);

    public MeetingProfileDto selectMeetingProfileByUserId(int user_id);

    public void insertNewGroup(MeetingGroupDto meetingGroupDto);

    public void insertFirstLocationCategory(MeetingFirstLocationCategoryDto meetingFirstLocationCategoryDto);

    public void insertSecondLocationCategory(MeetingSecondLocationCategoryDto meetingSecondLocationCategoryDto);

    public void insertGroupFirstLocationCategory(MeetingGroupFirstLocationCategoryDto meetingGroupFirstLocationCategoryDto);

    public void insertGroupSecondLocationCategory(MeetingGroupSecondLocationCategoryDto meetingGroupSecondLocationCategoryDto);

    public void insertTag(MeetingTagDto meetingTagDto);

    public void insertGroupTag(MeetingGroupTagDto meetingGroupTagDto);

    public List<MeetingGroupDto> selectGroupListAll();

    // * 그룹PK 기준 모집글 셀렉트
    public MeetingGroupDto selectGroupByGroupId(int groupId);

    // * 그룹PK 기준 모집글 태그 셀렉트
    public List<MeetingGroupTagDto> selectGroupTagListByGroupId(int groupId);

    // * 미팅프로필PK 기준 프로필정보 셀렉트
    public MeetingProfileDto selectMeetingProfileByProfileId(int profileId);

    // * 태그PK 기준 태그 셀렉트
    public MeetingTagDto selectTagByTagId(int tagId);


    // * 미팅 신청자 인서트
    public void insertMeetingApplyUser(MeetingApplyUserDto meetingApplyUserDto);

    // * 로그인 유저가 해당 미팅 신청내역 존재여부 확인
    public int countMeetingApplyUserByProfileId(int profileId, int groupId);

    // * 미팅 모집글에 신청한 신청자리스트 셀렉트
    public List<MeetingApplyUserDto> selectGroupApplyUserList(int groupId);

    // * 접속유저 프로필PK기준 미팅모집글 리스팅
    public List<MeetingGroupDto> selectMeetingGroupListByProfilePk(int profileId);

    // * 미팅 확정멤버 인서트
    public void insertMeetingGroupMember(MeetingGroupMemberDto meetingGroupMemberDto);

    // * 미팅 모집글PK 기준 확정멤버 셀렉트
    public List<MeetingGroupMemberDto> selectMeetingGroupMemberListByGroupPk(int groupId);

    // * 미팅 모집글PK기준 확정멤버 셀렉트(AJAX)
    public List<Map<String, Object>> selectGroupMemberListByGroupIdForAJAX(int groupId);

    // * 미팅 확정멤버됨과 동시에 신청자 리스트에서 업데이트
    public void updateApplyUserApplyStatus(int groupId, int profileId);

    // * 미팅 모집글PK 기준 확정멤버수 카운트
    public int countMeetingGroupMemberByGroupId(int groupId);

    // * 미팅 모집글PK 기준 신청멤버수 카운트
    public int countMeetingGroupApplyUserByGroupId(int groupId);

    // * 프로필PK 기준 모집신청내역 리스팅
    public List<MeetingApplyUserDto> selectApplyUserByProfileId(int profileId);

    // * 유저PK 기준 유저정보 셀렉트
    public UserInfoDto selectUserInfoByUserId(int user_id);

    // * 상품 테이블에 merchan_id = 그룹PK 로 인서트(모집글 인서트와 동시에 작동)
    public void insertItemInfo(int groupId);

    // * 그룹PK 기준 모집달성현황 업데이트
    public void updateGroupApplyStatusByGroupId(int groupId);

    // * 모집글PK 기준 상품테이블에서 모집글에 해당하는 상품 셀렉트
    public ItemInfoDto selectItemInfoDtoByGroupId(int groupId);

    // * 주문번호 테이블 인서트(미팅방에서 유저 자신의 회비 결제시 인서트)
    public void insertOrderInfo(OrderInfoDto orderInfoDto);

    // * 주문번호PK로 주문번호 Dto셀렉트
    public OrderInfoDto selectOrderInfoByOrderId(String order_id);

    // * 카카오 결제요청 테이블 인서트
    public void insertKakaoPaymentReq(KakaoPaymentReqDto kakaoPaymentReqDto);

    // * 카카오 결제준비응답 테이블 인서트
    public void insertKakaoPaymentRes(KakaoPaymentResDto kakaoPaymentResDto);

    // * 결제 완료 후 미팅-선발멤버 테이블에서 결제내역 업데이트
    public void updateGroupMemberPaymentStatusByGroupIdAndProfileId(int groupId, int profileId);

    // * 모집글PK, 프로필PK 기준 모집글 확정멤버 Dto 셀렉트
    public MeetingGroupMemberDto selectGroupMemberDtoByGroupIdAndProfileId(int groupId, int profileId);

    // * 모집글PK기준 확정멤버 회비 미납인원 카운트
    public int countNotPaidGroupMemberCount(int groupId);

    // * 그룹리뷰 인서트
    public void insertGroupReviewDto(MeetingGroupReviewDto meetingGroupReviewDto);

    // * 선발멤버PK 기준 그룹리뷰 존재 확인 쿼리
    public int countIsExistGroupReviewByGroupMemberId(int groupMemberId);    

    // * 그룹멤버 누가/누구에게PK 기준 리뷰 존재여부 확인
    public int countIsExistGroupMemberReviewByFromIdAndToId(int groupMemberIdFrom, int groupMemberIdTo);

    // * 그룹멤버 테이블 인서트
    public void insertGroupMemberReviewDto(MeetingMemberReviewDto meetingMemberReviewDto);

    // * 그룹멤버PK 기준 그룹리뷰 셀렉트
    public MeetingGroupReviewDto selectGroupReviewByGroupMemberId(int groupMemberId);

    // * 그룹멤버PK 기준 내가 작성한 멤버후기 셀렉트
    public List<MeetingMemberReviewDto> selectgGroupMemberReviewFromByGroupMemberId(int groupMemberIdFrom);

    // * 그룹멤버PK 기준 내가 선발한 킹/퀸선발내역 셀렉트
    public List<MeetingVoteBestMemberDto> selectVoteBestMemberFromByGroupMemberId(int groupMemberIdFrom);

    // * 그룹멤버PK 기준 '내가' 선택한 쌍방호감도 셀렉트
    public List<MeetingBothLikeDto> selectBothLikeFromByGroupMemberId(int groupMemberIdFrom);

    // * 그룹멤버PK 기준 '나를' 선택한 쌍방호감도 셀렉트
    public List<MeetingBothLikeDto> selectBothLikeToByGroupMemberId(int groupMemberIdTo);

    // * 쌍방호감도 테이블 인서트
    public void insertBothLikeDto(int groupMemberIdTo, int groupMemberIdFrom);

    // * 접속유저PK기준 SNS Dto 셀렉트
    public MeetingSNSDto selectSNSDtoByProfileId(int profileId);

    // * 프로필PK기준 그룹멤버PK 셀렉트
    public List<MeetingGroupMemberDto> selectGroupMemberDtoListByProfileId(int profileId);

    // * 그룹멤버PK기준 그룹멤버Dto 셀렉트
    public MeetingGroupMemberDto selectGroupMemberDtoByGroupMemberId(int groupMemberId);


    // * 채팅방 Dto 인서트
    public void insertChatRoomDto(MeetingChatRoomDto meetingChatRoomDto);

    // * 채팅방PK 생성
    public int createChatRoomPk();

    // * 채팅참여유저 Dto 인서트
    public void insertChatRoomUserDto(MeetingChatRoomUserDto meetingChatRoomUserDto);

    // * 프로필PK기준 채팅방제목이 해당프로필 닉네임인 방 셀렉트
    public List<MeetingChatRoomDto> selectChatRoomDtoByProfileNickname(String profileNickname);

    // * 채팅방PK기준 채팅방 메세지 Dto 셀렉트
    public List<MeetingChatMessageDto> selectChatMessageDtoByChatRoomId(int chatRoomId);

    // * 채팅방PK, 프로핑PK기준 채팅방 존재여부 판별
    public int countExistChatRoomByChatRoomIdAndProfileId(int chatRoomId, int profileId);

    // * 채팅방PK 기준 채팅참여유저Dto 셀렉트
    public List<MeetingChatRoomUserDto> selectChatRoomUserDtoByChatRoomId(int chatRoomId);

    // * 프로핑PK기준 채팅참여유저Dto 셀렉트
    public List<MeetingChatRoomUserDto> selectChatRoomUserDtoByProfileId(int profileId);

    // * 채팅방PK기준 채팅방Dto 셀렉트
    public MeetingChatRoomDto selectChatRoomDtoByChatRoomId(int chatRoomId);

    // * 프로필닉네임기준 프로필Dto 셀렉트
    public MeetingProfileDto selectProfileDtoByProfileNickname(String profileNickname);

    // * 프로필PK, 채팅방PK기준 채팅유저인지 확인(카운트)
    public int countChatRoomUserByChatRoomIdAndProfileId(int chatRoomId, int profileId);

    // * 채팅메세지 테이블 인서트
    public void insertChatMessageDto(MeetingChatMessageDto meetingChatMessageDto);

    // * 채팅방PK, 프로필PK로 채팅참여유저Dto 셀렉트
    public MeetingChatRoomUserDto selectChatRoomUserDtoByProfileIdAndChatRoomId(int profileId, int chatRoomId);

    // * 모집글PK기준 미팅완료여부 업데이트
    public void updateMeetingStatus(int groupId);

    // * 모집글PK기준 조회수 업데이트
    public void updateGroupReadCount(int groupId);
    
    // * 모집글 조회수 기준 top5인 모집글PK 셀렉트
    public int[] selectHotMeetingGroupIdList();

    // * 모집글 등록일자 기준 오늘기준 최신글 top5인 모집글PK 셀렉트
    public int[] selectNewMeetingGroupIdList();

    // * 검색키워드로 모집글 검색 및 해당모집글 리스팅
    public List<MeetingGroupDto> selectMeetingGroupListBySearchKeyword(String searchKeyword);

    // * 그룹멤버PK 기준 베스트멤버 테이블 존재여부 확인
    public int countVoteBestMemberByGroupMemberIdFrom(int groupMemberIdFrom);

    // * 베스트멤버 테이블 인서트
    public void insertVoteBestMember(MeetingVoteBestMemberDto meetingVoteBestMemberDto);

    // * 그룹멤버PK기준 베스트멤버로 선정된 횟수 카운트 */
    public int countVoteBestMemberByGroupMemberIdTo(int groupMemberIdTo);



}
