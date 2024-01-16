package com.cu.ufuf.circle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleBoardImageDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.CircleVoteDto;
import com.cu.ufuf.dto.CircleVoteOptionDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface CircleSqlMapper {
    
    // 소규모 카테고리 리스트
    public List<CircleMiddleCategoryDto> circlemiddlecategoryInfoAll();
    public List<CircleSmallCategoryDto> circlesmallcategoryInfoAll();
    public List<CircleSmallCategoryDto> circlesmallcategoryInfoByMiddleId(int circle_middle_category_id);

    // 동아리 등록
    public void circleInfoInsert(CircleDto circleDto);

    // 공고 이미지 등록
    public void circleNoticeImageInfoInsert(CircleNoticeImageDto circleNoticeImageDto);

    // 동아리번호max값 가져오기
    public int circleIdMaxByUserId(int user_id);

    // 동아리 회원정보 입력
    public void cirlceMemberinfoInsert(CircleMemberDto circleMemberDto);
    
    // 동아리 핫한 3개 리스트 가져오기 & 신규순전체 동아리 리스트
    public List<CircleDto> circleInfoHotThree();
    public List<CircleDto> circleNewListOrderByCircleId();

    // 동아리 소분류카테고리번호로 카테고리 이름 가져올거.. & 중분류 카테고리 소분류카테고리번호로가져옴
    public CircleSmallCategoryDto circlesmallCategoryListBysmallCategoryId(int circle_small_category_id);
    public CircleMiddleCategoryDto circlemiddleCategoryInfoByMiddleCategoryId(int circle_middle_category_id);

    // 동아리 등급번호로 동아리 등급정보 가져오기 & 동아리 회원수 몇명인지 count
    public CircleGradeDto circleGradeInfoByGradeId(int circle_grade_id);
    public int circleMemberCountInfo(int circle_id);

    // 동아리id로 단일동아리정보 가져오기
    public CircleDto circleInfoByCircleId(int circle_id);

    // 동아리 상세이미지(여러개) 가져오는..
    public List<CircleNoticeImageDto> circleNoticeImageInfoByCircleId(int circle_id);
    
    // 동아리 가입신청 ====> 나중에 직책이 P , M 인 사람이 승인을 할 수 있도록.. 승인여부 Y로 바뀌면 동아리회원정보 insert되도록 만들어야 함
    public void circleJoinApplyInsert(CircleJoinApplyDto circleJoinApplyDto);

    // 동아리 회원정보 (세션userid로 가져올거) => 내가 가지고 있는게 여러개 나오겠지..
    public List<CircleMemberDto> circleMemberInfoByUserId(int user_id);

    // 동아리 회원정보 (동아리 키 기준)
    public List<CircleMemberDto> circleMemberInfoByCircleId(int circle_id);

    // 동아리 게시판리스트 (동아리 회원번호기준)
    public List<CircleBoardDto> circleboardInfoByCircleMemberId(int circle_member_id);

    // 유저정보 by user_id
    public UserInfoDto userInfoByUserId(int user_id);

    // userid and circleid 동아리 회원 정보찾기
    public CircleMemberDto circleMemberInfoByUserIdAndCircleId(@Param("user_id") int user_id, @Param("circle_id") int circle_id);

    // 게시글 등록록
    public void circleboardDtoInsert(CircleBoardDto circleBoardDto);

    // 게시글 내가작성한것중에 최근에 작성한 글찾아오기
    public int boardIdMaxByCircleMemberId(int circle_member_id);

    // 게시글 이미지 등록
    public void circleboardImageDtoInsert(CircleBoardImageDto circleBoardImageDto);
    
    // 투표글생성 & 투표항목생성 => insert
    public void circleVoteInsert(CircleVoteDto circleVoteDto);
    public void circleVoteOptionInsert(CircleVoteOptionDto circleVoteOptionDto);

    // 흠
}
