package com.cu.ufuf.circle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.CircleDto;
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
    
    // 동아리 핫한 3개 리스트 가져오기
    public List<Map<String, Object>> circleMemberPrintHotThree();
}
