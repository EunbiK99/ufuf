package com.cu.ufuf.circle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.circle.mapper.CircleSqlMapper;
import com.cu.ufuf.dto.CircleDto;
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
    
}
