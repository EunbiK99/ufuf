package com.cu.ufuf.meeting.api.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingRestResponseDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;
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

    @PostMapping("registerGroupProcess")
    public MeetingRestResponseDto registerGroupProcess(
        MeetingGroupDto meetingGroupDto,
        MeetingFirstLocationCategoryDto meetingFirstLocationCategoryDto,
        MeetingSecondLocationCategoryDto meetingSecondLocationCategoryDto,        
        String[] tagNameList,
        MultipartFile group_image
    ){

        int groupPk = meetingService.createGroupPk();        
        meetingGroupDto.setGroupId(groupPk);
        // meetingGroupDto.setProfileId((int)groupData.get("profileId"));
        // meetingGroupDto.setGroupTitle((String)(groupData.get("groupTitle")));
        // meetingGroupDto.setGroupContent((String)groupData.get("groupContent"));
        // meetingGroupDto.setGroupHeadCount(Integer.parseInt((String)(groupData.get("groupHeadCount"))));
        // meetingGroupDto.setGroupMeetingDate(LocalDateTime.parse((String)groupData.get("groupMeetingDate")));
        // meetingGroupDto.setGroupApplyStart(LocalDate.parse((String)groupData.get("groupApplyStart")));
        // meetingGroupDto.setGroupApplyEnd(LocalDate.parse((String)groupData.get("groupApplyEnd")));
        // meetingGroupDto.setGroupApplyCharge(Integer.parseInt((String)groupData.get("groupApplyCharge")));
        // meetingGroupDto.setGroupMeetingPlace((String)groupData.get("groupMeetingPlace"));
        // meetingGroupDto.setGroupGenderOption((String)groupData.get("groupGenderOption"));
        
        
        
        int firstLocationCategoryId = meetingService.createFirstLocationCategoryPk();
        meetingFirstLocationCategoryDto.setFirstLocationCategoryId(firstLocationCategoryId);                
        
        int secondLocationCategoryId = meetingService.createSecondLocationCategoryPk();
        meetingSecondLocationCategoryDto.setSecondLocationCategoryId(secondLocationCategoryId);        
        
        meetingService.registerLocationCategory(meetingFirstLocationCategoryDto, meetingSecondLocationCategoryDto);        
        
        MeetingGroupFirstLocationCategoryDto meetingGroupFirstLocationCategoryDto = new MeetingGroupFirstLocationCategoryDto();
        meetingGroupFirstLocationCategoryDto.setGroupId(groupPk);
        meetingGroupFirstLocationCategoryDto.setFirstLocationCategoryId(firstLocationCategoryId);
        
        MeetingGroupSecondLocationCategoryDto meetingGroupSecondLocationCategoryDto = new MeetingGroupSecondLocationCategoryDto();
        meetingGroupSecondLocationCategoryDto.setGroupId(groupPk);
        meetingGroupSecondLocationCategoryDto.setSecondLocationCategoryId(secondLocationCategoryId);

        meetingService.registerGroupLocationCategory(meetingGroupFirstLocationCategoryDto, meetingGroupSecondLocationCategoryDto);
        
        for(String tagName : tagNameList){            
            int tagPk = meetingService.createTagPk();
            MeetingTagDto meetingTagDto = new MeetingTagDto();            
            meetingTagDto.setTagName(tagName);
            meetingTagDto.setTagId(tagPk);

            meetingService.registerTag(meetingTagDto);
            
            MeetingGroupTagDto meetingGroupTagDto = new MeetingGroupTagDto();
            meetingGroupTagDto.setGroupId(groupPk);
            meetingGroupTagDto.setTagId(tagPk);
            
            meetingService.registerGroupTag(meetingGroupTagDto);
        }

        if(group_image != null){
            String originalFilename = group_image.getOriginalFilename();
            System.out.println(originalFilename);

            String rootPath = "c:/uploadFiles/ufuf/meeting/groupImage/";
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
                group_image.transferTo(new File(rootPath + todayPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            meetingGroupDto.setGroupImage(todayPath + fileName);
        }


        meetingService.registerNewGroup(meetingGroupDto);

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        
        return meetingRestResponseDto;
    }

    @GetMapping("getGroupList")
    public MeetingRestResponseDto getGroupList(){

        List<MeetingGroupDto> groupList = meetingService.getGroupListAll();
        
        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(groupList);
        return meetingRestResponseDto;
    }

    


    
    
    public MeetingRestResponseDto templete(){

        MeetingRestResponseDto meetingRestResponseDto = new MeetingRestResponseDto();

        meetingRestResponseDto.setResult("success");
        meetingRestResponseDto.setData(1);
        return meetingRestResponseDto;
    }
}
