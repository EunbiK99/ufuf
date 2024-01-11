package com.cu.ufuf.circle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeDto;
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

    // 동아리 공고 등록 & 공고 이미지 등록
    public void circleNoticeInfoInsert(CircleNoticeDto circleNoticeDto);
    public void circleNoticeImageInfoInsert(CircleNoticeImageDto circleNoticeImageDto);
    
    // 동아리 공고 max번호 가져오기
    public int circleNoticeIdInt();
}
