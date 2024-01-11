package com.cu.ufuf.circle.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.circle.service.CircleService;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/circle")
public class CircleRestController {
    
    @Autowired
    private CircleService circleService;

    @RequestMapping("smallCategoryAll")
    public RestResponseDto smallCategoryAll(){

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> CircleSmallCategoryDtos = circleService.circleSmallCategoryDtos();

        responseDto.setData(CircleSmallCategoryDtos);
        responseDto.setResult("success");
        
        return responseDto;
    }
    @RequestMapping("circlesmallcategoryInfoByMiddleId")
    public RestResponseDto circlesmallcategoryInfoByMiddleId(@RequestParam(name="circle_middle_category_id") int circle_middle_category_id){

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> CircleSmallCategoryDtos = circleService.circlesmallcategoryInfoByMiddleId(circle_middle_category_id);

        responseDto.setData(CircleSmallCategoryDtos);
        responseDto.setResult("success");
        
        return responseDto;
    }

    @RequestMapping("middleCategoryAll")
    public RestResponseDto middleCategoryAll(){

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleMiddleCategoryDto> CircleMiddleCategoryDtos = circleService.circlemiddlecategoryInfoAll();

        responseDto.setData(CircleMiddleCategoryDtos);
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleComplete insert만하니까 데이터는 안보내줘도 됨
    // 여기 세션 정보값 넣어줘야되는데.. 지금 세션정보있나..? 음 아직없으면 만들지말자..
    @RequestMapping("circleComplete")
    public RestResponseDto circleComplete(@RequestParam("file") MultipartFile file,
    @RequestParam("data") int circle_small_category_id,
    @RequestParam("circle_name") String circle_name,
    @RequestParam("circle_content") String circle_content, HttpSession session){

        // 세션정보들
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        String circle_university = userInfoDto.getUniversity();

        // 인스턴스 생성
        RestResponseDto responseDto = new RestResponseDto();

        // 동아리 대표 배너이미지
        String filename = file.getOriginalFilename();
        long randomFilename = System.currentTimeMillis();
        String Path = "C:/uploadFiles/";
        

        File realtodayPath = new File(Path);

        if(!realtodayPath.exists()){
            realtodayPath.mkdirs();
        }

        String commafile = filename.substring(filename.lastIndexOf("."));
        String fileLink = randomFilename + commafile;
        
        try {
            file.transferTo(new File(Path + fileLink));
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        CircleDto circleDto = new CircleDto();
        circleDto.setCircle_small_category_id(circle_small_category_id);
        circleDto.setCircle_name(circle_name);
        circleDto.setCircle_content(circle_content);
        circleDto.setCircle_grade_id(1);
        circleDto.setCircle_image(fileLink); 
        circleDto.setUser_id(user_id);
        circleDto.setCircle_university(circle_university);

        circleService.circleInfoInsert(circleDto);
        // 이거끝나고 상세이미지 등록하는거 해주어야함
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    @RequestMapping("smallCategoryList")
    public RestResponseDto smallCategoryList(){

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> circleSmallCategoryDtos = circleService.circleSmallCategoryDtos();
        // 여기서 고민 6개만 넣을까?
        List<CircleSmallCategoryDto> circleSmallCategoryDtos2 = new ArrayList<>(); 
        int sizeToCopy = Math.min(6, circleSmallCategoryDtos.size());
        for (int x = 0; x < sizeToCopy; x++) {
            circleSmallCategoryDtos2.add(circleSmallCategoryDtos.get(x));
        }
                
        responseDto.setData(circleSmallCategoryDtos2);
        responseDto.setResult("success");
        
        return responseDto;
    }
    
    



}
