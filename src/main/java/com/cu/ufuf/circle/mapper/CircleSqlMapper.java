package com.cu.ufuf.circle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleBoardImageDto;
import com.cu.ufuf.dto.CircleBoardLikeDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleGradeDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleLikeDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleScheduleApplyDto;
import com.cu.ufuf.dto.CircleScheduleAttendanceDto;
import com.cu.ufuf.dto.CircleScheduleDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.CircleVoteCompleteDto;
import com.cu.ufuf.dto.CircleVoteDto;
import com.cu.ufuf.dto.CircleVoteOptionDto;
import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;

@Mapper
public interface CircleSqlMapper {
    
    // 소규모 카테고리 리스트
    public List<CircleMiddleCategoryDto> circlemiddlecategoryInfoAll();
    public List<CircleSmallCategoryDto> circlesmallcategoryInfoAll();
    public List<CircleSmallCategoryDto> circlesmallcategoryInfoByMiddleId(int circle_middle_category_id);

    // 동아리 등록
    public void circleInfoInsert(CircleDto circleDto);

    // 공고 이미지 등록
    public void circleNoticeImageInfoInsert(CircleNoticeImageDto circleNoticeImageDto);

    // 동아리번호max값 가져오기
    public int circleIdMaxByUserId(int user_id);

    // 동아리 회원정보 입력
    public void cirlceMemberinfoInsert(CircleMemberDto circleMemberDto);
    
    // 동아리 핫한 3개 리스트 가져오기 & 신규순전체 동아리 리스트
    public List<CircleDto> circleInfoHotThree();
    public List<CircleDto> circleNewListOrderByCircleId();

    // 동아리 소분류카테고리번호로 카테고리 이름 가져올거.. & 중분류 카테고리 소분류카테고리번호로가져옴
    public CircleSmallCategoryDto circlesmallCategoryListBysmallCategoryId(int circle_small_category_id);
    public CircleMiddleCategoryDto circlemiddleCategoryInfoByMiddleCategoryId(int circle_middle_category_id);

    // 동아리 등급번호로 동아리 등급정보 가져오기 & 동아리 회원수 몇명인지 count
    public CircleGradeDto circleGradeInfoByGradeId(int circle_grade_id);
    public int circleMemberCountInfo(int circle_id);

    // 동아리id로 단일동아리정보 가져오기
    public CircleDto circleInfoByCircleId(int circle_id);

    // 동아리 상세이미지(여러개) 가져오는..
    public List<CircleNoticeImageDto> circleNoticeImageInfoByCircleId(int circle_id);
    
    // 동아리 가입신청 ====> 나중에 직책이 P , M 인 사람이 승인을 할 수 있도록.. 승인여부 Y로 바뀌면 동아리회원정보 insert되도록 만들어야 함
    public void circleJoinApplyInsert(CircleJoinApplyDto circleJoinApplyDto);

    // 동아리 회원정보 (세션userid로 가져올거) => 내가 가지고 있는게 여러개 나오겠지..
    public List<CircleMemberDto> circleMemberInfoByUserId(int user_id);

    // 동아리 회원정보 (동아리 키 기준)
    public List<CircleMemberDto> circleMemberInfoByCircleId(int circle_id);

    // 동아리 게시판리스트 (동아리 회원번호기준)
    public List<CircleBoardDto> circleboardInfoByCircleMemberId(int circle_member_id);

    // 유저정보 by user_id
    public UserInfoDto userInfoByUserId(int user_id);

    // userid and circleid 동아리 회원 정보찾기
    public CircleMemberDto circleMemberInfoByUserIdAndCircleId(@Param("user_id") int user_id, @Param("circle_id") int circle_id);

    // 게시글 등록록
    public void circleboardDtoInsert(CircleBoardDto circleBoardDto);

    // 게시글 내가작성한것중에 최근에 작성한 글찾아오기
    public int boardIdMaxByCircleMemberId(int circle_member_id);

    // 게시글 이미지 등록
    public void circleboardImageDtoInsert(CircleBoardImageDto circleBoardImageDto);
    
    // 투표글생성 & 투표항목생성 => insert
    public void circleVoteInsert(CircleVoteDto circleVoteDto);
    public void circleVoteOptionInsert(CircleVoteOptionDto circleVoteOptionDto);

    // 투표글max 값 가져오기
    public int circleVoteMaxCircleVoteId(int circle_member_id);

    // 해당동아리 투표글 모두가져오기
    public List<CircleVoteDto> circleVoteBoardListByCircleMemberId(int circle_member_id);

    // 투표글번호로 항목리스트 가져오기 & 항목 투표한 동아리 회원번호 찾기 & 인원수로 가져온다면?
    // 맨위에 항목리스트 1번 2번 3번 가져왔다고 치면 그거 하나씩 넣어서 += 해서 다합치면 투표한거 나올듯 ㅎ;; ==> 해결
    public List<CircleVoteOptionDto> circleVoteOptionInfoByCircleVoteId(int Circle_vote_id);
    public List<CircleVoteCompleteDto> circleVoteCompleteInfoByVoteOptionId(int vote_option_id);
    public int circleVoteCompleteCntByVoteOptionId(int vote_option_id); //항목카운트임.. 조심
    
    // 투표글 하나 가져오기 
    public CircleVoteDto circleVoteInfoByCircleVoteId(int circle_vote_id);

    // 투표하면 내가선택한 항목 저장
    public void circleVoteCompleteInfoInsert(CircleVoteCompleteDto circleVoteCompleteDto);

    // 동아리 일정 등록 & 동아리 일정신청(일정신청시 item_info insert후 상품코드 같이 들어가게끔 테이블해놨다고함)
    // 일정출석부
    public void circleScheduleInfoInsert(CircleScheduleDto circleScheduleDto);
    public void circleScheduleApplicationInfoInsert(CircleScheduleApplyDto circleScheduleApplyDto);
    public void circleScheduleAttenDanceInfoInfoInsert(CircleScheduleAttendanceDto circleScheduleAttendanceDto);

    // 동아리 일정 전체리스트(그냥전체)
    public List<CircleScheduleDto> circleScheduleListAllByCircleMemberId(int circle_member_id);
    
    // 투표했는지 안했는지 boolean타입 체크 & 일정등록하는사람이 관리자 + 매니저인지 확인 & 일정신청을 했는지 안했는지 확인
    public Boolean voteChecked(@Param("vote_option_id") int vote_option_id, @Param("circle_member_id") int circle_member_id);
    public Boolean scheduleApplyCheckPAndAByCircleMemberId(int circle_member_id);
    public Boolean scheduleApplyCheckByCircleScheduleIdAndCircleMemberId(@Param("circle_member_id") int circle_member_id, @Param("circle_schedule_id") int circle_schedule_id);
    
    // 
    public int circleBoartCnt(int circle_id);
    
    // 동아리 가입신청 N인애들 끌어오기 & 가입신청 Y로바꿔주기
    public List<CircleJoinApplyDto> ApprovalJoinAllListByCircleIdAndSubmitN(int circle_id);
    public void circleJoinApplyCompleteUpdateByCircleJoinApplyId(int circle_join_apply_id); 

    // 검증1 => 가입신청을 했는가?
    public Boolean verificationjoinApplyByUserIdAndCircleId(@Param("circle_id") int circle_id, @Param("user_id") int user_id);
    
    // 동아리 게시글 이미지 리스트 가져오기
    public List<CircleBoardImageDto> circleBoardImageInfoByCircleBoardId(int circle_board_id);

    // circleScheduleIdMax
    public int circleScheduleIdMaxValue();

    // 카카오페이 상품테이블 insert
    public void itemInfoInsert(int circle_schedule_id);

    // 카카오결제요청 테이블 insert && 결제응답테이블 insert && 주문번호테이블 insert
    public void kakaoPaymentReqInsert(KakaoPaymentReqDto kakaoPaymentReqDto);
    public void kakaoPaymentResInsert(KakaoPaymentResDto kakaoPaymentResDto);
    public void orderInfoInsert(OrderInfoDto orderInfoDto);

    //itemPk값 가져오기 & userPk가져오기(스케줄등록한사람)
    public int itemPkGetByCircleScheduleId(int circle_schedule_id);
    public int userPkByCircleScheduleId(int circle_schedule_id);

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
    
    // mainPage #눌렀을때 리스트 리스팅
    public List<CircleDto> circleListByCircleSmallcategory(int circle_small_category_id);

    // 동아리 리스트 인기순 가져올 항목들 ==> 1.동아리 맴버수 높은순으로 리스트 가져오기
    public List<CircleMemberDto> circleMemberListMemberCntOrderByCircleId();

    // 동아리 리스트 등급별로 가져올 항목
    public List<CircleDto> circleInfoListOrderByGradeId();
    
    // 동아리 좋아요 && 확인 && 삭제
    public void circleLikeInfoInsert(CircleLikeDto circleLikeDto);
    public int circleLikeInfoCheck(CircleLikeDto circleLikeDto);
    public void circleLikeInfoDelete(CircleLikeDto circleLikeDto);

    // 동아리 좋아요 리스트 가져오기
    public List<CircleLikeDto> circleLikeInfoByUserId(int user_id);

    // 동아리 일정단일테이블 가져오기 && 일정 신청 테이블 개수가져오기 => 검증2
    public CircleScheduleDto circleScheduleByCircleScheduleId(int circle_schedule_id);
    public int circleScheduleApplicationByCircleScheduleId(int circle_schedule_id);

    // 동아리 내가 일정신청한 리스트를 가져옴 && 스케쥴id로 상품테이블 가져옴 && item테이블정보 상품pk로
    public List<CircleScheduleApplyDto> circleScheduleApplyByUserId(int user_id);
    public ItemInfoDto itemInfoByCircleScheduleId(int circle_schedule_id);
    public OrderInfoDto orderInfoByItemId(int item_id);

    // 카카오페이 결제요청 내역 상품pk로 가져오기 && 결제응답도 상품pk로 가져옴
    public KakaoPaymentReqDto kakaoPaymentReqInfoByItemId(String order_id);
    public KakaoPaymentAcceptResDto kakaoPaymentAccResInfoByItemId(String order_id);

    // 스케쥴 정보 가져오기
    public CircleScheduleDto circleScheduleInfoByCircleScheduleid(int circle_schedule_id);

    // 일정신청한 인원
    public int circleScheduleApplyPeopleCount(int circle_schedule_id);

    // 카카오페이취소요청 insert && 취소응답 insert
    public void kakakPaymentCancleReqInsert(KakaoPaymentCancelReqDto kakaoPaymentCancelReqDto);
    public void kakakPaymentCancleResInsert(KakaoPaymentCancelResDto kakaoPaymentCancelResDto);

    // 결제취소후 상태변경 && 일정신청 테이블에서 삭제 결제취소된 내용들
    public void paymentCancelStatusChangeOrderInfoStatus(String order_id);
    public void scheduleApplicationTableDelete(int circle_schedule_application_id);
    
    // 메인쪽에서 최근에 올라온 투표글 3개 리스트출력 && 동아리id 찾아오기 투표글id로
    public List<CircleVoteDto> voteThreeNewList();
    public int circleIdGetByCircleVoteId(int circle_vote_id);

    // 출석부에쓸 일정신청테이블 max값 가져오기 && 일정신청개수 가져오기
    public int circleScheduleApplicationMaxIdByCircleMemId(int circle_member_id);
    public int circleScheduleApplicationPeopleCntByCircleScheduleId(int circle_schedule_id);

    // 일정신청리스트 뽑아오기 && 무조건 하나밖에나올수 밖에없는 scheduleAttendanceInfo
    public List<CircleScheduleApplyDto> circleScheduleApplicationListOrderByDesc(int circle_schedule_id);
    public CircleScheduleAttendanceDto circleScheduleAttendanceInfoByCircleScheduleApplicationId(int circle_schedule_application_id);
    
    // 일정출석부 출석체크
    public void circleAttendanceChangeY(int circle_schedule_application_id);
    public void circleAttendanceChangeN(int circle_schedule_application_id);

    // 맴버정보 by memberid
    public CircleMemberDto circleMemberInfoByCircleMemberId(int circle_member_id);

    // 동아리 게시글 좋아요 insert // 좋아요취소 // 좋아요 체크
    public void circleBoardLikeInsert(CircleBoardLikeDto circleBoardLikeDto);
    public void circleBoardLikeDelete(CircleBoardLikeDto circleBoardLikeDto);
    public Boolean circleBoardLikeCheck(CircleBoardLikeDto circleBoardLikeDto);

    // 그냥 투표수 토탈가져오게함 && 두번째껀 한항목당 투표수가져오기
    public int circleVoteTotalCount(int circle_vote_id);
    public int circleVoteCompleteCount(int vote_option_id);

    // 동아리 가입신청 체크
    public int circlejoinApplyPossibleCheck(@Param("circle_id") int circle_id, @Param("user_id") int user_id);

    // 내가쓴 board개수
    public int boardCntByUserId(int user_id);
    public int voteCntByUserId(int user_id);
    public int scheduleCntByUserId(int user_id);

    // 검색한 동아리 리스트
    public List<CircleDto> circleSearchList(String searchword);
    public List<CircleDto> circleNameDuplicateCheck(String circle_name);
}
