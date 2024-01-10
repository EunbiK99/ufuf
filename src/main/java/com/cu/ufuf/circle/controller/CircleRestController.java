package com.cu.ufuf.circle.controller;

import java.io.File;
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
    @RequestParam("data") String circle_name,
    @RequestParam("data") String circle_content){
        // circle_name ==> 왜data값이 다튀어나오는가?????????????????????????
        System.out.println(circle_name);


        CircleDto circleDto = new CircleDto();
        circleDto.setCircle_small_category_id(circle_small_category_id);
        circleDto.setCircle_name(circle_name);
        circleDto.setCircle_content(circle_content);
        // UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute(null);

        RestResponseDto responseDto = new RestResponseDto();

        // 1. 등급번호 1(아이언) set
        circleDto.setCircle_grade_id(1); 
        // 2. 파일등록 multipartFile
        // 이게 원본 파일명
        String filename = file.getOriginalFilename();
        long randomFilename = System.currentTimeMillis();
        String Path = "C:/uploadFiles/";
        

        File realtodayPath = new File(Path);

        // 디렉토리 생성
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
        
        System.out.println(fileLink);
        //circleService.circleInfoInsert(circleDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    
    



}
