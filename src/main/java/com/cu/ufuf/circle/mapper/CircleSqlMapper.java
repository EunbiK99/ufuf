package com.cu.ufuf.circle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;

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
    public List<Map<String, Object>> circleMemberPrintHotThree();
    public List<CircleDto> circleNewListOrderByCircleId();

    // 동아리 소분류카테고리번호로 카테고리 이름 가져올거.. & 중분류 카테고리 소분류카테고리번호로가져옴
    public CircleSmallCategoryDto circlesmallCategoryListBysmallCategoryId(int circle_small_category_id);
    public CircleMiddleCategoryDto circlemiddleCategoryInfoBySmallCategoryId(int circle_small_category_id);

    // 동아리 등급번호로 동아리 등급정보 가져오기 & 동아리 회원수 몇명인지 count
    public CircleGradeDto circleGradeInfoByGradeId(int circle_grade_id);
    public int circleMemberCountInfo(int circle_id);

    // 동아리id로 단일동아리정보 가져오기
    public CircleDto circleInfoByCircleId(int circle_id);

    // 동아리 상세이미지(여러개) 가져오는..
    public List<CircleNoticeImageDto> circleNoticeImageInfoByCircleId(int circle_id);
    
}
