package com.cu.ufuf.room.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.InterestRoomDto;
import com.cu.ufuf.dto.RoomGuestDto;
import com.cu.ufuf.dto.RoomGuestReviewDto;
import com.cu.ufuf.dto.RoomGuestReviewImageDto;
import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionCategoryDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface RoomSqlMapper {

    //방 등록
    public int creatRoomInfoId();
    public void roomInsert(RoomInfoDto roomInfoDto);
    public void insertRoomOption(RoomOptionDto roomOptionDto);
    public void insertRoomDetailImage(RoomImageDto roomImageDto);

    public List<RoomOptionCategoryDto> selectRoomOptionCategoryAll();

    //방 예약
    public void insertRoomGuestInfo(RoomGuestDto roomGuestDto);

    //방 리스트뽑게 
     public List<RoomInfoDto> roomSelectAll();

     public UserInfoDto selectByUserId(int user_id);

     //방 상세
     public RoomInfoDto roomSelectById(int room_info_id);
     public List<RoomImageDto> roomImageSelectById(int room_info_id);
     public List<RoomOptionDto> roomOptionSelectById(int room_info_id);
     public RoomOptionCategoryDto roomOptionCategorySelectById(int room_option_category_id);

     public int roomImageCount(int room_info_id);
     public RoomInfoDto roomSelectByRoomAndUserIdForGuest(int user_id, int room_info_id);

     //예약자 정보
     public int roomGuestSelectByRoomAndUserId(RoomGuestDto roomGuestDto);

     //예약자가 예약한곳 리스트
     public List<RoomGuestDto> roomGuestSelectByUserId(int user_id);
 
     //몇박인지
     public int reservationDuration(int user_id, int room_info_id);

     //총 얼마인지
     public int reservationRoomCharge(int user_id, int room_info_id);

     //추가금
     public int reservationExtraCharge(int user_id, int room_info_id);


    public RoomGuestDto roomGuestSelectByRoomId(int user_id, int room_info_id);
    public RoomGuestDto roomGuestSelectByRoomAndUserId(int user_id, int room_info_id);

    //리뷰 등록
    public int creatGuestReviewId();
    public void insertGuestReview(RoomGuestReviewDto roomGuestReviewDto);
    public void insertGuestReviewImage(RoomGuestReviewImageDto roomGuestReviewImageDto);


    //관심 방

    //관심방 등록
    public void insertInterestRoom(InterestRoomDto interestRoomDto);
    //관심 취소
    public void deleteInterestRoom(InterestRoomDto interestRoomDto);

    //글 전체 좋아요 카운트
    public int roomInterestTotalCount(int room_info_id);
    //유저 그 글에 좋아요 했는지 카운트
    public int roomInterestUserCount(InterestRoomDto interestRoomDto);

    //유저 좋아요 목록
    public List<InterestRoomDto> UserInterestRoom(int user_id);

    //수정용
    public void updateRoomInfo(RoomInfoDto roomInfoDto);

    //이미지 삭제
    public void deleteRoomImage(int room_info_id);

    //카테고리 삭제
    public void deleteRoomOption(int room_info_id);

    //방 정보 삭제
    public void deleteRoomInfo(int room_info_id);


}
