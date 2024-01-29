package com.cu.ufuf.circle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;
import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleBoardImageDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleScheduleApplyDto;
import com.cu.ufuf.dto.CircleScheduleDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.CircleVoteCompleteDto;
import com.cu.ufuf.dto.CircleVoteDto;
import com.cu.ufuf.dto.CircleVoteOptionDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;

@Service
public class CircleService {

    @Autowired
    private CircleSqlMapper circleSqlMapper;

    public List<CircleMiddleCategoryDto> circlemiddlecategoryInfoAll(){

        return circleSqlMapper.circlemiddlecategoryInfoAll();
    }

    public List<CircleSmallCategoryDto> circleSmallCategoryDtos(){

        return circleSqlMapper.circlesmallcategoryInfoAll();
    }
    
    public List<CircleSmallCategoryDto> circlesmallcategoryInfoByMiddleId(int circle_middle_category_id){

        return circleSqlMapper.circlesmallcategoryInfoByMiddleId(circle_middle_category_id);
    }
    public void circleInfoInsert(CircleDto circleDto){

        circleSqlMapper.circleInfoInsert(circleDto);
    }
    
    public void circleNoticeImageInfoInsert(CircleNoticeImageDto circleNoticeImageDto){
        circleSqlMapper.circleNoticeImageInfoInsert(circleNoticeImageDto);
    }

    public int circleIdMaxByUserId(int user_id){
        return circleSqlMapper.circleIdMaxByUserId(user_id);
    }
    // 양식은 똑같음 값만 세팅잘하면됨
    public void cirlceMemberinfoInsert(CircleMemberDto circleMemberDto){
        circleSqlMapper.cirlceMemberinfoInsert(circleMemberDto);
    }

    public List<Map<String, Object>> circleInfoHotThree(){
        
        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleInfoHotThree();

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }
        
        


        return list;

    }
    public List<Map<String, Object>> circleNewListOrderByCircleId(){
        
        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleNewListOrderByCircleId();

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }
        
        


        return list;
    }
    // 이거 자바스크립트로 받을까???? ok 받는법 알아벌임
    public Map<String, Object> circleInfoVarious(int circle_id){

        Map<String, Object> map = new HashMap<>();
        
        CircleDto circleDto = circleSqlMapper.circleInfoByCircleId(circle_id);
        int circle_grade_id = circleDto.getCircle_grade_id();
        int circle_small_category_id = circleDto.getCircle_small_category_id();

        CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
        CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);

        int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
        CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
        
        // 4개정보 받아서 보내면됨
        int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);
        int userId = circleDto.getUser_id();
        UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(userId);

        map.put("userInfoDto", userInfoDto);
        map.put("circleMemberCnt", circleMemberCnt);
        map.put("circleDto", circleDto);
        map.put("circleSmallCategoryDto", circleSmallCategoryDto);
        map.put("circleGradeDto", circleGradeDto);
        map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);

        

        return map;
    }

    public List<CircleNoticeImageDto> circleNoticeImageInfoByCircleId(int circle_id){


        return circleSqlMapper.circleNoticeImageInfoByCircleId(circle_id);
    }
    public void circleJoinApplyInsert(CircleJoinApplyDto circleJoinApplyDto){
        
        circleSqlMapper.circleJoinApplyInsert(circleJoinApplyDto);
    }

    public List<Map<String, Object>> myCircleInfoListAndPosition(int user_id){

        List<Map<String, Object>> list = new ArrayList<>();
        
        List<CircleMemberDto> CircleMemberDtos = circleSqlMapper.circleMemberInfoByUserId(user_id);
        
        for(CircleMemberDto e : CircleMemberDtos){

            Map<String, Object> map = new HashMap<>();

            int circle_id = e.getCircle_id();
            CircleDto circleDto = circleSqlMapper.circleInfoByCircleId(circle_id);
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            int circle_grade_id = circleDto.getCircle_grade_id();
            int circle_small_category_id = circleDto.getCircle_small_category_id();

            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);

            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
        
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleMemberDto", e);
            map.put("circleDto", circleDto);
            
            list.add(map);

        }


        return list;
    }

    public List<Map<String, Object>> circleboardList(int circle_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByCircleId(circle_id);

        // 동아리 회원이 여러명 근데 회원 한명당 글을 여러개 작성할 수도 있단말이야?

        for(CircleMemberDto e : circleMemberDtos){

            int circle_member_id = e.getCircle_member_id();

            List<CircleBoardDto> circleBoardDtos = circleSqlMapper.circleboardInfoByCircleMemberId(circle_member_id);

            if(!(circleBoardDtos.equals(null))){
                for(CircleBoardDto e2 : circleBoardDtos){

                    Map<String, Object> map = new HashMap<>();

                    int user_id = e.getUser_id();
                    UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
                    // 유저정보(프로필사진), 글정보(글내용), 동아리멤버정보(등급)
                    map.put("userInfoDto", userInfoDto);
                    map.put("circleBoardDto", e2);
                    map.put("circleMemberDto", e);
                    
                    list.add(map);
                }
            }


        }

        return list;
    }

    public CircleMemberDto circleMemberInfoByUserIdAndCircleId(int user_id, int circle_id){

        return circleSqlMapper.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
    }

    public void circleboardDtoInsert(CircleBoardDto circleBoardDto){

        circleSqlMapper.circleboardDtoInsert(circleBoardDto);
    }

    public int boardIdMaxByCircleMemberId(int circle_member_id){

        return circleSqlMapper.boardIdMaxByCircleMemberId(circle_member_id);
    }
    public void circleboardImageDtoInsert(CircleBoardImageDto circleBoardImageDto){
        
        circleSqlMapper.circleboardImageDtoInsert(circleBoardImageDto);
    }
    public CircleDto circleInfoByCircleId(int circle_id){

        return circleSqlMapper.circleInfoByCircleId(circle_id);
    }
    public void circleVoteInsert(CircleVoteDto circleVoteDto){
        
        circleSqlMapper.circleVoteInsert(circleVoteDto);
    }
    public void circleVoteOptionInsert(CircleVoteOptionDto circleVoteOptionDto){

        circleSqlMapper.circleVoteOptionInsert(circleVoteOptionDto);
    }
    public int circleVoteMaxCircleVoteId(int circle_member_id){
        
        return circleSqlMapper.circleVoteMaxCircleVoteId(circle_member_id);
    }
    public List<Map<String, Object>> circleVoteAllListInfo(int circle_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByCircleId(circle_id);

        for(CircleMemberDto e : circleMemberDtos){
            
            int circle_member_id = e.getCircle_member_id();
            int user_id = e.getUser_id();

            UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
            
            List<CircleVoteDto> circleVoteDtos = circleSqlMapper.circleVoteBoardListByCircleMemberId(circle_member_id);
            
                if(!circleVoteDtos.equals(null)){
                    for(CircleVoteDto ee : circleVoteDtos){
                        
                        // 게시글 하나하나 나옴 여기서 put add 해야됨
                        Map<String, Object> map = new HashMap<>();
                        int circleVoteId = ee.getCircle_vote_id();
                        List<CircleVoteOptionDto> circleVoteOptionDtos = circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circleVoteId);
                        int count = 0;
                        for(CircleVoteOptionDto circleVoteDto : circleVoteOptionDtos){
                            int voteOptionId = circleVoteDto.getVote_option_id();
                            int voteCnt = circleSqlMapper.circleVoteCompleteCntByVoteOptionId(voteOptionId);
                            count += voteCnt;
                        }
                        map.put("voteCnt", count);
                        map.put("circleMemberDto", e);
                        map.put("userInfoDto", userInfoDto);
                        map.put("circleVoteDto", ee);
                        
                        list.add(map);
                        
                    }
            }
            

        }
        
        return list;
    }

    public CircleVoteDto voteInfoOne(int circle_vote_id){

        return circleSqlMapper.circleVoteInfoByCircleVoteId(circle_vote_id);
    }
    public List<CircleVoteOptionDto> circleVoteOptionInfoByCircleVoteId(int circle_vote_id){

        return circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circle_vote_id);
    }
    public void circleVoteCompleteInfoInsert(CircleVoteCompleteDto circleVoteCompleteDto){
        
        circleSqlMapper.circleVoteCompleteInfoInsert(circleVoteCompleteDto);
    }
    public Boolean voteChecked(int vote_option_id, int circle_member_id){

        return circleSqlMapper.voteChecked(vote_option_id, circle_member_id);
    }
    public void circleScheduleInfoInsert(CircleScheduleDto circleScheduleDto){

        circleSqlMapper.circleScheduleInfoInsert(circleScheduleDto);
    }
    public List<CircleScheduleDto> circleScheduleListAll(int circle_id){

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByCircleId(circle_id);

        List<CircleScheduleDto> list = new ArrayList<>();

        for(CircleMemberDto e : circleMemberDtos){
            int circle_member_id = e.getCircle_member_id();
            List<CircleScheduleDto> circleScheduleDtos = circleSqlMapper.circleScheduleListAllByCircleMemberId(circle_member_id);
            for(CircleScheduleDto ee : circleScheduleDtos){
                list.add(ee);
            }
        }

        return list;
    }
    public void circleScheduleApplicationInfoInsert(CircleScheduleApplyDto circleScheduleApplyDto){

        circleSqlMapper.circleScheduleApplicationInfoInsert(circleScheduleApplyDto);
    }
    public Boolean scheduleApplyCheckByCircleScheduleIdAndCircleMemberId(int circle_member_id, int circle_schedule_id){

        return circleSqlMapper.scheduleApplyCheckByCircleScheduleIdAndCircleMemberId(circle_member_id, circle_schedule_id);
    }

    public Boolean scheduleApplyCheckPAndAByCircleMemberId(int circle_member_id){

        return circleSqlMapper.scheduleApplyCheckPAndAByCircleMemberId(circle_member_id);
    }
    
    public List<Map<String, Object>> circleListOnlyManager(int user_id){

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByUserId(user_id);

        List<Map<String, Object>> list = new ArrayList<>();

        for(CircleMemberDto e : circleMemberDtos){

            String circlePosition = e.getCircle_position();
            

            if(circlePosition.equals("P") || circlePosition.equals("A")){
                Map<String, Object> map = new HashMap<>();
                // 정보 다엮어봐?
                int circle_id = e.getCircle_id();
                CircleDto circleDto = circleSqlMapper.circleInfoByCircleId(circle_id);
                int circle_grade_id = circleDto.getCircle_grade_id();
                CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
                int circle_small_category_id = circleDto.getCircle_small_category_id();
                CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
                int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
                CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
                int memberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);
                int circleBoartCnt = circleSqlMapper.circleBoartCnt(circle_id);
                map.put("circleBoartCnt", circleBoartCnt);
                map.put("memberCnt",memberCnt);
                map.put("circleMemberDto", e);
                map.put("circleDto", circleDto);
                map.put("circleGradeDto", circleGradeDto);
                map.put("circleSmallCategoryDto", circleSmallCategoryDto);
                map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);

                list.add(map);

            }

        }
        
        return list;
    }

    public List<Map<String, Object>> ApprovalJoinAllListByCircleIdAndSubmitN(int circle_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleJoinApplyDto> circleJoinApplyDtos = circleSqlMapper.ApprovalJoinAllListByCircleIdAndSubmitN(circle_id);

        for(CircleJoinApplyDto e : circleJoinApplyDtos){

            Map<String, Object> map = new HashMap<>();

            int user_id = e.getUser_id();

            UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);

            map.put("circleJoinApplyDto", e);
            map.put("userInfoDto", userInfoDto);

            list.add(map);
            
        }

        return list;

    }

    public void circleJoinApplyCompleteUpdateByCircleJoinApplyId(int circle_join_apply_id){

        circleSqlMapper.circleJoinApplyCompleteUpdateByCircleJoinApplyId(circle_join_apply_id);
    }
    public Boolean verificationjoinApplyByUserIdAndCircleId(int circle_id, int user_id){

        return circleSqlMapper.verificationjoinApplyByUserIdAndCircleId(circle_id, user_id);
    }
    public CircleDto circleInfo(int circle_id){

        return circleSqlMapper.circleInfoByCircleId(circle_id);
    }
    public List<CircleBoardImageDto> circleBoardImageInfoByCircleBoardId(int circle_board_id){

        return circleSqlMapper.circleBoardImageInfoByCircleBoardId(circle_board_id);
    }

    public void itemInfoInsert(int circle_schedule_id){
        
        circleSqlMapper.itemInfoInsert(circle_schedule_id);
    }
    public int circleScheduleIdMaxValue(){
        
        return circleSqlMapper.circleScheduleIdMaxValue();
    }
    
    public void kakaoPaymentReqInsert(KakaoPaymentReqDto kakaoPaymentReqDto){
        
        circleSqlMapper.kakaoPaymentReqInsert(kakaoPaymentReqDto);
    }
    public void orderInfoInsert(OrderInfoDto orderInfoDto){

        circleSqlMapper.orderInfoInsert(orderInfoDto);
    }
    public int itemPkGetByCircleScheduleId(int circle_schedule_id){

        return circleSqlMapper.itemPkGetByCircleScheduleId(circle_schedule_id);
    }
    public int userPkByCircleScheduleId(int circle_schedule_id){

        return circleSqlMapper.userPkByCircleScheduleId(circle_schedule_id);
    }
    public String orderIdMax(){
        
        return circleSqlMapper.orderIdMax();
    }

    public void kakaoPaymentResInsert(KakaoPaymentResDto kakaoPaymentResDto){
        
        circleSqlMapper.kakaoPaymentResInsert(kakaoPaymentResDto);
    }
    public void kakaoPaymentAcceptReqInsert(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){

        circleSqlMapper.kakaoPaymentAcceptReqInsert(kakaoPaymentAcceptReqDto);
    }
    public void kakaoPaymentAcceptResInsert(KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto){

        circleSqlMapper.kakaoPaymentAcceptResInsert(kakaoPaymentAcceptResDto);
    }
}
