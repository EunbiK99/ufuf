package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionAcceptedDto;
import com.cu.ufuf.dto.MissionCompleteDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionNotificationDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapsqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;

    // 미션 등록
    public void registerMissionProcess(MissionInfoDto missionInfoDto){

        int mission_id = missionMapsqlMapper.createMissionPk();
        int user_id = missionInfoDto.getUser_id();
        missionInfoDto.setMission_id(mission_id);
 
        missionMapsqlMapper.insertMission(missionInfoDto);

        ItemInfoDto itemInfoDto = new ItemInfoDto();
        int item_id = merchanSqlMapper.createItemPk();

        itemInfoDto.setItem_id(item_id);
        itemInfoDto.setItem_category_id(1);
        itemInfoDto.setMerchan_id(mission_id);

        merchanSqlMapper.insertItemInfo(itemInfoDto);

        OrderInfoDto orderInfoDto = new OrderInfoDto();

        String order_id = "MI";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");
        ramdomUUID = ramdomUUID.substring(0, 10);
        ramdomUUID = ramdomUUID.toUpperCase();

        order_id = order_id + item_id + today + ramdomUUID;

        orderInfoDto.setOrder_id(order_id);
        orderInfoDto.setItem_id(item_id);
        orderInfoDto.setUser_id(user_id);
        orderInfoDto.setStatus("주문완료");

        merchanSqlMapper.insertOrderInfo(orderInfoDto);
    }

    // 미션 전체 리스트 출력
    public List<MissionInfoDto> loadMissionList(){
        return missionMapsqlMapper.selectAllMission();
    }

    // 미션 상세 출력
    public Map<String, Object> getMissionDetail(int mission_id){

        Map<String, Object> missionDetail = new HashMap<>();

        MissionInfoDto missionDto = missionMapsqlMapper.selectMissionById(mission_id);

        int user_id = missionDto.getUser_id();

        missionDetail.put("missionDto", missionDto);
        missionDetail.put("userDto", missionMapsqlMapper.selectUserById(user_id));

        return missionDetail;
    }

    // 주문상태 업데이트
    public void updateOrderStatus(OrderInfoDto orderInfoDto){
        merchanSqlMapper.updateOrderStatus(orderInfoDto);
    }

    // 아이템/주문 정보 가져오기
    public Map<String, Object> getItemAndOrderInfo(int mission_id){

        Map<String, Object> ItemOrderInfo = new HashMap<>();

        OrderInfoDto orderInfoDto = missionMapsqlMapper.getOrderInfoByMissionId(mission_id);
        int item_id = orderInfoDto.getItem_id();

        ItemOrderInfo.put("orderInfoDto", orderInfoDto);
        ItemOrderInfo.put("itemInfoDto", missionMapsqlMapper.getItemInfo(item_id));

        return ItemOrderInfo;
    }

    // 주문 정보 가져오기
    public OrderInfoDto getOrderInfo(String Order_id){
        return missionMapsqlMapper.getOrderInfo(Order_id);
    }

    // 미션 수락하기
    public void acceptingMission(MissionAcceptedDto missionAcceptedDto){

        int mission_accepted_id = missionMapsqlMapper.createMissionAccPk();
        missionAcceptedDto.setMission_accepted_id(mission_accepted_id);

        int mission_id = missionAcceptedDto.getMission_id();
        MissionInfoDto missionDto = missionMapsqlMapper.selectMissionById(mission_id);

        int accUser_id = missionAcceptedDto.getUser_id();
        UserInfoDto userInfoDto = missionMapsqlMapper.selectUserById(accUser_id);

        missionMapsqlMapper.insertMissionAcc(missionAcceptedDto);
        missionMapsqlMapper.updateStatus(mission_id, "진행중");

        String content = """
                        %s님이 "%s" 미션을 수락하셨습니다.
                        """.formatted(userInfoDto.getName(), missionDto.getTitle());

        insertNotification(1, missionDto.getUser_id(), mission_id, content);

    }

    // 내가 수락한 미션
    public List<Map<String, Object>> getMyAccMission(int user_id){
        
        List<Map<String, Object>> accMissionList = new ArrayList<>();

        List<MissionAcceptedDto> missionAcceptedList = missionMapsqlMapper.selectMyAccMission(user_id);

        for(MissionAcceptedDto missionAccDto : missionAcceptedList){

            int mission_id = missionAccDto.getMission_id();

            MissionInfoDto missionInfoDto = missionMapsqlMapper.selectMissionById(mission_id);
            int userId = missionInfoDto.getUser_id();

            Map<String, Object> accMissionInfo = new HashMap<>();

            accMissionInfo.put("missionAccDto", missionAccDto);
            accMissionInfo.put("missionInfoDto", missionInfoDto);
            accMissionInfo.put("userDto", missionMapsqlMapper.selectUserById(userId));

            accMissionList.add(accMissionInfo);
        }

        return accMissionList;
    }

    // 미션 완료 인증 인서트
    public void insertMissionComplete(MissionCompleteDto missionCompleteDto){

        int mission_complete_id = missionMapsqlMapper.createMissionComplPk();
        missionCompleteDto.setMission_complete_id(mission_complete_id);

        int mission_accepted_id = missionCompleteDto.getMission_accepted_id();
        int mission_id = missionMapsqlMapper.getMissionIdByMissionAccId(mission_accepted_id);

        MissionInfoDto missionDto = missionMapsqlMapper.selectMissionById(mission_id);
        MissionAcceptedDto missionAcceptedDto = missionMapsqlMapper.selectAccMissionByAccId(mission_accepted_id);
        
        int accUser_id = missionAcceptedDto.getUser_id();
        UserInfoDto userInfoDto = missionMapsqlMapper.selectUserById(accUser_id);

        missionMapsqlMapper.insertMissionComplete(missionCompleteDto);
        missionMapsqlMapper.updateStatus(mission_id, "인증완료");

        String content = """
                        %s님이 "%s" 미션 완료 인증을 등록하셨습니다.
                        """.formatted(userInfoDto.getName(), missionDto.getTitle());

        insertNotification(2, missionDto.getUser_id(), mission_id, content);
    }

    // 유저가 등록한 미션 전부 가져오기
    public List<Map<String, Object>> getMyResMission(int user_id){
        
        List<Map<String, Object>> myResMission = new ArrayList<>();

        List<MissionInfoDto> missionInfoList = missionMapsqlMapper.selectMissionListByUserId(user_id);

        for(MissionInfoDto missionInfoDto : missionInfoList){

            Map<String, Object> missionDetail = new HashMap<>();

            missionDetail.put("missionInfoDto", missionInfoDto);
            missionDetail.put("userDto", missionMapsqlMapper.selectUserById(user_id));

            myResMission.add(missionDetail);
        }

        return myResMission;
    }

    // 유저 미션 알림리스트
    public List<MissionNotificationDto> getNotificationList(int user_id){
        return missionMapsqlMapper.selectNotifcationByUser(user_id);
    }

    public boolean isExistNotification(int user_id){

        if(0 < missionMapsqlMapper.isExistNotification(user_id)){
            return true;
        }else{
            return false;
        }
    }

    // 알림 읽음 업데이트
    public void updateNotifReadStatus(int mission_notification_id){
        missionMapsqlMapper.updateNotifReadStatus(mission_notification_id);
    }

    // 미션 아이디로 미션 진행 정보들 불러오기
    public Map<String, Object> getMissionProcessInfo(int mission_id){

        Map<String, Object> missionProcessInfo = new HashMap<>();

        MissionInfoDto missionInfoDto = missionMapsqlMapper.selectMissionById(mission_id);
        MissionAcceptedDto missionAcceptedDto = missionMapsqlMapper.selectMissionAccInfoByMissionId(mission_id);

        int accUser = missionAcceptedDto.getUser_id();
        UserInfoDto accUserDto = missionMapsqlMapper.selectUserById(accUser);

        missionProcessInfo.put("missionInfoDto", missionInfoDto);
        missionProcessInfo.put("missionAcceptedDto", missionAcceptedDto);
        missionProcessInfo.put("accUserDto", accUserDto);
        
        return missionProcessInfo;
    }







    // 카카오페이
    public void insertKakaoPayReqInfo(KakaoPaymentReqDto kakaoPaymentReqDto){
        merchanSqlMapper.insertKakaoPayReqInfo(kakaoPaymentReqDto);
    }

    public void insertKakaoPayResInfo(KakaoPaymentResDto kakaoPaymentResDto){
        merchanSqlMapper.insertKakaoPayResInfo(kakaoPaymentResDto);
    }

    public void insertKakaoPayAccReqInfo(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        merchanSqlMapper.insertKakaoPayAccReqInfo(kakaoPaymentAcceptReqDto);
    }

    public void insertKakaoPayAccResInfo(GetKakaoPaymentAcceptResDto params){

        AmountDto amountDto = params.getAmount();
        int amount_id = merchanSqlMapper.createAmountPk();
        amountDto.setAmount_id(amount_id);

        CardInfoDto cardDto = params.getCard_info();
        int card_id = merchanSqlMapper.createCardInfoPk();
        cardDto.setCard_id(card_id);

        KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto = new KakaoPaymentAcceptResDto();

        kakaoPaymentAcceptResDto.setTid(params.getTid());
        kakaoPaymentAcceptResDto.setAid(params.getAid());
        kakaoPaymentAcceptResDto.setAmount(amount_id);
        kakaoPaymentAcceptResDto.setCard_info(card_id);
        kakaoPaymentAcceptResDto.setPartner_order_id(params.getPartner_order_id());
        kakaoPaymentAcceptResDto.setPartner_user_id(params.getPartner_user_id());
        kakaoPaymentAcceptResDto.setApproved_at(params.getApproved_at());

        merchanSqlMapper.insertKakaoPayAccResInfo(kakaoPaymentAcceptResDto);

    }

    // 미션 알림
    public void insertNotification(int mission_notification_category_id, int user_id, int mission_id, String content){

        MissionNotificationDto missionNotificationDto = new MissionNotificationDto();
        
        missionNotificationDto.setMission_notification_category_id(mission_notification_category_id);
        missionNotificationDto.setUser_id(user_id);
        missionNotificationDto.setMission_id(mission_id);
        missionNotificationDto.setContent(content);

        missionMapsqlMapper.insertNotification(missionNotificationDto);
    }


}
