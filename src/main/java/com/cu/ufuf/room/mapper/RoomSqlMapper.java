package com.cu.ufuf.room.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
import com.cu.ufuf.dto.RoomGuestReviewImageDto;
import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionCategoryDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.dto.UserInfoDto;

import java.time.LocalDate;

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

    //방 리스트 기본
    public List<RoomInfoDto> roomSelectAll();

    //방 리스트 검색용(기본)
    public List<RoomInfoDto> roomSelectAllForSearchLocation(@Param("searchWord") String searchWord);

    //방 리스트 검색용(비싼순)
    public List<RoomInfoDto> roomSelectAllForSearchLocationAndChargeDesc(@Param("searchWord") String searchWord);

    //방 리스트 검색용(싼순)
    public List<RoomInfoDto> roomSelectAllForSearchLocationAndChargeAsc(@Param("searchWord") String searchWord);
    
    //방 리스트 필터 적용용(기본용)
    public List<RoomInfoDto> roomSelectFilter(@Param("searchWord") String searchWord,
                                              @Param("peopleCount") int peopleCount, 
                                              @Param("startSchedule") LocalDate startSchedule,
                                              @Param("endSchedule") LocalDate endSchedule);

    //방 리스트 필터 적용용(비싼순)
    public List<RoomInfoDto> roomSelectFilterAndChargeDesc(@Param("searchWord") String searchWord,
                                              @Param("peopleCount") int peopleCount, 
                                              @Param("startSchedule") LocalDate startSchedule,
                                              @Param("endSchedule") LocalDate endSchedule);
    
    
    //방 리스트 필터 적용용(비싼순)
    public List<RoomInfoDto> roomSelectFilterAndChargeAsc(@Param("searchWord") String searchWord,
                                              @Param("peopleCount") int peopleCount, 
                                              @Param("startSchedule") LocalDate startSchedule,
                                              @Param("endSchedule") LocalDate endSchedule);


    public UserInfoDto selectByUserId(int user_id);

     //방 상세
     public RoomInfoDto roomSelectById(int room_info_id);
     public List<RoomImageDto> roomImageSelectById(int room_info_id);
     public List<RoomOptionDto> roomOptionSelectById(int room_info_id);
     public RoomOptionCategoryDto roomOptionCategorySelectById(int room_option_category_id);

     public int roomImageCount(int room_info_id);
     public RoomInfoDto roomSelectByRoomAndUserIdForGuest(int room_info_id, int room_guest_id);

    //예약자 정보

    //방별로 예약자 리스트
    public List<RoomGuestDto> roomReservationList(int room_info_id);

     //게스트 아이디로 뽑아오는거
     public List<RoomGuestDto> roomGuestSelectByGuestId(int room_guest_id);

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

    public int maxGuestId();
    public RoomGuestDto selectMaxGuestIdInfo(int room_guest_id);

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
    public List<InterestRoomDto> userInterestRoom(int user_id);

    //수정용
    public void updateRoomInfo(RoomInfoDto roomInfoDto);

    //이미지 삭제
    public void deleteRoomImage(int room_info_id);

    //카테고리 삭제
    public void deleteRoomOption(int room_info_id);

    //방 정보 삭제
    public void deleteRoomInfo(int room_info_id);

    //게스트 삭제
    public void deleteRoomGuest(int room_info_id);

    //게스트 리뷰 삭제
    public void deleteRoomGuestReview(int room_info_id);


    //방 상세페이지용 리뷰 리스트
    public List<RoomGuestReviewDto> roomReviewListForRoomInfo(int room_info_id);
    
    //상세페이지용 리뷰 카운트
    public int roomReviewCount(int room_info_id);

    //유저용 리뷰 리스트
    public List<RoomGuestReviewDto> roomReviewListForUser(int room_info_id);

    //메인페이지용 리뷰 리스트
    public List<RoomGuestReviewDto> roomReviewListForMainPage();

    //리뷰 썼나 안썼나
    public int guestRoomReviewCount(int room_guest_id);

    
    //카카오페이

    public int roomInfoIdMaxValue();

    // 카카오페이 상품테이블 insert
    public void itemInfoInsert(int room_info_id);

    // 카카오결제요청 테이블 insert && 결제응답테이블 insert && 주문번호테이블 insert
    public void kakaoPaymentReqInsert(KakaoPaymentReqDto kakaoPaymentReqDto);
    public void kakaoPaymentResInsert(KakaoPaymentResDto kakaoPaymentResDto);
    public void orderInfoInsert(OrderInfoDto orderInfoDto);

    //itemPk값 가져오기 & userPk가져오기(방 등록한사람)
    public int itemPkGetByRoomInfoId(int room_info_id);
    public int userPkByRoomInfoId(int room_info_id);

    //orderId 가져오기
    public String orderIdMax();

    // 카카오결제승인요청 테이블 insert && 결제승인응답 테이블 insert
    public void kakaoPaymentAcceptReqInsert(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto);
    public void kakaoPaymentAcceptResInsert(KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto);

    // 카카오페이 Amount
    public void amountInfoInsert(AmountDto amountDto);
    public void cardInfoInsert(CardInfoDto cardInfoDto);

    // Max값 추출
    public int cardIdMax();
    public int amountIdMax();

    // orderInfo 업데이트 '결제완료'
    public void orderInfoStatusByOrderId(String order_id);



}
