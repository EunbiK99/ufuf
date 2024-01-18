package com.cu.ufuf.room.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.RoomGuestDto;
import com.cu.ufuf.dto.RoomGuestReviewDto;
import com.cu.ufuf.dto.RoomGuestReviewImageDto;
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
	
    //룸 상세보기
	public Map<String, Object> getRoomInfo(int room_info_id){
		
        
		Map<String, Object> roomMap=new HashMap<>();
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(room_info_id);

        int UserPK=roomInfoDto.getUser_id();
		UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);

        
        List<RoomImageDto> roomImageDto=roomSqlMapper.roomImageSelectById(room_info_id);
        List<RoomOptionDto> roomOptionDto=roomSqlMapper.roomOptionSelectById(room_info_id);
        List<RoomOptionCategoryDto> roomOptionCategoryDto = new ArrayList<>();
        for (RoomOptionDto optionDto : roomOptionDto) {
            int categoryId = optionDto.getRoom_option_category_id();
            RoomOptionCategoryDto categoryDto = roomSqlMapper.roomOptionCategorySelectById(categoryId);
            roomOptionCategoryDto.add(categoryDto);
        }
		
        int roomImageCount=roomSqlMapper.roomImageCount(room_info_id);

        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);
		roomMap.put("roomImageDto", roomImageDto);
		roomMap.put("roomOptionDto", roomOptionDto);
		roomMap.put("roomOptionCategoryDto", roomOptionCategoryDto);
        roomMap.put("roomImageCount", roomImageCount);

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

    public Map<String, Object> getReservationInfo(int user_id,int room_info_id) {
		
		Map<String, Object> roomMap=new HashMap<>();

        RoomGuestDto roomGuestDto=roomSqlMapper.roomGuestSelectByRoomAndUserId(user_id, room_info_id);
		RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectByRoomAndUserIdForGuest(roomGuestDto.getUser_id(),roomGuestDto.getRoom_info_id());
        System.out.println(roomInfoDto.getRoom_info_id());

        //몇박인지
        int reservationDuration=roomSqlMapper.reservationDuration(user_id, room_info_id);
        
        //기본 숙박비
        int standardRoomCharge=roomSqlMapper.reservationRoomCharge(user_id, room_info_id);
        //추가요금
        int extraCharge=roomSqlMapper.reservationExtraCharge(user_id, room_info_id);
        //총 요금
        int totalCost=roomSqlMapper.reservationRoomCharge(user_id, room_info_id)+roomSqlMapper.reservationExtraCharge(user_id, room_info_id);

        int UserPK=roomGuestDto.getUser_id();
		UserInfoDto userDto=roomSqlMapper.selectByUserId(UserPK);

        roomMap.put("roomInfoDto", roomInfoDto);
        roomMap.put("userDto", userDto);
        roomMap.put("roomGuestDto", roomGuestDto);
        
        roomMap.put("reservationDuration", reservationDuration);
        roomMap.put("standardRoomCharge", standardRoomCharge);
        roomMap.put("extraCharge", extraCharge);
        roomMap.put("totalCost", totalCost);

		return roomMap;
		
	}

    public List<Map<String, Object>> roomReservationList(int user_id) {
		
		List<Map<String, Object>> roomList=new ArrayList<>();
		
        List<RoomGuestDto> roomGuestDtoList=roomSqlMapper.roomGuestSelectByUserId(user_id);

		for(RoomGuestDto roomGuestDto:roomGuestDtoList) {

            RoomInfoDto roomInfoDto=roomSqlMapper.roomSelectById(roomGuestDto.getRoom_info_id());
			UserInfoDto userDto=roomSqlMapper.selectByUserId(roomGuestDto.getUser_id());
            
			
			Map<String, Object> map=new HashMap<>();
			map.put("roomInfoDto", roomInfoDto);
			map.put("userDto", userDto);
			map.put("roomGuestDto", roomGuestDto);

			roomList.add(map);

			
		}
		
		return roomList;
		
	}

    //리뷰 등록하기
    public void guestReviewRegister(RoomGuestReviewDto roomGuestReviewDto){
       
        roomSqlMapper.insertGuestReview(roomGuestReviewDto);
    }
    
}
