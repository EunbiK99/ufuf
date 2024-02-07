package com.cu.ufuf.room.controller;

import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.InterestRoomDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.room.service.RoomServiceIpml;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/room")
public class RoomRestController {

    @Autowired
    private RoomServiceIpml roomService;


    @RequestMapping("toggleInterestRoom")
	public RestResponseDto toggleInterestRoom(HttpSession session, InterestRoomDto params,@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int user_id=sessionUserInfo.getUser_id();

		params.setUser_id(user_id);
		
		int room_info_id = Integer.parseInt(roomInfoId);
		params.setRoom_info_id(room_info_id);
	
		roomService.toggleInterestRoom(params);
		
		restResponseDto.setResult("success");
		
		return restResponseDto;
	}
	
	@RequestMapping("getTotalInterestCount")
	public RestResponseDto getTotalInterestCount(@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		int room_info_id = Integer.parseInt(roomInfoId);
		int count = roomService.getTotalInterestCount(room_info_id);
		
		restResponseDto.setResult("success");
		restResponseDto.setData(count);
		
		return restResponseDto;
	}
	
	@RequestMapping("isInterest")
	public RestResponseDto isInterest(HttpSession session, InterestRoomDto params,@RequestParam("roomInfoId") String roomInfoId) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
		int user_id=sessionUserInfo.getUser_id();

		params.setUser_id(user_id);
		
		int room_info_id = Integer.parseInt(roomInfoId);
		params.setRoom_info_id(room_info_id);

		boolean isInterestRoom = roomService.isInterestRoom(params);
		
		restResponseDto.setResult("success");
		restResponseDto.setData(isInterestRoom);
		
		return restResponseDto;
	}

	@RequestMapping("getReviewList")
    public RestResponseDto getReviewList() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
		
		restResponseDto.setData(roomService.getReviewList());
        return restResponseDto;
	}

    @RequestMapping("getReviewListForMain")
    public RestResponseDto getReviewListForMain() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
		
		restResponseDto.setData(roomService.getReviewList().subList(0, Math.min(roomService.getReviewList().size(), 9)));
        return restResponseDto;
	}

    @RequestMapping("getMyId")
	public RestResponseDto getMyId(HttpSession session) {
		RestResponseDto restResponseDto = new RestResponseDto();
		
		UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");


		
		restResponseDto.setResult("success");
		
		if(sessionUserInfo != null) {
			restResponseDto.setData(sessionUserInfo.getUser_id());
		}
		
		return restResponseDto;
	}

	@RequestMapping("getRoomList")
    public RestResponseDto getRoomList() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
		
		restResponseDto.setData(roomService.getRoomInfoList());
        return restResponseDto;
	}

    @RequestMapping("getRoomListForMain")
    public RestResponseDto getRoomListForMain() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
		
		restResponseDto.setData(roomService.getRoomInfoList().subList(0, Math.min(roomService.getRoomInfoList().size(), 5)));
        return restResponseDto;
	}

	@RequestMapping("getRoomListForSearch")
    public RestResponseDto getRoomListForSearch(@RequestParam(required = false) String searchWord,@RequestParam(required = false) Integer peopleCount,@RequestParam(required = false) LocalDate startSchedule,@RequestParam(required = false)LocalDate endSchedule) {
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");

		if(searchWord !=null || peopleCount!=null || (startSchedule!=null && endSchedule!=null)){
			restResponseDto.setData(roomService.getRoomInfoListForFilter(searchWord,peopleCount, startSchedule, endSchedule));
		}else{
			restResponseDto.setData(roomService.getRoomInfoList());
		}
		
        return restResponseDto;
	}

	@RequestMapping("getRoomInfoListForSearchLocation")
	public RestResponseDto getRoomInfoListForSearchLocation(@RequestParam(required = false) String searchWord) {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");

        if(searchWord !=null){
            
			restResponseDto.setData(roomService.getRoomInfoListForSearchLocation(searchWord).subList(0, Math.min(roomService.getRoomInfoListForSearchLocation(searchWord).size(), 5)));
		}else{
			restResponseDto.setData(roomService.getRoomInfoList());
		}

        return restResponseDto;
	}

	//itemPkget
    @RequestMapping("itemPkget")
	public RestResponseDto itemPkget(@RequestParam("room_info_id") int room_info_id){
	
	RestResponseDto responseDto = new RestResponseDto();

	responseDto.setData(roomService.itemPkGetByRoomInfoId(room_info_id));
	responseDto.setResult("success");
	
	return responseDto;
	}

	// orderInfoInsert
    @RequestMapping("roomOrderInfoInsert")
        public RestResponseDto roomOrderInfoInsert(@RequestParam("item_id") int item_id, @RequestParam("room_info_id") int room_info_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();
		
        int roomInfoUserId = roomService.userPkByRoomInfoId(room_info_id);
        String roomInfoUserIdString = String.valueOf(roomInfoUserId); // string값으로 변환시켜줘야됨
        

        // 여긴 insert를 위한 코드 
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        // 주문번호 생성
        // MI + 상품코드 + 주문이 발생한 연도 + 주문이 발생한 월 + 주문이 발생한 날짜 + UUID(substring(0, 10))
        String orderNumber = "RM" + item_id;

        // 현재 날짜 정보 가져오기
        
        Date currentDate = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        // 주문이 발생한 연도, 월, 날짜 추가
        orderNumber += yearFormat.format(currentDate);
        orderNumber += monthFormat.format(currentDate);
        orderNumber += dayFormat.format(currentDate);
        
        // UUID 생성 및 substring(0, 10)으로 처리
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        orderNumber += uuid;
        
        // 대입
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setItem_id(item_id);
        orderInfoDto.setUser_id(user_id);
        orderInfoDto.setStatus("READY");
        orderInfoDto.setOrder_id(orderNumber);

        // insert
        roomService.orderInfoInsert(orderInfoDto);
        
        // 데이터 보낼거 ==> 주문정보pk + 판매자userpk
        Map<String, Object> map = new HashMap<>();
        map.put("room_user_id", roomInfoUserIdString);
        map.put("order_id", roomService.orderIdMax());
        map.put("item_id", item_id); // 그냥한번넣어봄 test 지워도 무관
        
        responseDto.setData(map);
        responseDto.setResult("success");
        
        return responseDto;
    }
	
	//kakaoPaymentInfoInsert 결제요청
    @RequestMapping("kakaoPaymentReqInsert")
      public RestResponseDto kakaoPaymentReqInsert(@RequestBody KakaoPaymentReqDto kakaoPaymentReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

		roomService.kakaoPaymentReqInsert(kakaoPaymentReqDto);

        responseDto.setResult("success");
      
        return responseDto;
      
    }
    //kakaoPaymentResInsert 결제준비응답
    @RequestMapping("kakaoPaymentResInsert")
      public RestResponseDto kakaoPaymentResInsert(@RequestBody KakaoPaymentResDto kakaoPaymentResDto){
      
      RestResponseDto responseDto = new RestResponseDto();

      roomService.kakaoPaymentResInsert(kakaoPaymentResDto);
      
      responseDto.setResult("success");
      
      return responseDto;
    }

    // kakaoPaymentAcceptReqInsert  요청값 insert
    @RequestMapping("kakaoPaymentAcceptReqInsert")
        public RestResponseDto kakaoPaymentAcceptReqInsert(@RequestBody KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        roomService.kakaoPaymentAcceptReqInsert(kakaoPaymentAcceptReqDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 승인응답테이블 ==> 여기서 amount는 서버에서 dto로 응답받음 insert시키고 max값 받아서 insert시켜야됨 (amount먼저)
    @RequestMapping("kakaoPaymentAcceptResInsert") 
        public RestResponseDto kakaoPaymentAcceptResInsert(@RequestBody KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto){
        
        RestResponseDto responseDto = new RestResponseDto();
        // 여기서 amountpk랑 cardpk받아서 insert 시켜야함
        kakaoPaymentAcceptResDto.setAmount(roomService.amountIdMax());
        kakaoPaymentAcceptResDto.setCard_info(roomService.cardIdMax());
        
        // 이거 승인 완료시간이 이상함??? 21시 몇분 이렇게 나옴 => 난 12시에 함 !!!!!!
        roomService.kakaoPaymentAcceptResInsert(kakaoPaymentAcceptResDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    

	// 세션값에 cid tid partner user_id, order_id 저장
    @RequestMapping("sessionPaymentInfoRestore")
        public RestResponseDto sessionPaymentInfoRestore(HttpSession session, @RequestBody KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        session.setAttribute("kakaoPaymentAcceptReqValue", kakaoPaymentAcceptReqDto); // 세션값에 value를 집어넣은다음에 성공페이지에서 값을 insert해줌

        responseDto.setResult("success");
        
        return responseDto;
    }
    // sessionKakaoAcceptReqValue
    @RequestMapping("sessionKakaoAcceptReqValue")
        public RestResponseDto sessionKakaoAcceptReqValue(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto =(KakaoPaymentAcceptReqDto)session.getAttribute("kakaoPaymentAcceptReqValue");
        
        responseDto.setData(kakaoPaymentAcceptReqDto);
        responseDto.setResult("success");
        
        return responseDto;
    }

    //amountInfoInsert
    @RequestMapping("amountInfoInsert")
        public RestResponseDto amountInfoInsert(@RequestBody AmountDto amountDto){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        roomService.amountInfoInsert(amountDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // cardInfoInsert
    @RequestMapping("cardInfoInsert")
        public RestResponseDto cardInfoInsert(@RequestBody CardInfoDto cardInfoDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        roomService.cardInfoInsert(cardInfoDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    //orderStatusChange
    @RequestMapping("orderStatusChange")
        public RestResponseDto orderStatusChange(@RequestParam("order_id") String order_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        roomService.orderInfoStatusByOrderId(order_id);
        
        responseDto.setResult("success");
        
        return responseDto;
    }

	public RestResponseDto templete() {
        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setResult("success");
        //restResponseDto.setData(count);
        return restResponseDto;
	}
}
