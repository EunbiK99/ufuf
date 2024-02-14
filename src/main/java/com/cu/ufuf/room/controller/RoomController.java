package com.cu.ufuf.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.cu.ufuf.dto.RoomGuestDto;
import com.cu.ufuf.dto.RoomGuestReviewDto;
import com.cu.ufuf.dto.RoomGuestReviewImageDto;
import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.room.service.RoomServiceIpml;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomServiceIpml roomService;


	//게스트하우스 메인페이지
	@RequestMapping("roomMainPage")
    public String roomMainPage(Model model){

		// Controller에서 Thymeleaf에 전달하기 전에 리스트를 처리
		model.addAttribute("roomList", roomService.getRoomInfoList().subList(0, Math.min(roomService.getRoomInfoList().size(), 5)));
		model.addAttribute("reviewList", roomService.getReviewList().subList(0, Math.min(roomService.getReviewList().size(), 9)));

		//나중에 인기 숙소, 내근처 숙소, 리뷰목록 가져오기

        return "room/roomMainPage";
    }

	//방 등록페이지
    @RequestMapping("roomRegisterPage")
    public String roomRegisterPage(Model model, HttpSession session){

		model.addAttribute("roomOptionList", roomService.getRoomOptionlist());

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}

        return "room/roomRegisterPage";
    }

	//방 등록 값 넘기기
    @RequestMapping("roomRegisterProcess")
    public String roomRegisterProcess(HttpSession session, RoomInfoDto roomInfoDto, @RequestParam(name = "mainImageFile") MultipartFile mainImageFile, @RequestParam(name = "imageFiles") MultipartFile[] imageFiles, @RequestParam(name = "room_option_category_id") int[] room_option_category_id,String checkin1,String checkin2,String checkout1,String checkout2 ){
		
		//메인이미지
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd/");

		if(mainImageFile != null && !mainImageFile.isEmpty()) {
			String imageRootPath = "C:/uploadFiles/ufuf/roomImage";
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

				String rootPath = "C:/uploadFiles/ufuf/roomImage/";
				
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

		// 카카오페이 결제 상품 insert
		int room_info_id = roomService.roomInfoIdMaxValue();

		roomService.itemInfoInsert(room_info_id);
        
        
        return "redirect:./roomRegisterCompletePage";
    }

	//방 등록 완료페이지
    @RequestMapping("roomRegisterCompletePage")
    public String roomRegisterCompletePage(){
        
        return "room/roomRegisterCompletePage";
    }

	//게스트 예약페이지
	@GetMapping("roomReservationPage")
	public String roomReservationPage(Model model, @RequestParam("room_info_id") int room_info_id, HttpSession session){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
		

		model.addAttribute("roomInfo", roomService.getRoomInfoForReservation(room_info_id));

		return "room/roomReservationPage";
	}

	//예약 값 넘기는거
	@RequestMapping("roomReservationProcess")
	public String roomReservationProcess(@RequestParam(name = "guest_count", required = false, defaultValue = "1") int guestCount, 
				HttpSession session, 
				RoomGuestDto roomGuestDto,
				@RequestParam("start_reservation_schedule") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startReservationSchedule,
				@RequestParam("end_reservation_schedule") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endReservationSchedule
			){
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int userPk = sessionUserInfo.getUser_id();

		System.out.println(userPk);
		
		roomGuestDto.setUser_id(userPk);
		roomGuestDto.setGuest_count(guestCount);

		roomGuestDto.setStart_reservation_schedule(startReservationSchedule);
		roomGuestDto.setEnd_reservation_schedule(endReservationSchedule);
		
		roomService.roomReservation(roomGuestDto);

		return "redirect:./roomReservationCompletePage?room_info_id="+roomGuestDto.getRoom_info_id();
	}

	//예약완료페이지(해야댐)
	@RequestMapping("roomReservationCompletePage")
    public String roomReservationCompletePage(HttpSession session, Model model, @RequestParam("room_info_id") int room_info_id){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int user_id=sessionUserInfo.getUser_id();

		System.out.println(user_id);
		model.addAttribute("reservationInfo", roomService.getReservationInfo(user_id, room_info_id));


        return "room/roomReservationCompletePage";
    }
	
	
	//숙소 리스트 페이지(기본)
	@RequestMapping("roomListPage")
	public String roomListPage(){
		
		
		return "room/roomListPage";
	}

	//숙소 상세 페이지
	@GetMapping("roomDetailPage")
	public String roomDetailPage(Model model, @RequestParam("room_info_id") int room_info_id){
		
		model.addAttribute("roomDetail", roomService.getRoomInfo(room_info_id));
		model.addAttribute("currentDate", LocalDate.now());
		return "room/roomDetailPage";
	}


	//예약(게스트) 상세 페이지W
	@RequestMapping("roomReservationInfoPage")
    public String roomReservationInfoPage(HttpSession session, Model model, @RequestParam("room_info_id") int room_info_id){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		
		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
		int user_id=sessionUserInfo.getUser_id();


		model.addAttribute("reservationInfo", roomService.getReservationInfo(user_id, room_info_id));
		model.addAttribute("currentDate", LocalDate.now());

		

        return "room/roomReservationInfoPage";
    }

	//리뷰쓰기 페이지
	@RequestMapping("guestReviewPage")
	public String guestReviewPage(HttpSession session, Model model, @RequestParam("room_info_id") int room_info_id){
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
		int user_id=sessionUserInfo.getUser_id();

		model.addAttribute("ReservationInfo", roomService.getReservationInfo(user_id, room_info_id));

		return "room/guestReviewPage";
	}

	//리뷰 쓴거 값 넘기기
	@RequestMapping("guestReviewProcess")
	public String guestReviewProcess(int room_info_id, RoomGuestReviewDto roomGuestReviewDto) {
		
		roomService.guestReviewRegister(roomGuestReviewDto);
        
		return "redirect:./roomReservationInfoPage?room_info_id="+room_info_id;
	}

	
	//내가 올린 방 목록
	@RequestMapping("myRoomListPage")
	public String myRoomListPage(Model model, HttpSession session){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}

		model.addAttribute("roomList", roomService.getRoomInfoList());
		model.addAttribute("currentDate", LocalDate.now());

		return "room/myRoomListPage";
	}

	//내가 찜한 방 목록
	@RequestMapping("myInterestRoomListPage")
	public String myInterestRoomListPage(HttpSession session,Model model){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
		int user_id=sessionUserInfo.getUser_id();

		model.addAttribute("interestRoomList", roomService.userInterestRoomList(user_id));
		
		return "room/myInterestRoomListPage";
	}

	//내가 예약한 방 목록
	@RequestMapping("myRoomReservationListPage")
	public String myRoomReservationListPage(HttpSession session, Model model){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}
		int user_id=sessionUserInfo.getUser_id();

		model.addAttribute("roomReservationList", roomService.roomReservationList(user_id));
		model.addAttribute("currentDate", LocalDate.now());
		return "room/myRoomReservationListPage";
	}

	//올린 게스트하우스 글 수정
	@RequestMapping("updateRoomInfoPage")
	public String updateRoomInfoPage(Model model, @RequestParam("room_info_id") int room_info_id, HttpSession session){
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}

		model.addAttribute("roomDetail", roomService.getRoomInfo(room_info_id));
		model.addAttribute("roomOptionList", roomService.getRoomOptionlist());

		return "room/updateRoomInfoPage";
	}

	


	//올린 게스트하우스 글 수정 값 넘기는거
    @RequestMapping("updateRoomInfoProcess")
    public String updateRoomInfoProcess(HttpSession session,RoomInfoDto roomInfoDto, @RequestParam(name = "mainImageFile") MultipartFile mainImageFile, @RequestParam(name = "imageFiles") MultipartFile[] imageFiles, @RequestParam(name = "room_option_category_id") int[] room_option_category_id,String checkin1,String checkin2,String checkout1,String checkout2 ){

		

		//메인이미지
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd/");

		if(mainImageFile != null && !mainImageFile.isEmpty()) {
			String imageRootPath = "C:/uploadFiles/ufuf/roomImage";
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
		}else{
			roomInfoDto.setMain_image(roomInfoDto.getMain_image());
		}

		//추가사진
		List<RoomImageDto> roomImageDtoList = new ArrayList<>(); 
		
		if(imageFiles != null) {
			for(MultipartFile multipartFile : imageFiles) {
				if(multipartFile.isEmpty()) {
					continue;
				}

				String rootPath = "C:/uploadFiles/ufuf/roomImage";
				
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

		roomService.updateRoom(roomInfoDto, room_option_category_id);
		roomService.updateRoomDetailImage(roomInfoDto, roomImageDtoList);
        
        
        return "redirect:./myRoomListPage";
    }

	//방 정보삭제
	@RequestMapping("deleteRoomInfoProcess")
	public String deleteRoomInfoProcess( @RequestParam("room_info_id")int room_info_id){
		roomService.deleteRoomInfo(room_info_id);
		return "redirect:./myRoomListPage";
	}

	//지도로 방 찾는 페이지?
	@RequestMapping("roomMapPage")
	public String roomMapPage(Model model,HttpSession session){
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}

		model.addAttribute("roomList", roomService.getRoomInfoList());

		return "room/roomMapPage";
	}

	//테스트용
	@RequestMapping("roomTestPage")
	public String roomTestPage(){


		return "room/roomTestPage";
	}


	//결제실패
	@RequestMapping("roomPaymentFail")
    public String roomPaymentFail(){

        return "room/roomPaymentFail";
    }

	//결제 성공
    @RequestMapping("roomPaymentSuccess")
    public String roomPaymentSuccess(){

        return "room/roomPaymentSuccess";
    }

	//결제 취소
    @RequestMapping("roomPaymentCancel")
    public String roomPaymentCancel(){

        return "room/roomPaymentCancel";
    }

	//방별로 예약자 명단
	@RequestMapping("roomReservationListPage")
	public String roomReservationListPage(Model model, int room_info_id, HttpSession session){

		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

		if(sessionUserInfo == null) {
			return "redirect:../login/loginPage";
			
		}

		model.addAttribute("getRoomReservationList", roomService.getRoomReservationList(room_info_id));

		return "room/roomReservationListPage";
	}


}
