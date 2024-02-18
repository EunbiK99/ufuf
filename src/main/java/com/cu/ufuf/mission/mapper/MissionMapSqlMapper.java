package com.cu.ufuf.mission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionProcessDto;
import com.cu.ufuf.dto.MissionReviewDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.dto.UserPointDto;


@Mapper
public interface MissionMapSqlMapper {

    // 인서트 
    public void insertMission(MissionInfoDto missionInfoDto);
    public void insertMissionCourse(MissionCourseDto missionCourseDto);

    // 미션 아이디로 미션 셀렉트
    public MissionInfoDto selectMissionById(int mission_id);

    // 주문 아이디로 미션 셀렉트
    public MissionInfoDto selectMissionByOrderId(String order_id);

    // 미션 리스트 출력
    public List<MissionInfoDto> selectAllMission();

    // 미션 코스 리스트
    public List<MissionCourseDto> selectCourseByMission(int mission_id);

    // 미션 상태 업데이트
    public void updateStatus(@RequestParam(name="param1")int mission_id, @RequestParam(name="param2")String Status);

    // 유저가 이미 이 미션에 등록했는지 확인
    public int isUserApplied(MissionChatRoomDto missionChatRoomDto);

    // 내가 등록한 미션 리스트
    public List<MissionInfoDto> selectMyResMission(int user_id);


    // 유저 셀렉트
    public UserInfoDto selectUserById(int user_id);

    // 미션 유저 수락하기
    public void updateAccStatus(int chat_room_id);

    // 미션 코스 완료
    public void insertMissionProcess(MissionProcessDto missionProcessDto);
    // 코스 완료 카운트
    public int countCompleteCourse(int chat_room_id);
    // 코스 달성률 퍼센테이지
    public int countProgressPercent(int mission_id);
    // 코스 달성 리스트
    public MissionProcessDto selectMissionProcessByChatRoomId(int mission_course_id);
    // 챗룸아이디로 코스 전체 갯수
    public int countTotalCourseByChatRoomId(int chat_room_id);

    // 내가 수행하는 미션(내가 신청한 미션룸)
    public List<MissionChatRoomDto> selectMyPlayMission(int user_id);

    // 미션 포기
    public void updateGiveup(int chat_room_id);

    // 리뷰 작성
    public void insertReview(MissionReviewDto missionReviewDto);
    // 리뷰 작성 했는지 안했는지 확인
    public int isReviewExist(int chat_room_id);

    // 미션 성공시 포인트 입력
    public void insertPoint(UserPointDto userPointDto);
    // 미션 결과에 따른 주문상태 업데이트
    public String selectOrderIdByMissionId(int mission_id);
    // 시간초과로 미션 종료된 로직 가져오기
    public List<MissionInfoDto> selectFailMissionForScheduleMethod();
    // 환불되지 않은 미션실패 주문정보 가져오기
    public List<OrderInfoDto> selectOrderInfoNotYetCanceled();
    // 주문아이디로 카카오페이 취소위한 정보 가져오기
    public KakaoPaymentAcceptResDto selectKakaoPayAccResInfoByOrderId(String order_id);


    // 내 미션들 카운트
    public int countMyRegMission(int user_id);
    public int countMyCompleteMission(int user_id);

    // 내 포인트
    public int countTotalPoint(int user_id);

    // 내가 작성한 리뷰 
    public List<MissionReviewDto> selectReviewByUserId(int user_id);

    // 미션 완료 후 3일 지났는데 리뷰 없는거
    public List<MissionInfoDto> selectMissionCompleteAfter3days();

    // 미션완료 후 리뷰 열람
    public MissionReviewDto selectReviewByMission(int chat_room_id);

    // 미션 아이디로 수락된 미션챗룸 가져오기
    public MissionChatRoomDto selectAcceptedChatRoom(int mission_id);

    // 미션 삭제
    public void deleteMission(int mission_id);

}
