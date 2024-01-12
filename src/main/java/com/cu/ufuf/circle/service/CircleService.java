package com.cu.ufuf.circle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
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
    // 이거 자바스크립트로 받을까????
    public Map<String, Object> circleInfoVarious(int circle_id){

        Map<String, Object> map = new HashMap<>();
        
        CircleDto circleDto = circleSqlMapper.circleInfoByCircleId(circle_id);
        int circle_grade_id = circleDto.getCircle_grade_id();
        int circle_small_category_id = circleDto.getCircle_small_category_id();

        CircleSmallCategoryDto circleSmallCategoryDto = circleSqlMapper.circlesmallCategoryListBysmallCategoryId(circle_small_category_id);
        CircleGradeDto circleGradeDto = circleSqlMapper.circleGradeInfoByGradeId(circle_grade_id);

        int circle_middle_category_id = circleSmallCategoryDto.getCircle_middle_category_id();
        CircleMiddleCategoryDto circleMiddleCategoryDto = circleSqlMapper.circlemiddleCategoryInfoBySmallCategoryId(circle_middle_category_id);
        
        // 4개정보 받아서 보내면됨
        map.put("circleDto", circleDto);
        map.put("circleSmallCategoryDto", circleSmallCategoryDto);
        map.put("circleGradeDto", circleGradeDto);
        map.put("circleMiddleCategoryDto", circleMiddleCategoryDto);

        

        return map;
    }

    public List<CircleNoticeImageDto> circleNoticeImageInfoByCircleId(int circle_id){


        return circleSqlMapper.circleNoticeImageInfoByCircleId(circle_id);
    }
    
}
