package com.cu.ufuf.meeting.api.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingRestResponseDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;

@RestController
@RequestMapping("/meeting/api/*")
public class RestMeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;


    @GetMapping("checkDuplicateSNSUrl")
    public MeetingRestResponseDto checkDuplicateSNSUrl(String value){
        
        System.out.println(value);
        
        int checkedValue = meetingService.checkDuplicateSNSUrl(value);
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(checkedValue);        

        return meetingRestResponseDto;
    }

    @PostMapping("registerProfile")
    public MeetingRestResponseDto registerProfile(
        @RequestParam(name="profile_Img") MultipartFile profile_Img,
        MeetingProfileDto meetingProfileDto,
        MeetingSNSDto meetingSNSDto
    ){
        if(profile_Img != null){
            String originalFilename = profile_Img.getOriginalFilename();
            System.out.println(originalFilename);

            String rootPath = "c:/uploadFiles/ufuf/meeting/";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
            String todayPath = sdf.format(new Date());

            File todayFolderForCreate = new File(rootPath + todayPath);
            if(!todayFolderForCreate.exists()){
                todayFolderForCreate.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            long curruntTime = System.currentTimeMillis();
            String fileName = uuid + "_" + curruntTime;

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName += ext;

            try {
                profile_Img.transferTo(new File(rootPath + todayPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            meetingProfileDto.setProfileImg(todayPath + fileName);
        }
        int meetingProfilePk = meetingService.createProfilePk();
        meetingProfileDto.setProfileid(meetingProfilePk);
        meetingSNSDto.setProfileid(meetingProfilePk);

        meetingService.registerNewMeetingProfile(meetingProfileDto);
        meetingService.resgisterNewSNS(meetingSNSDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");        
        
        return meetingRestResponseDto;
    }

    @GetMapping("checkMeetingProfile")
    public MeetingRestResponseDto checkMeetingProfile(int user_id){
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(meetingService.checkExistMeetingProfile(user_id));
        
        return meetingRestResponseDto;
    }
    
    
    
    
    
    
    
    public MeetingRestResponseDto templete(){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(1);
        return meetingRestResponseDto;
    }
}
