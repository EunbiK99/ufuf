package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapsqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;

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

    public Map<String, Object> getItemAndOrderInfo(int mission_id){

        Map<String, Object> ItemOrderInfo = new HashMap<>();

        OrderInfoDto orderInfoDto = missionMapsqlMapper.getOrderInfoByMissionId(mission_id);
        int item_id = orderInfoDto.getItem_id();

        ItemOrderInfo.put("orderInfoDto", orderInfoDto);
        ItemOrderInfo.put("itemInfoDto", missionMapsqlMapper.getItemInfo(item_id));

        return ItemOrderInfo;
    }

    public OrderInfoDto getOrderInfo(String Order_id){
        return missionMapsqlMapper.getOrderInfo(Order_id);
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

    public void insertKakaoPayAccResInfo(KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto){
        merchanSqlMapper.insertKakaoPayAccResInfo(kakaoPaymentAcceptResDto);
    }

    public void insertAmountInfo(AmountDto amountInfo){
        merchanSqlMapper.insertAmountInfo(amountInfo);
    }

    public void insertCardInfo(CardInfoDto cardInfoDto){
        merchanSqlMapper.insertCardInfo(cardInfoDto);
    }

    


}
