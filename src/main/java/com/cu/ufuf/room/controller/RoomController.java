package com.cu.ufuf.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.cu.ufuf.dto.RoomGuestDto;
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


	//게스트하우스 메인페이지
	@RequestMapping("roomMainPage")
    public String roomMainPage(){
		
		//나중에 인기 숙소, 내근처 숙소, 리뷰목록 가져오기

        return "room/roomMainPage";
    }

	//방 등록페이지
    @RequestMapping("roomRegisterPage")
    public String roomRegisterPage(Model model){

		model.addAttribute("roomOptionList", roomService.getRoomOptionlist());

        return "room/roomRegisterPage";
    }

	//방 등록 값 넘기기
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
        
        
        return "redirect:./roomRegisterCompletePage";
    }

	//방 등록 완료페이지
    @RequestMapping("roomRegisterCompletePage")
    public String roomRegisterCompletePage(){
        
        return "room/roomRegisterCompletePage";
    }

	//게스트 예약페이지
	@GetMapping("roomReservationPage")
	public String roomReservationPage(Model model, @RequestParam("room_info_id") int room_info_id){
		

		model.addAttribute("roomInfo", roomService.getRoomInfoForReservation(room_info_id));

		return "room/roomReservationPage";
	}

	//예약 값 넘기는거
	@RequestMapping("roomReservationProcess")
	public String roomReservationProcess(@RequestParam(name = "guest_count", required = false, defaultValue = "1") int guestCount, HttpSession session, RoomGuestDto roomGuestDto) {
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int userPk = sessionUserInfo.getUser_id();
		
		roomGuestDto.setUser_id(userPk);
		roomGuestDto.setGuest_count(guestCount);
		System.out.println(roomGuestDto.getRoom_info_id());
		
		roomService.roomReservation(roomGuestDto);

		return "redirect:./roomDetailPage?room_info_id="+roomGuestDto.getRoom_info_id();
	}

	//예약완료페이지(해야댐)
	@RequestMapping("roomReservationCompletePage")
    public String roomReservationCompletePage(Model model, @RequestParam("room_info_id") int room_info_id){

		System.out.println(room_info_id);
		model.addAttribute("reservationInfo", roomService.getReservationInfo(room_info_id));
		
        //여기다 예약정보 받아와서 예약 이렇게 됐다 확인할 수 있게
        return "room/roomReservationCompletePage";
    }
	
	
	//숙소 리스트 페이지(기본)
	@RequestMapping("roomListPage")
	public String roomListPage(Model model){

		
		model.addAttribute("roomList", roomService.getRoomInfoList());
		return "room/roomListPage";
	}

	//숙소 상세 페이지
	@GetMapping("roomDetailPage")
	public String roomDetailPage(Model model, @RequestParam("room_info_id") int room_info_id){
		
		model.addAttribute("roomDetail", roomService.getRoomInfo(room_info_id));
		return "room/roomDetailPage";
	}


}
