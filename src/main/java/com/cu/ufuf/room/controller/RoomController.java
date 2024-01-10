package com.cu.ufuf.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.room.service.RoomServiceIpml;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomServiceIpml roomService;


	@RequestMapping("roomMainPage")
    public String roomMainPage(){
		
		//나중에 인기 숙소, 내근처 숙소, 리뷰목록 가져오기

        return "room/roomMainPage";
    }

    @RequestMapping("roomRegisterPage")
    public String roomRegisterPage(Model model){

		model.addAttribute("roomOptionList", roomService.getRoomOptionlist());

        return "room/roomRegisterPage";
    }

    @RequestMapping("roomRegisterProcess")
    public String roomRegisterProcess(HttpSession session, RoomInfoDto roomInfoDto, @RequestParam(name = "mainImageFile") MultipartFile mainImageFile, @RequestParam(name = "imageFiles") MultipartFile[] imageFiles, @RequestParam(name = "room_option_category_id") int[] room_option_category_id,String checkin1,String checkin2,String checkout1,String checkout2 ){
		
		//메인이미지
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd/");

		if(mainImageFile != null && !mainImageFile.isEmpty()) {
			String imageRootPath = "C:/Workspace/GitWorkspace/ufuf/src/main/resources/static/public/image/room/";
			String imageTodayPath = sdf.format(new Date());
			
			File todayFolderForCreate = new File(imageRootPath + imageTodayPath);
			
			if(!todayFolderForCreate.exists()) {
				todayFolderForCreate.mkdirs();
			}
			
			String originalFileName = mainImageFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			long currentTime = System.currentTimeMillis();
			
			String fileName = uuid + "_" + currentTime;
			
			String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
			fileName += ext;
			
			try {
				mainImageFile.transferTo(new File(imageRootPath + imageTodayPath + fileName));
			}catch(Exception e) {
				e.printStackTrace();
			}
			roomInfoDto.setMain_image(imageTodayPath + fileName);;
		}

		//추가사진
		List<RoomImageDto> roomImageDtoList = new ArrayList<>(); 
		
		if(imageFiles != null) {
			for(MultipartFile multipartFile : imageFiles) {
				if(multipartFile.isEmpty()) {
					continue;
				}

				String rootPath = "C:/Workspace/GitWorkspace/ufuf/src/main/resources/static/public/image/room/";
				
				String todayPath = sdf.format(new Date());
				
				File todayFolderForCreate = new File(rootPath + todayPath);
				
				if(!todayFolderForCreate.exists()) {
					todayFolderForCreate.mkdirs();
				}
				
				String originalFileName = multipartFile.getOriginalFilename();

				//파일명 충돌 회피 - 랜덤, 시간 조합
				String uuid = UUID.randomUUID().toString();
				long currentTime = System.currentTimeMillis();
				String fileName = uuid + "_" + currentTime;
				
				// 확장자 추출
				String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
				fileName += ext;
				
				try {
					multipartFile.transferTo(new File(rootPath + todayPath + fileName));					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				RoomImageDto roomImageDto = new RoomImageDto();
				roomImageDto.setLocation(todayPath + fileName);
				roomImageDto.setOriginal_filename(originalFileName);
				
				roomImageDtoList.add(roomImageDto);
				
			}
		}
		
		
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int userPk = sessionUserInfo.getUser_id();
		
		roomInfoDto.setUser_id(userPk);
		
		roomInfoDto.setCheckin_time(checkin1+" "+checkin2);
		roomInfoDto.setCheckout_time(checkout1+" "+checkout2);
		
		roomService.roomRegister(roomInfoDto, room_option_category_id, roomImageDtoList);
        
        
        return "redirect:./roomRegisterPage";
    }

    @RequestMapping("roomRegisterCompletePage")
    public String roomRegisterCompletePage(){
        
        return "room/roomRegisterCompletePage";
    }

}
