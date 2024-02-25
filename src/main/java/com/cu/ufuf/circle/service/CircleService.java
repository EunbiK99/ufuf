package com.cu.ufuf.circle.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;
import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleBoardImageDto;
import com.cu.ufuf.dto.CircleBoardLikeDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleLikeDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleScheduleApplyDto;
import com.cu.ufuf.dto.CircleScheduleAttendanceDto;
import com.cu.ufuf.dto.CircleScheduleDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.CircleVoteCompleteDto;
import com.cu.ufuf.dto.CircleVoteDto;
import com.cu.ufuf.dto.CircleVoteOptionDto;
import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
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
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }
        
        


        return list;

    }
    // 신규 개설된 순 리스트
    public List<Map<String, Object>> circleNewListOrderByCircleId(){
        
        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleNewListOrderByCircleId();

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }
        
        


        return list;
    }
    // 인기순 ==> 동아리를 인원순으로 정렬 시킬거
    public List<Map<String, Object>> circleMemberListMemberCntOrderByCircleId(){

        List<Map<String, Object>> list = new ArrayList<>();
        
        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberListMemberCntOrderByCircleId();

        for(CircleMemberDto circleMemberDto : circleMemberDtos){

            Map<String, Object> map = new HashMap<>();

            int circle_id = circleMemberDto.getCircle_id();
            // 여기서 circleList를 가져오고
            CircleDto e = circleSqlMapper.circleInfoByCircleId(circle_id);
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);

        }



        return list;
    }
    // 동아리 리스트 등급별로 가져옴 circleInfoListOrderByGradeId
    public List<Map<String, Object>> circleInfoListOrderByGradeId(){
        
        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleInfoListOrderByGradeId();

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
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

        map.put("userInfoDto", userInfoDto); //개설자 이름 개설자 프로필 사진
        map.put("circleMemberCnt", circleMemberCnt); // 인원수
        map.put("circleDto", circleDto); //개설 날짜
        map.put("circleSmallCategoryDto", circleSmallCategoryDto);
        map.put("circleGradeDto", circleGradeDto); // 등급명 가져오기.. ㅎㅎ; 게시물작성수기준이 있음
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

    // public List<Map<String, Object>> circleboardList(int circle_id){

    //     List<Map<String, Object>> list = new ArrayList<>();
    //     // 동아리 id로 동아리 회원들 리스트를 뽑음
    //     List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByCircleId(circle_id);

    //     // 동아리 회원이 여러명 근데 회원 한명당 글을 여러개 작성할 수도 있단말이야?
    //     // 동아리 맴버 별로 글을 하나씩 뽑는거..? 이게맞나...
    //     for(CircleMemberDto e : circleMemberDtos){

    //         int circle_member_id = e.getCircle_member_id();
    //         // 동아리 회원 한명당 리스트를 뽑음 
    //         List<CircleBoardDto> circleBoardDtos = circleSqlMapper.circleboardInfoByCircleMemberId(circle_member_id);

    //         if(!(circleBoardDtos.equals(null))){
    //             for(CircleBoardDto e2 : circleBoardDtos){

    //                 Map<String, Object> map = new HashMap<>();

    //                 int user_id = e.getUser_id();
    //                 UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
    //                 // 유저정보(프로필사진), 글정보(글내용), 동아리멤버정보(등급)
    //                 map.put("userInfoDto", userInfoDto);
    //                 map.put("circleBoardDto", e2);
    //                 map.put("circleMemberDto", e);
                    
    //                 list.add(map);
    //             }
    //         }


    //     }

    //     return list;
    // }

    public List<Map<String, Object>> circleboardList(int circle_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleBoardDto> circleBoardDtos = circleSqlMapper.circleBoardDtoBycircleId(circle_id);

        if(!(circleBoardDtos.equals(null))){
            for(CircleBoardDto e : circleBoardDtos){

                Map<String, Object> map = new HashMap<>();
                int circle_member_id = e.getCircle_member_id();
                CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByCircleMemberId(circle_member_id);
                int user_id = circleMemberDto.getUser_id();
                UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
                // 유저정보(프로필사진), 글정보(글내용), 동아리멤버정보(등급)
                map.put("userInfoDto", userInfoDto);
                map.put("circleBoardDto", e);
                map.put("circleMemberDto", circleMemberDto);
                
                list.add(map);
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
    
            List<CircleVoteDto> circleVoteDtos = circleSqlMapper.circleVoteDtoBycircleId(circle_id);
            
                if(!circleVoteDtos.equals(null)){
                    for(CircleVoteDto ee : circleVoteDtos){
                        

                        // 게시글 하나하나 나옴 여기서 put add 해야됨
                        Map<String, Object> map = new HashMap<>();

                        
                        CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByCircleMemberId(ee.getCircle_member_id());
                        UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(circleMemberDto.getUser_id());

                        int circleVoteId = ee.getCircle_vote_id();
                        List<CircleVoteOptionDto> circleVoteOptionDtos = circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circleVoteId);
                        int count = 0;
                        for(CircleVoteOptionDto circleVoteDto : circleVoteOptionDtos){
                            int voteOptionId = circleVoteDto.getVote_option_id();
                            int voteCnt = circleSqlMapper.circleVoteCompleteCntByVoteOptionId(voteOptionId);
                            count += voteCnt;
                        }
                        map.put("voteCnt", count);
                        map.put("circleMemberDto", circleMemberDto);
                        map.put("userInfoDto", userInfoDto);
                        map.put("circleVoteDto", ee);
                        
                        list.add(map);
                        
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
    // 얜 동아리 장 일때만 출력
    public List<Map<String, Object>> circleListOnlyP(int user_id){

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByUserId(user_id);

        List<Map<String, Object>> list = new ArrayList<>();

        for(CircleMemberDto e : circleMemberDtos){

            String circlePosition = e.getCircle_position();
            
            if(circlePosition.equals("P")){
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
    public void amountInfoInsert(AmountDto amountDto){
        
        circleSqlMapper.amountInfoInsert(amountDto);
    }
    public void cardInfoInsert(CardInfoDto cardInfoDto){

        circleSqlMapper.cardInfoInsert(cardInfoDto);
    }
    public int cardIdMax(){
        
        return circleSqlMapper.cardIdMax();
    }
    public int amountIdMax(){
        
        return circleSqlMapper.amountIdMax();
    }
    public void orderInfoStatusByOrderId(String order_id){

        circleSqlMapper.orderInfoStatusByOrderId(order_id);
    }
    // for(CircleDto e : circleDtos){

    //     Map<String, Object> map = new HashMap<>();
    //     int circle_small_category_id = e.getCircle_small_category_id();
    //     CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
    //     int circle_grade_id = e.getCircle_grade_id();
    //     CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
    //     int circle_id = e.getCircle_id();
    //     int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

    //     map.put("circleMemberCnt", circleMemberCnt);
    //     map.put("circleGradeDto", circleGradeDto);
    //     map.put("circleSmallCategoryDto", circleSmallCategoryDto);
    //     map.put("circleDto", e);

    //     list.add(map);
        
    // }
    // 카테고리 클릭시 나오는 리스트들 여기 리스트는 3개이상 나와도됨
    public List<Map<String, Object>> circleListByCircleSmallcategory(int circle_small_category_id){

        List<Map<String, Object>> list = new ArrayList<>();
        
        List<CircleDto> circleDtos = circleSqlMapper.circleListByCircleSmallcategory(circle_small_category_id);//리스트들 나와야함
        // 카테고리 관련 리스트들이나옴
        for(CircleDto e : circleDtos){

                Map<String, Object> map = new HashMap<>();
                CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
                int circle_grade_id = e.getCircle_grade_id();
                int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
                CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
                CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
                int circle_id = e.getCircle_id();
                int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

                map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
                map.put("circleMemberCnt", circleMemberCnt);
                map.put("circleGradeDto", circleGradeDto);
                map.put("circleSmallCategoryDto", circleSmallCategoryDto);
                map.put("circleDto", e);
        
                list.add(map);
                
            }


        return list;
    }
    public void circleLikeInfoInsert(CircleLikeDto circleLikeDto){
        
        circleSqlMapper.circleLikeInfoInsert(circleLikeDto);
    }
    public int circleLikeInfoCheck(CircleLikeDto circleLikeDto){

        return circleSqlMapper.circleLikeInfoCheck(circleLikeDto);
    }
    public void circleLikeInfoDelete(CircleLikeDto circleLikeDto){
        
        circleSqlMapper.circleLikeInfoDelete(circleLikeDto);
    }

    public List<Map<String, Object>> circleLikeInfo(int user_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleLikeDto> circleLikeDtos = circleSqlMapper.circleLikeInfoByUserId(user_id);
        
        for(CircleLikeDto circleLikeDto : circleLikeDtos){

            int circle_id = circleLikeDto.getCircle_id();
            CircleDto e = circleSqlMapper.circleInfoByCircleId(circle_id);
            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            

        }


        return list;
    }

    public List<Map<String, Object>> circleMyVoteList(int user_id){

        List<Map<String, Object>> list = new ArrayList<>();

        // 내 동아리 회원정보리스트
        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByUserId(user_id);

        for(CircleMemberDto e : circleMemberDtos){
            
            int circle_member_id = e.getCircle_member_id();
            int circle_id = e.getCircle_id(); //circle_id ==> 쿼리스트링 값으로 넘겨야됨
            // 내가 개설한 투표항목리스트네..이건
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
                    map.put("circle_id", circle_id);
                    map.put("voteCnt", count);
                    map.put("circleMemberDto", e);
                    map.put("circleVoteDto", ee);
                    
                    list.add(map);
                    
                }
        }
                
            

        }
        
        return list;


    }
    public CircleScheduleDto circleScheduleByCircleScheduleId(int circle_schedule_id){

        return circleSqlMapper.circleScheduleByCircleScheduleId(circle_schedule_id);
    }
    public int circleScheduleApplicationByCircleScheduleId(int circle_schedule_id){

        return circleSqlMapper.circleScheduleApplicationByCircleScheduleId(circle_schedule_id);
    }
    // 일정신청리스트를 가져와 일정이랑 엮어서 정보를 보내야 함
    // 엮으거 일정테이블, 상품테이블, 주문번호테이블, 카카오페이결제요청테이블에 있는 상품명이랑 상품수량 총액도 가져와야함
    public List<Map<String, Object>> circleScheduleApplyByUserId(int user_id){

        List<Map<String, Object>> list = new ArrayList<>();
        // 여긴맞음
        List<CircleScheduleApplyDto> circleScheduleApplyDtos = circleSqlMapper.circleScheduleApplyByUserId(user_id);
        
        
        for(CircleScheduleApplyDto circleScheduleApplyDto : circleScheduleApplyDtos){
            
            Map<String, Object> map = new HashMap<>();

            //o
            int circle_schedule_id = circleScheduleApplyDto.getCircle_schedule_id(); // 일정pk값을가져옴
            //o 조건이 아이템카테고리 2번 and merchan_id = schedule_id 맞음
            ItemInfoDto itemInfoDto = circleSqlMapper.itemInfoByCircleScheduleId(circle_schedule_id);
            int item_id = itemInfoDto.getItem_id();
            // ==> 여기서 에러가 남 조건이 부합하지 않음 item_pk 이랑 user_id 랑 조건이 안맞아서 에러가 난거임 ==> 결제가 안되었을때 자꾸 type miss 에러가 나옴
            OrderInfoDto orderInfoDto = circleSqlMapper.orderInfoByItemIdAndUserId(item_id, user_id);
            String order_id = orderInfoDto.getOrder_id();
            KakaoPaymentReqDto kakaoPaymentReqDto = circleSqlMapper.kakaoPaymentReqInfoByItemId(order_id);
            KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto = circleSqlMapper.kakaoPaymentAccResInfoByItemId(order_id);
            // o
            CircleScheduleDto circleScheduleDto = circleSqlMapper.circleScheduleInfoByCircleScheduleid(circle_schedule_id);

            // 인원수도 있으면 좋을거 같은데?
            int applyPeopleCount = circleSqlMapper.circleScheduleApplyPeopleCount(circle_schedule_id);
            
            map.put("applyPeopleCount", applyPeopleCount);
            map.put("circleScheduleDto", circleScheduleDto);
            map.put("circleScheduleApplyDto", circleScheduleApplyDto);
            map.put("itemInfoDto", itemInfoDto);
            map.put("orderInfoDto", orderInfoDto);
            map.put("kakaoPaymentReqDto", kakaoPaymentReqDto);
            map.put("kakaoPaymentAcceptResDto", kakaoPaymentAcceptResDto);
            
            
            // null 값이라 안나오는듯

            list.add(map);

        }

        return list;

    }

    public void kakakPaymentCancleReqInsert(KakaoPaymentCancelReqDto kakaoPaymentCancelReqDto){

        circleSqlMapper.kakakPaymentCancleReqInsert(kakaoPaymentCancelReqDto);
    }
    public void kakakPaymentCancleResInsert(KakaoPaymentCancelResDto kakaoPaymentCancelResDto){

        circleSqlMapper.kakakPaymentCancleResInsert(kakaoPaymentCancelResDto);
    }
    public void paymentCancelStatusChangeOrderInfoStatus(String order_id){
        
        circleSqlMapper.paymentCancelStatusChangeOrderInfoStatus(order_id);
    }
    public void scheduleApplicationTableDelete(int circle_schedule_application_id){

        circleSqlMapper.scheduleApplicationTableDelete(circle_schedule_application_id);
    }
    public List<Map<String, Object>> voteThreeNewList(){

        List<Map<String, Object>> list = new ArrayList<>();
        
        List<CircleVoteDto> circleVoteDtos = circleSqlMapper.voteThreeNewList();
        // 투표글을 가져온다
        for(CircleVoteDto circleVoteDto : circleVoteDtos){
            
            Map<String, Object> map = new HashMap<>();

            int circle_vote_id = circleVoteDto.getCircle_vote_id();
            List<CircleVoteOptionDto> circleVoteOptionDtos = circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circle_vote_id);
            // 항목여러개를 가져온다 vote숫자만가져올거니까 항목여러개일때 반복문돌려서 투표한 숫자만 가져옴 
            int count = 0;
            for(CircleVoteOptionDto circleVoteOptionDto : circleVoteOptionDtos){

                int vote_option_id = circleVoteOptionDto.getVote_option_id();
                count += circleSqlMapper.circleVoteCompleteCntByVoteOptionId(vote_option_id);
                
            }
            // 그 관련된 동아리 가입페이지로 이동시키려면 여기서 동아리 정보도 넣어야함
            int circle_id = circleSqlMapper.circleIdGetByCircleVoteId(circle_vote_id);

            map.put("circle_id", circle_id);
            map.put("voteCnt", count); // 투표숫자하고
            map.put("circleVoteDto", circleVoteDto); // 글정보만 들어가면 될듯요?
            
            
            list.add(map);

        }


        return list;
    }
    public int circleScheduleApplicationMaxIdByCircleMemId(int circle_member_id){

        return circleSqlMapper.circleScheduleApplicationMaxIdByCircleMemId(circle_member_id);
    }
    public void circleScheduleAttenDanceInfoInfoInsert(CircleScheduleAttendanceDto circleScheduleAttendanceDto){
        
        circleSqlMapper.circleScheduleAttenDanceInfoInfoInsert(circleScheduleAttendanceDto);
    }
    //circleScheduleListAllByCircleMemberId
    public List<Map<String, Object>> circleAttendanceList(int circle_id, int user_id){

        List<Map<String, Object>> list = new ArrayList<>();

        CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        List<CircleScheduleDto> circleScheduleDtos = circleSqlMapper.circleScheduleListAllByCircleMemberId(circle_member_id);
        for(CircleScheduleDto circleScheduleDto : circleScheduleDtos){

            Map<String, Object> map = new HashMap<>();
            // 여기서 뽑아낼건.. 동아리 리스트만뽑고 출석부 인원까지만 뽑아내자..
            int circle_schedule_id =  circleScheduleDto.getCircle_schedule_id();
            int applyCnt = circleSqlMapper.circleScheduleApplicationPeopleCntByCircleScheduleId(circle_schedule_id);
            LocalDateTime dataTime = circleScheduleDto.getEnd_time();
            System.out.println("일정종료시간:" + dataTime);

            LocalDateTime currentDateTime = LocalDateTime.now();
            System.out.println("현재시간:" + currentDateTime);

            // 현재시간과 일정종료시간 차이 날짜 계산
            long daysUntilEnd = ChronoUnit.DAYS.between(currentDateTime, dataTime);

            // 음수일때는 일정종료일이 지난거
            if (daysUntilEnd < 0) {
                continue;
            } 
            else if(daysUntilEnd < 2){
                map.put("circleScheduleDto", circleScheduleDto);
                map.put("applyCnt", applyCnt);

                list.add(map);
            } // 2일미만일때만 출력시킬거임 (일정종료일 지난지 1일 23시간 59분 59초까지)

            
        }

        return list;
    }

    public List<Map<String, Object>> circleAttendanceApplicationList(int circle_schedule_id){

        List<Map<String, Object>> list = new ArrayList<>();
        
        List<CircleScheduleApplyDto> circleScheduleApplyDtos = circleSqlMapper.circleScheduleApplicationListOrderByDesc(circle_schedule_id);
        
        for(CircleScheduleApplyDto circleScheduleApplyDto : circleScheduleApplyDtos){
            
            Map<String, Object> map = new HashMap<>();

            int circle_schedule_application_id = circleScheduleApplyDto.getCircle_schedule_application_id();
            CircleScheduleAttendanceDto circleScheduleAttendanceDto = circleSqlMapper.circleScheduleAttendanceInfoByCircleScheduleApplicationId(circle_schedule_application_id);
            int circle_member_id = circleScheduleApplyDto.getCircle_member_id();
            CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByCircleMemberId(circle_member_id);
            int user_id = circleMemberDto.getUser_id();
            UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
            
            
            
            // 일정신청 하나에 일정출석 하나
            // 여기서 버튼체크를 위해서 하나 넣을까 생각중.
            map.put("userInfoDto", userInfoDto);
            map.put("circleMemberDto", circleMemberDto);
            map.put("circleScheduleApplyDto", circleScheduleApplyDto);
            map.put("circleScheduleAttendanceDto", circleScheduleAttendanceDto);

            list.add(map);
            

        }
        

        return list;
    }
    
    public void circleAttendanceChangeY(int circle_schedule_application_id){
        
        circleSqlMapper.circleAttendanceChangeY(circle_schedule_application_id);
    }
    public void circleAttendanceChangeN(int circle_schedule_application_id){
        
        circleSqlMapper.circleAttendanceChangeN(circle_schedule_application_id);
    }
    public void circleBoardLikeInsert(CircleBoardLikeDto circleBoardLikeDto){

        circleSqlMapper.circleBoardLikeInsert(circleBoardLikeDto);
    }
    public void circleBoardLikeDelete(CircleBoardLikeDto circleBoardLikeDto){

        circleSqlMapper.circleBoardLikeDelete(circleBoardLikeDto);
    }
    public Boolean circleBoardLikeCheck(CircleBoardLikeDto circleBoardLikeDto){
        
        return circleSqlMapper.circleBoardLikeCheck(circleBoardLikeDto);
    }
    public List<Map<String, Object>> voteChartInfo(int circle_vote_id){
        // 단일 하나의 값이니까 리스트가 나올수없음 걍 map으로 하면될듯
        
        List<Map<String, Object>> list = new ArrayList<>();
        CircleVoteDto circleVoteDto = circleSqlMapper.circleVoteInfoByCircleVoteId(circle_vote_id); // voteinfo
        List<CircleVoteOptionDto> circleVoteOptionDtos = circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circle_vote_id); //항목리스트
        
        for(CircleVoteOptionDto circleVoteOptionDto : circleVoteOptionDtos){
            Map<String, Object> map = new HashMap<>();
             
            int vote_option_id = circleVoteOptionDto.getVote_option_id();
            
            int voteCount = circleSqlMapper.circleVoteCompleteCount(vote_option_id);

            // vote옵션하나당 투표한수만 넣으면 될거같음
            int totalCount = circleSqlMapper.circleVoteTotalCount(circle_vote_id);
            map.put("voteCount", voteCount);
            map.put("circleVoteOptionDto", circleVoteOptionDto);
            map.put("totalCount", totalCount);
            
            list.add(map);
        }

        return list;
    }
    public int circlejoinApplyPossibleCheck(int circle_id, int user_id){

        return circleSqlMapper.circlejoinApplyPossibleCheck(circle_id, user_id);
    }
    // 마이페이지 들어갈 내가 작성한 게시글수, 작성한 투표글수, 작성한 일정 수
    public Map<String, Object> myPageInfo(int user_id){

        Map<String, Object> map = new HashMap<>();

        int boardCnt = circleSqlMapper.boardCntByUserId(user_id);
        int voteCnt = circleSqlMapper.voteCntByUserId(user_id);
        int scheduleCnt = circleSqlMapper.scheduleCntByUserId(user_id);
        UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);

        map.put("userInfoDto", userInfoDto);
        map.put("boardCnt", boardCnt);
        map.put("voteCnt", voteCnt);
        map.put("scheduleCnt", scheduleCnt);

        return map;
    }

    // 동아리 검색 했을때 리스트
    public List<Map<String, Object>> circleSearchList(String searchword){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleSearchList(searchword);

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }

        return list;
    }
    public List<CircleDto> circleNameDuplicateCheck(String circle_name){

        return circleSqlMapper.circleNameDuplicateCheck(circle_name);
    }
    // 항목을 체크한 유저종보가 필요함 
    public List<Map<String, Object>> voteCompleteInfo(int circle_vote_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleVoteOptionDto> circleVoteOptionDtos = circleSqlMapper.circleVoteOptionInfoByCircleVoteId(circle_vote_id);

        for(CircleVoteOptionDto e : circleVoteOptionDtos){

            int vote_option_id = e.getVote_option_id();
            List<CircleVoteCompleteDto> circleVoteCompleteDtos = circleSqlMapper.circleVoteCompleteInfoByVoteOptionId(vote_option_id);
            for(CircleVoteCompleteDto ee : circleVoteCompleteDtos){
                Map<String, Object> map = new HashMap<>();

                int circle_member_id = ee.getCircle_member_id();
                CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByCircleMemberId(circle_member_id);
                int user_id = circleMemberDto.getUser_id();
                UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);

                map.put("circleVoteOptionDto", e);
                map.put("circleMemberDto", circleMemberDto);
                map.put("userInfoDto", userInfoDto);

                list.add(map);
                
            } 

        }

        return list;
    }
    // 어차피 동아리회원이 아니면 피드조차 못 들어가니까 동아리회원이 아닌사람들이 삭제버튼 눌렀을때는 예외처리를 안해도 됨 !!!!!!!
    public Boolean boardDeleteCheckSession(int circle_member_id, int circle_board_id){
        
        Boolean check = false;

        CircleBoardDto circleBoardDto = circleSqlMapper.circleBoardInfoByCircleBoardId(circle_board_id);
        int check_id = circleBoardDto.getCircle_member_id();

        CircleMemberDto circleMemberDto = circleSqlMapper.circleMemberInfoByCircleMemberId(circle_member_id);
        String position = circleMemberDto.getCircle_position();
        // 본인이거나 관리자거나 동아리장일때만 삭제가 가능해야함
        if(check_id == circle_member_id || position == "P" || position == "A"){
            check = true;
        }

        

        return check;

    }

    public List<Map<String, Object>> circleSessionListOrderByCircleId(int user_id){
        
        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleDto> circleDtos = circleSqlMapper.circleSessionListOrderByCircleId(user_id);

        for(CircleDto e : circleDtos){

            Map<String, Object> map = new HashMap<>();
            int circle_small_category_id = e.getCircle_small_category_id();
            CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
            int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
            CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoByMiddleCategoryId(circle_middle_category_id);
            int circle_grade_id = e.getCircle_grade_id();
            CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);
            int circle_id = e.getCircle_id();
            int circleMemberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);

            map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);
            map.put("circleMemberCnt", circleMemberCnt);
            map.put("circleGradeDto", circleGradeDto);
            map.put("circleSmallCategoryDto", circleSmallCategoryDto);
            map.put("circleDto", e);

            list.add(map);
            
        }
        
        return list;
    }
    
    public Boolean peopleCheckNagative(int circle_id){

        Boolean check = true;

        CircleDto circleDto = circleSqlMapper.circleInfoByCircleId(circle_id);
        
        CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circleDto.getCircle_grade_id());
        int gradePeople = circleGradeDto.getCircle_people();
        int memberCnt = circleSqlMapper.circleMemberCountInfo(circle_id);
        
        if(gradePeople == memberCnt){
            check = false;
        }

        return check;

    }
    // 가져와야할 것들 ==> 동아리 회원정보, userInfo, 가입신청정보
    public List<Map<String, Object>> circleMemberManageList(int circle_id, int jang_id){

        List<Map<String, Object>> list = new ArrayList<>();

        List<CircleMemberDto> circleMemberDtos = circleSqlMapper.circleMemberInfoByCircleId(circle_id);

        for(CircleMemberDto e : circleMemberDtos){
            
            Map<String, Object> map = new HashMap<>();

            int user_id = e.getUser_id();
            if(user_id == jang_id){
                continue;
            }
            UserInfoDto userInfoDto = circleSqlMapper.userInfoByUserId(user_id);
            CircleJoinApplyDto circleJoinApplyDto = circleSqlMapper.circleJoinApplyInfoByCircleIdAndUserId(circle_id, user_id);
            // 어차피 동아리 가입신청은 한명당 하나씩밖에 못하니까 테이블이 두개이상 나올수가 없음

            map.put("circleMemberDto", e);
            map.put("userInfoDto", userInfoDto);
            map.put("circleJoinApplyDto", circleJoinApplyDto);

            list.add(map);

        }

        return list;
    }
    public void circleMemberDeleteByCircleMemberId(int circle_member_id){
        // 여기서 모든걸 삭제하시면 됩니다.. .. 이 동아리 회원과 관련된 모든 정보들을.. 결제내역은 내가 책임안진다.. 확인잘하고 하도록
        circleSqlMapper.circleNoticeImageDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleBoardImageDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleVoteOptionDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleScheduleAttendanceDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleBoardDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleVoteDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleVoteCompleteDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleBoardLikeDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleScheduleDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleScheduleApplicationDeleteByCircleMemberId(circle_member_id);
        circleSqlMapper.circleMemberDeleteByCircleMemberId(circle_member_id);

    }
    public void circleJoinApplyDeleteByCircleJoinApplyId(int circle_join_apply_id){

        circleSqlMapper.circleJoinApplyDeleteByCircleJoinApplyId(circle_join_apply_id);
    }
    public void circleMemberChangeAByCircleMemberId(int circle_member_id){

        circleSqlMapper.circleMemberChangeAByCircleMemberId(circle_member_id);

    }
    public void circleMemberChangeMByCircleMemberId(int circle_member_id){

        circleSqlMapper.circleMemberChangeMByCircleMemberId(circle_member_id);
    }
    
}
