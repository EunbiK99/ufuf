package com.cu.ufuf.room.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.RoomGuestDto;
import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionCategoryDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface RoomSqlMapper {

    //방 등록
    public int creatRoomInfoPk();
    public void roomInsert(RoomInfoDto roomInfoDto);
    public void insertRoomOption(RoomOptionDto roomOptionDto);
    public void insertRoomDetailImage(RoomImageDto roomImageDto);

    public List<RoomOptionCategoryDto> selectRoomOptionCategoryAll();

    //방 예약
    public void insertRoomGuestInfo(RoomGuestDto roomGuestDto);

    //방 리스트뽑게 
     public List<RoomInfoDto> roomSelectAll();
     public RoomInfoDto selectById(int room_info_id);

     public UserInfoDto selectByUserId(int user_id);

     //방 상세
     public RoomInfoDto roomSelectById(int room_info_id);
     public List<RoomImageDto> roomImageSelectById(int room_info_id);
     public List<RoomOptionDto> roomOptionSelectById(int room_info_id);
     public RoomOptionCategoryDto roomOptionCategorySelectById(int room_option_category_id);

     public int roomImageCount(int room_info_id);

     //예약자 정보
     public RoomGuestDto roomGuestSelectByRoomInfoAndUserId(int room_info_id);
 
     //몇박인지
     public int reservationDuration(int room_info_id);

     //총 얼마인지
     public int reservationRoomCharge(int room_info_id);

     //추가금
     public int reservationExtraCharge(int room_info_id);
}
