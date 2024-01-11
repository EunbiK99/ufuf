package com.cu.ufuf.circle.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.circle.service.CircleService;
import com.cu.ufuf.dto.CircleNoticeDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/circle")
public class CircleController {
    
    @Autowired
    private CircleService circleService;

    @RequestMapping("test")
    public String test(){
        
        return "circle/test";
    }

    @RequestMapping("circleMainPage")
    public String circleMainPage(){

        

        return "circle/circleMainPage";
    }
    
    @RequestMapping("circleApplyPage")
    public String circleApplyPage(){

        

        return "circle/circleApplyPage";
    }
    @RequestMapping("circleNoticeApplyPage")
    public String circleNoticeApplyPage(){

        

        return "circle/circleNoticeApplyPage";
    }
    
    @RequestMapping("circleNoticeApplyProcess")
    public String circleNoticeApplyProcess(CircleNoticeDto circleNoticeDto, @RequestParam("circle_notice_image") MultipartFile[] images){
        // 모집공고 등록 insert // 모집이미지 등록여러개 // !!!!!(아직안함)동아리키 받아와야함
        System.out.println(circleNoticeDto.getCircle_notice_title());
        System.out.println(circleNoticeDto.getCircle_notice_content());

        // 모집공고 등록이 먼저 되어야하고 그다음에 번호값을 받아서(Max) 번호키를 이미지번호키에 등록
        
        
        int noticeId = circleService.circleNoticeIdInt();
        for(MultipartFile multipartFile : images){
            // 빈공간 처리
            if(multipartFile.isEmpty()) {
				continue;
			}
            // 이게 원본 파일명
            String filename = multipartFile.getOriginalFilename();
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
				multipartFile.transferTo(new File(Path + fileLink));
			}catch(Exception e) {
				e.printStackTrace();
			}
            // 반복문 돌리면서 insert
            CircleNoticeImageDto circleNoticeImageDto = new CircleNoticeImageDto();
            circleNoticeImageDto.setCircle_notice_image(fileLink);
            circleNoticeImageDto.setCircle_notice_id(noticeId); //여기다 공고키 max 

            circleService.circleNoticeImageInfoInsert(circleNoticeImageDto);
            System.out.println(fileLink);
        }
        

        return "redirect:./circleMainPage";
    }
    
    @RequestMapping("circleNoticeBoardPage")
    public String circleNoticeBoardPage(){
        
        return "circle/circleNoticeBoardPage";
    }
    
    


    


}
