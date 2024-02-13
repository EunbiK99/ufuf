package com.cu.ufuf.room.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.InterestRoomDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.RoomGuestDto;
import com.cu.ufuf.dto.RoomGuestReviewDto;
import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionCategoryDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.room.mapper.RoomSqlMapper;

@Service
public class RoomServiceIpml {

    @Autowired
    private RoomSqlMapper roomSqlMapper;

    //방 등록하기
    public void roomRegister(RoomInfoDto roomInfoDto,int[] roomOptionCategoryIdList,List<RoomImageDto> roomImageDtoList){
       
        int roomPk =roomSqlMapper.creatRoomInfoId();
                
        roomInfoDto.setRoom_info_id(roomPk);

        //사진
        for(RoomImageDto roomImageDto : roomImageDtoList) {
            roomImageDto.setRoom_info_id(roomPk);
            roomSqlMapper.insertRoomDetailImage(roomImageDto);
        }

        //옵션
        if(roomOptionCategoryIdList != null) {
            for(int roomOptionCategoryId : roomOptionCategoryIdList) {
                RoomOptionDto roomOptionDto=new RoomOptionDto();
                roomOptionDto.setRoom_option_category_id(roomOptionCategoryId); 
                roomOptionDto.setRoom_info_id(roomPk);
                
                roomSqlMapper.insertRoomOption(roomOptionDto);
            }
        }
    

        
        roomSqlMapper.roomInsert(roomInfoDto);
    }

    public List<RoomOptionCategoryDto> getRoomOptionlist(){
        return roomSqlMapper.selectRoomOptionCategoryAll();
    }

    //예약하기
    public void roomReservation(RoomGuestDto roomGuestDto){
        roomSqlMapper.insertRoomGuestInfo(roomGuestDto);
    }

    //방 목록 필터용
    public List<Map<String, Object>> getRoomInfoListForFilter(String searchWord,int peopleCount, LocalDate startSchedule, LocalDate endSchedule) {


		List<Map<String, Object>> roomList=new ArrayList<>();
		
		List<RoomInfoDto> roomInfoDtoList=roomSqlMapper.roomSelectFilter(searchWord, peopleCount,startSchedule,endSchedule);
		
		for(RoomInfoDto roomInfoDto:roomInfoDtoList) {
			int UserPK=roomInfoDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			
			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //방 목록 필터용(비싼순)
    public List<Map<String, Object>> getRoomInfoListForFilterAndChargeDesc(String searchWord,int peopleCount, LocalDate startSchedule, LocalDate endSchedule) {


		List<Map<String, Object>> roomList=new ArrayList<>();
		
		List<RoomInfoDto> roomInfoDtoList=roomSqlMapper.roomSelectFilterAndChargeDesc(searchWord, peopleCount,startSchedule,endSchedule);
		
		for(RoomInfoDto roomInfoDto:roomInfoDtoList) {
			int UserPK=roomInfoDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			
			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //방 목록 필터용(싼순)
    public List<Map<String, Object>> getRoomInfoListForFilterAndChargeAsc(String searchWord,int peopleCount, LocalDate startSchedule, LocalDate endSchedule) {


		List<Map<String, Object>> roomList=new ArrayList<>();
		
		List<RoomInfoDto> roomInfoDtoList=roomSqlMapper.roomSelectFilterAndChargeAsc(searchWord, peopleCount,startSchedule,endSchedule);
		
		for(RoomInfoDto roomInfoDto:roomInfoDtoList) {
			int UserPK=roomInfoDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			
			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //서치용 방 목록
    public List<Map<String, Object>> getRoomInfoListForSearchLocation(String searchWord) {
		List<Map<String, Object>> roomList=new ArrayList<>();
		
		List<RoomInfoDto> roomInfoDtoList=roomSqlMapper.roomSelectAllForSearchLocation(searchWord);
		
		for(RoomInfoDto roomInfoDto:roomInfoDtoList) {
			int UserPK=roomInfoDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			
			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //방 목록 기본
    public List<Map<String, Object>> getRoomInfoList() {
		
		List<Map<String, Object>> roomList=new ArrayList<>();
		
		List<RoomInfoDto> roomInfoDtoList=roomSqlMapper.roomSelectAll();
		
		for(RoomInfoDto roomInfoDto:roomInfoDtoList) {
			int UserPK=roomInfoDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			
			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //방별로 예약자 목록
    public List<Map<String, Object>> getRoomReservationList(int room_info_id) {
		
		List<Map<String, Object>> roomReservationList=new ArrayList<>();
		
		List<RoomGuestDto> roomGuestDtoList=roomSqlMapper.roomReservationList(room_info_id);
		
		for(RoomGuestDto roomGuestDto:roomGuestDtoList) {
			int UserPK=roomGuestDto.getUser_id();
			UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);

            RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(room_info_id);
            //몇박인지
            int reservationDuration=roomSqlMapper.reservationDuration(UserPK, room_info_id);
            
            //기본 숙박비
            int standardRoomCharge=roomSqlMapper.reservationRoomCharge(UserPK, room_info_id);
            //추가요금
            int extraCharge=roomSqlMapper.reservationExtraCharge(UserPK, room_info_id);
            //총 요금
            int totalCost=roomSqlMapper.reservationRoomCharge(UserPK, room_info_id)+roomSqlMapper.reservationExtraCharge(UserPK, room_info_id);

			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
            map.put("roomGuestDto", roomGuestDto);
			map.put("userDto", userDto);

            map.put("reservationDuration", reservationDuration);
            map.put("standardRoomCharge", standardRoomCharge);
            map.put("extraCharge", extraCharge);
            map.put("totalCost", totalCost);
			
			roomReservationList.add(map);

			
		}
		
		return roomReservationList;
		
	}
    

    public List<Map<String, Object>> getReviewList() {
		
		List<Map<String, Object>> roomReviewList=new ArrayList<>();
		
		List<RoomGuestReviewDto> roomGuestReviewDtoList=roomSqlMapper.roomReviewListForMainPage();
		
		for(RoomGuestReviewDto roomGuestReviewDto:roomGuestReviewDtoList) {

            List<RoomGuestDto> roomGuestDtoList=roomSqlMapper.roomGuestSelectByGuestId(roomGuestReviewDto.getRoom_guest_id());
            
            for(RoomGuestDto roomGuestDto:roomGuestDtoList){
                RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(roomGuestDto.getRoom_info_id());

                int UserPK=roomGuestDto.getUser_id();
                UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);
                
                Map<String, Object> map=new HashMap<>();
                map.put("roomInfoDto", roomInfoDto);
                map.put("userDto", userDto);
                map.put("roomGuestReviewDto",roomGuestReviewDto);
                map.put("roomGuestDto",roomGuestDto);
                
                roomReviewList.add(map);
            };
		}
		
		return roomReviewList;
		
	}
	
    //룸 상세보기
	public Map<String, Object> getRoomInfo(int room_info_id){
		
        
		Map<String, Object> roomMap=new HashMap<>();
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(room_info_id);

        int UserPK=roomInfoDto.getUser_id();
		UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);

        
        List<RoomImageDto> roomImageDto=roomSqlMapper.roomImageSelectById(room_info_id);
        List<RoomOptionDto> roomOptionDto=roomSqlMapper.roomOptionSelectById(room_info_id);
        List<RoomOptionCategoryDto> roomOptionCategoryDto = new ArrayList<>();
        List<RoomGuestReviewDto> roomGuestReviewDto=roomSqlMapper.roomReviewListForRoomInfo(room_info_id);
        for (RoomOptionDto optionDto : roomOptionDto) {
            int categoryId = optionDto.getRoom_option_category_id();
            RoomOptionCategoryDto categoryDto = roomSqlMapper.roomOptionCategorySelectById(categoryId);
            roomOptionCategoryDto.add(categoryDto);
        }
		
        int roomImageCount=roomSqlMapper.roomImageCount(room_info_id);
        int roomReviewCount=roomSqlMapper.roomReviewCount(room_info_id);

        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);
		roomMap.put("roomImageDto", roomImageDto);
		roomMap.put("roomOptionDto", roomOptionDto);
		roomMap.put("roomOptionCategoryDto", roomOptionCategoryDto);
        roomMap.put("roomImageCount", roomImageCount);
        roomMap.put("roomGuestReviewDto",roomGuestReviewDto);
        roomMap.put("roomReviewCount",roomReviewCount);

        //이미지랑 옵션 반복문 돌려야하나 멩,,,,일단 해야지
		
		return roomMap;
	}

    public Map<String, Object> getRoomInfoForReservation(int room_info_id) {
		
		Map<String, Object> roomMap=new HashMap<>();
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(room_info_id);

        int UserPK=roomInfoDto.getUser_id();
		UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);


        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);

		return roomMap;
		
	}

    public Map<String, Object> getReservationInfoTest(int user_id) {
		
		Map<String, Object> roomMap=new HashMap<>();

       

        RoomGuestDto roomGuestDto=roomSqlMapper.selectMaxGuestIdInfo(roomSqlMapper.maxGuestId());
        

		UserInfoDto userDto=roomSqlMapper.selectByUserId(roomGuestDto.getUser_id());
        
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectByRoomAndUserIdForGuest(roomGuestDto.getRoom_info_id(),roomGuestDto.getRoom_guest_id());

        //몇박인지
        int reservationDuration=roomSqlMapper.reservationDuration(user_id, roomGuestDto.getRoom_info_id());
        
        //기본 숙박비
        int standardRoomCharge=roomSqlMapper.reservationRoomCharge(user_id, roomGuestDto.getRoom_info_id());
        //추가요금
        int extraCharge=roomSqlMapper.reservationExtraCharge(user_id, roomGuestDto.getRoom_info_id());
        //총 요금
        int totalCost=roomSqlMapper.reservationRoomCharge(user_id, roomGuestDto.getRoom_info_id())+roomSqlMapper.reservationExtraCharge(user_id, roomGuestDto.getRoom_info_id());


        int roomReviewCount=roomSqlMapper.guestRoomReviewCount(roomGuestDto.getRoom_guest_id());

        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);
        roomMap.put("roomGuestDto", roomGuestDto);
        
        roomMap.put("reservationDuration", reservationDuration);
        roomMap.put("standardRoomCharge", standardRoomCharge);
        roomMap.put("extraCharge", extraCharge);
        roomMap.put("totalCost", totalCost);
        roomMap.put("roomReviewCount", roomReviewCount);

		return roomMap;
		
	}

    public Map<String, Object> getReservationInfo(int user_id,int room_info_id) {
		
		Map<String, Object> roomMap=new HashMap<>();

        System.out.println(user_id);

        RoomGuestDto roomGuestDto=roomSqlMapper.roomGuestSelectByRoomAndUserId(user_id, room_info_id);
        

		UserInfoDto userDto=roomSqlMapper.selectByUserId(roomGuestDto.getUser_id());
        
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectByRoomAndUserIdForGuest(roomGuestDto.getRoom_info_id(),roomGuestDto.getRoom_guest_id());

        //몇박인지
        int reservationDuration=roomSqlMapper.reservationDuration(user_id, room_info_id);
        
        //기본 숙박비
        int standardRoomCharge=roomSqlMapper.reservationRoomCharge(user_id, room_info_id);
        //추가요금
        int extraCharge=roomSqlMapper.reservationExtraCharge(user_id, room_info_id);
        //총 요금
        int totalCost=roomSqlMapper.reservationRoomCharge(user_id, room_info_id)+roomSqlMapper.reservationExtraCharge(user_id, room_info_id);


        int roomReviewCount=roomSqlMapper.guestRoomReviewCount(roomGuestDto.getRoom_guest_id());

        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);
        roomMap.put("roomGuestDto", roomGuestDto);
        
        roomMap.put("reservationDuration", reservationDuration);
        roomMap.put("standardRoomCharge", standardRoomCharge);
        roomMap.put("extraCharge", extraCharge);
        roomMap.put("totalCost", totalCost);
        roomMap.put("roomReviewCount", roomReviewCount);

		return roomMap;
		
	}

    public List<Map<String, Object>> roomReservationList(int user_id) {
		
		List<Map<String, Object>> roomList=new ArrayList<>();
		
        List<RoomGuestDto> roomGuestDtoList=roomSqlMapper.roomGuestSelectByUserId(user_id);

		for(RoomGuestDto roomGuestDto:roomGuestDtoList) {

            RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(roomGuestDto.getRoom_info_id());


			UserInfoDto userDto=roomSqlMapper.selectByUserId(roomGuestDto.getUser_id());
            int roomReviewCount=roomSqlMapper.guestRoomReviewCount(roomGuestDto.getRoom_guest_id());
           
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			map.put("roomGuestDto", roomGuestDto);
            map.put("roomReviewCount", roomReviewCount);

			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //리뷰 등록하기
    public void guestReviewRegister(RoomGuestReviewDto roomGuestReviewDto){
       
        roomSqlMapper.insertGuestReview(roomGuestReviewDto);
    }


    //좋아요
    public void toggleInterestRoom(InterestRoomDto interestRoomDto) {
		
		if(roomSqlMapper.roomInterestUserCount(interestRoomDto) > 0) {
			roomSqlMapper.deleteInterestRoom(interestRoomDto);
		}else {
			roomSqlMapper.insertInterestRoom(interestRoomDto);
		}
	}
	
	public int getTotalInterestCount(int room_info_id) {
		return roomSqlMapper.roomInterestTotalCount(room_info_id);
	}
	
    //얘 머더라
	public boolean isInterestRoom(InterestRoomDto interestRoomDto) {
		return roomSqlMapper.roomInterestUserCount(interestRoomDto) > 0 ? true : false;
	}


    //유저가 좋아요 한 방 목록
    public List<Map<String,Object>> userInterestRoomList(int user_id){

        List<Map<String, Object>> InterestRoomList=new ArrayList<>();
		
        List<InterestRoomDto> interestRoomDtoList=roomSqlMapper.userInterestRoom(user_id);

		for(InterestRoomDto interestRoomDto:interestRoomDtoList) {

            RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(interestRoomDto.getRoom_info_id());
			UserInfoDto userDto=roomSqlMapper.selectByUserId(interestRoomDto.getUser_id());
            
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			map.put("interestRoomDto", interestRoomDto);

			InterestRoomList.add(map);

			
		}
		
		return InterestRoomList;
    }


    //방 수정하기
    public void updateRoom(RoomInfoDto roomInfoDto, int[] roomOptionCategoryIdList){
        //옵션
        if(roomOptionCategoryIdList != null) {
            roomSqlMapper.deleteRoomOption(roomInfoDto.getRoom_info_id());
            
            for(int roomOptionCategoryId : roomOptionCategoryIdList) {
                RoomOptionDto roomOptionDto=new RoomOptionDto();
                roomOptionDto.setRoom_option_category_id(roomOptionCategoryId); 
                roomOptionDto.setRoom_info_id(roomInfoDto.getRoom_info_id());
                
                roomSqlMapper.insertRoomOption(roomOptionDto);
            }
        }
        
        roomSqlMapper.updateRoomInfo(roomInfoDto);
    }

    //방 이미지 수정
    public void updateRoomDetailImage(RoomInfoDto roomInfoDto, List<RoomImageDto> roomImageDtoList){
        roomSqlMapper.deleteRoomImage(roomInfoDto.getRoom_info_id());

        //사진
        for(RoomImageDto roomImageDto : roomImageDtoList) {
            roomImageDto.setRoom_info_id(roomInfoDto.getRoom_info_id());
            roomSqlMapper.insertRoomDetailImage(roomImageDto);
        }

    }

    //방 삭제
    public void deleteRoomInfo(int room_info_id){
        roomSqlMapper.deleteRoomInfo(room_info_id);
        roomSqlMapper.deleteRoomImage(room_info_id);
        roomSqlMapper.deleteRoomOption(room_info_id);
        roomSqlMapper.deleteRoomGuest(room_info_id);
        roomSqlMapper.deleteRoomGuestReview(room_info_id);
    }
    



    //카카오페이
    
    public void itemInfoInsert(int room_info_id){
        
        roomSqlMapper.itemInfoInsert(room_info_id);
    }

    public int roomInfoIdMaxValue(){
        
        return roomSqlMapper.roomInfoIdMaxValue();
    }
    
    public void kakaoPaymentReqInsert(KakaoPaymentReqDto kakaoPaymentReqDto){
        
        roomSqlMapper.kakaoPaymentReqInsert(kakaoPaymentReqDto);
    }
    public void orderInfoInsert(OrderInfoDto orderInfoDto){

        roomSqlMapper.orderInfoInsert(orderInfoDto);
    }
    public int itemPkGetByRoomInfoId(int room_info_id){

        return roomSqlMapper.itemPkGetByRoomInfoId(room_info_id);
    }
    public int userPkByRoomInfoId(int room_info_id){

        return roomSqlMapper.userPkByRoomInfoId(room_info_id);
    }


    public String orderIdMax(){
        
        return roomSqlMapper.orderIdMax();
    }

    public void kakaoPaymentResInsert(KakaoPaymentResDto kakaoPaymentResDto){
        
        roomSqlMapper.kakaoPaymentResInsert(kakaoPaymentResDto);
    }
    public void kakaoPaymentAcceptReqInsert(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){

        roomSqlMapper.kakaoPaymentAcceptReqInsert(kakaoPaymentAcceptReqDto);
    }
    public void kakaoPaymentAcceptResInsert(KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto){

        roomSqlMapper.kakaoPaymentAcceptResInsert(kakaoPaymentAcceptResDto);
    }

     public int cardIdMax(){
        
        return roomSqlMapper.cardIdMax();
    }
    public int amountIdMax(){
        
        return roomSqlMapper.amountIdMax();
    }public void amountInfoInsert(AmountDto amountDto){
        
        roomSqlMapper.amountInfoInsert(amountDto);
    }
    public void cardInfoInsert(CardInfoDto cardInfoDto){

        roomSqlMapper.cardInfoInsert(cardInfoDto);
    }

 public void orderInfoStatusByOrderId(String order_id){

    roomSqlMapper.orderInfoStatusByOrderId(order_id);
    }
}
