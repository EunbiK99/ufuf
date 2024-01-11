package com.cu.ufuf.circle.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;

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

    public List<Map<String, Object>> circleMemberPrintHotThree(){
        
        return circleSqlMapper.circleMemberPrintHotThree();
    }
    
}
