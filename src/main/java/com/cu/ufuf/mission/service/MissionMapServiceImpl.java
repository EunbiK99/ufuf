package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionRegRequestDto;

import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Service
@Transactional
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapSqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;
    @Autowired
    private MissionPaymentServiceImpl missionPaymentService;

    // 미션 등록
    public KakaoPaymentReqDto registerMissionProcess(MissionRegRequestDto missionRegRequestDto){

        missionMapSqlMapper.insertMission(missionRegRequestDto.getMissionInfoDto());
        MissionInfoDto missionInfoDto = missionRegRequestDto.getMissionInfoDto();
        int missionId = missionInfoDto.getMission_id();
        int userId = missionInfoDto.getUser_id();

        int totalReward = 0;

        List<MissionCourseDto> courseList = missionRegRequestDto.getMissionCourseDto();
        
        for(MissionCourseDto missionCourseDto : courseList){
            missionCourseDto.setMission_id(missionId);
            missionMapSqlMapper.insertMissionCourse(missionCourseDto);
            int reward = missionCourseDto.getReward();
            totalReward = totalReward + reward;
        }

        ItemInfoDto itemInfoDto = new ItemInfoDto();
        itemInfoDto.setItem_category_id(1);
        itemInfoDto.setMerchan_id(missionId);

        merchanSqlMapper.insertItemInfo(itemInfoDto);
        int itemId = itemInfoDto.getItem_id();

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        String order_id = "MI";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");
        ramdomUUID = ramdomUUID.substring(0, 10);
        ramdomUUID = ramdomUUID.toUpperCase();

        order_id = order_id + itemId + today + ramdomUUID;

        orderInfoDto.setOrder_id(order_id);
        orderInfoDto.setItem_id(itemId);
        orderInfoDto.setUser_id(userId);
        orderInfoDto.setStatus("주문완료");

        merchanSqlMapper.insertOrderInfo(orderInfoDto);

        return missionPaymentService.kakaoPayReq(orderInfoDto, itemId, totalReward);

    }

    public MissionInfoDto selectMissionByOrderId(String order_id){
        return missionMapSqlMapper.selectMissionByOrderId(order_id);
    }

    // 미션 전체 리스트 출력
    public List<Map<String, Object>> loadMissionList(){
        
        List<Map<String, Object>> missionInfoList = new ArrayList<>();

        List<MissionInfoDto> missionDtoList = missionMapSqlMapper.selectAllMission();
        for(MissionInfoDto missionInfoDto : missionDtoList){
            int mission_id = missionInfoDto.getMission_id();
            int user_id = missionInfoDto.getUser_id();

            Map<String, Object> missionInfo = new HashMap();

            missionInfo.put("missionInfoDto", missionInfoDto);
            missionInfo.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));
            missionInfo.put("userInfoDto", missionMapSqlMapper.selectUserById(user_id));

            missionInfoList.add(missionInfo);
        }

        return missionInfoList;
    }

    // 미션 상세 출력
    public Map<String, Object> getMissionDetail(int mission_id, int user_id){

        Map<String, Object> missionDetail = new HashMap<>();

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);

        int missionUser_id = missionInfoDto.getUser_id();

        MissionChatRoomDto missionChatRoomDto = new MissionChatRoomDto();
        missionChatRoomDto.setMission_id(mission_id);
        missionChatRoomDto.setUser_id(user_id);

        missionDetail.put("missionInfoDto", missionInfoDto);
        missionDetail.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));
        missionDetail.put("userInfoDto", missionMapSqlMapper.selectUserById(missionUser_id));
        missionDetail.put("isUserApplied", missionMapSqlMapper.isUserApplied(missionChatRoomDto));

        return missionDetail;
    }

    

















    // // 주문상태 업데이트
    // public void updateOrderStatus(OrderInfoDto orderInfoDto){
    //     merchanSqlMapper.updateOrderStatus(orderInfoDto);
    // }

    // // 아이템/주문 정보 가져오기
    // public Map<String, Object> getItemAndOrderInfo(int mission_id){

    //     Map<String, Object> ItemOrderInfo = new HashMap<>();

    //     OrderInfoDto orderInfoDto = missionMapsqlMapper.getOrderInfoByMissionId(mission_id);
    //     int item_id = orderInfoDto.getItem_id();

    //     ItemOrderInfo.put("orderInfoDto", orderInfoDto);
    //     ItemOrderInfo.put("itemInfoDto", missionMapsqlMapper.getItemInfo(item_id));

    //     return ItemOrderInfo;
    // }

    // // 주문 정보 가져오기
    // public OrderInfoDto getOrderInfo(String Order_id){
    //     return missionMapsqlMapper.getOrderInfo(Order_id);
    // }

    // // 미션 수락하기
    // public void acceptingMission(MissionAcceptedDto missionAcceptedDto){

    //     int mission_accepted_id = missionMapsqlMapper.createMissionAccPk();
    //     missionAcceptedDto.setMission_accepted_id(mission_accepted_id);

    //     int mission_id = missionAcceptedDto.getMission_id();
    //     MissionInfoDto missionDto = missionMapsqlMapper.selectMissionById(mission_id);

    //     int accUser_id = missionAcceptedDto.getUser_id();
    //     UserInfoDto userInfoDto = missionMapsqlMapper.selectUserById(accUser_id);

    //     missionMapsqlMapper.insertMissionAcc(missionAcceptedDto);
    //     missionMapsqlMapper.updateStatus(mission_id, "진행중");

    //     String content = """
    //                     %s님이 "%s" 미션을 수락하셨습니다.
    //                     """.formatted(userInfoDto.getName(), missionDto.getTitle());

    //     insertNotification(1, missionDto.getUser_id(), mission_id, content);

    // }

    // // 내가 수락한 미션
    // public List<Map<String, Object>> getMyAccMission(int user_id){
        
    //     List<Map<String, Object>> accMissionList = new ArrayList<>();

    //     List<MissionAcceptedDto> missionAcceptedList = missionMapsqlMapper.selectMyAccMission(user_id);

    //     for(MissionAcceptedDto missionAccDto : missionAcceptedList){

    //         int mission_id = missionAccDto.getMission_id();

    //         MissionInfoDto missionInfoDto = missionMapsqlMapper.selectMissionById(mission_id);
    //         int userId = missionInfoDto.getUser_id();

    //         Map<String, Object> accMissionInfo = new HashMap<>();

    //         accMissionInfo.put("missionAccDto", missionAccDto);
    //         accMissionInfo.put("missionInfoDto", missionInfoDto);
    //         accMissionInfo.put("userDto", missionMapsqlMapper.selectUserById(userId));

    //         accMissionList.add(accMissionInfo);
    //     }

    //     return accMissionList;
    // }

    // // 미션 완료 인증 인서트
    // public void insertMissionComplete(MissionCompleteDto missionCompleteDto){

    //     int mission_complete_id = missionMapsqlMapper.createMissionComplPk();
    //     missionCompleteDto.setMission_complete_id(mission_complete_id);

    //     int mission_accepted_id = missionCompleteDto.getMission_accepted_id();
    //     int mission_id = missionMapsqlMapper.getMissionIdByMissionAccId(mission_accepted_id);

    //     MissionInfoDto missionDto = missionMapsqlMapper.selectMissionById(mission_id);
    //     MissionAcceptedDto missionAcceptedDto = missionMapsqlMapper.selectAccMissionByAccId(mission_accepted_id);
        
    //     int accUser_id = missionAcceptedDto.getUser_id();
    //     UserInfoDto userInfoDto = missionMapsqlMapper.selectUserById(accUser_id);

    //     missionMapsqlMapper.insertMissionComplete(missionCompleteDto);
    //     missionMapsqlMapper.updateStatus(mission_id, "인증완료");

    //     String content = """
    //                     %s님이 "%s" 미션 완료 인증을 등록하셨습니다.
    //                     """.formatted(userInfoDto.getName(), missionDto.getTitle());

    //     insertNotification(2, missionDto.getUser_id(), mission_id, content);
    // }

    // // 유저가 등록한 미션 전부 가져오기
    // public List<Map<String, Object>> getMyResMission(int user_id){
        
    //     List<Map<String, Object>> myResMission = new ArrayList<>();

    //     List<MissionInfoDto> missionInfoList = missionMapsqlMapper.selectMissionListByUserId(user_id);

    //     for(MissionInfoDto missionInfoDto : missionInfoList){

    //         Map<String, Object> missionDetail = new HashMap<>();

    //         missionDetail.put("missionInfoDto", missionInfoDto);
    //         missionDetail.put("userDto", missionMapsqlMapper.selectUserById(user_id));

    //         myResMission.add(missionDetail);
    //     }

    //     return myResMission;
    // }

    // // 유저 미션 알림리스트
    // public List<MissionNotificationDto> getNotificationList(int user_id){
    //     return missionMapsqlMapper.selectNotifcationByUser(user_id);
    // }

    // public boolean isExistNotification(int user_id){

    //     if(0 < missionMapsqlMapper.isExistNotification(user_id)){
    //         return true;
    //     }else{
    //         return false;
    //     }
    // }

    // // 알림 읽음 업데이트
    // public void updateNotifReadStatus(int mission_notification_id){
    //     missionMapsqlMapper.updateNotifReadStatus(mission_notification_id);
    // }

    // // 미션 아이디로 미션 진행 정보들 불러오기
    // public Map<String, Object> getMissionProcessInfo(int mission_id){

    //     Map<String, Object> missionProcessInfo = new HashMap<>();

    //     MissionInfoDto missionInfoDto = missionMapsqlMapper.selectMissionById(mission_id);
    //     MissionAcceptedDto missionAcceptedDto = missionMapsqlMapper.selectMissionAccInfoByMissionId(mission_id);

    //     int accUser = missionAcceptedDto.getUser_id();
    //     UserInfoDto accUserDto = missionMapsqlMapper.selectUserById(accUser);

    //     missionProcessInfo.put("missionInfoDto", missionInfoDto);
    //     missionProcessInfo.put("missionAcceptedDto", missionAcceptedDto);
    //     missionProcessInfo.put("accUserDto", accUserDto);
        
    //     return missionProcessInfo;
    // }










    

    

    // // 미션 알림
    // public void insertNotification(int mission_notification_category_id, int user_id, int mission_id, String content){

    //     MissionNotificationDto missionNotificationDto = new MissionNotificationDto();
        
    //     missionNotificationDto.setMission_notification_category_id(mission_notification_category_id);
    //     missionNotificationDto.setUser_id(user_id);
    //     missionNotificationDto.setMission_id(mission_id);
    //     missionNotificationDto.setContent(content);

    //     missionMapsqlMapper.insertNotification(missionNotificationDto);
    // }


}
