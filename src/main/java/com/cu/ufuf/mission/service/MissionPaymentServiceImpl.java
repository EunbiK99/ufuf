package com.cu.ufuf.mission.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

@Service
public class MissionPaymentServiceImpl {

    @Autowired
    private MerchanSqlMapper merchanSqlMapper;
    @Autowired
    private MissionMapSqlMapper missionMapSqlMapper;

    public KakaoPaymentReqDto kakaoPayReq(OrderInfoDto orderInfoDto,int item_id, int totalReward){

        KakaoPaymentReqDto kakaoPaymentReqDto = new KakaoPaymentReqDto();

        kakaoPaymentReqDto.setCid("TC0ONETIME");
        kakaoPaymentReqDto.setPartner_user_id(orderInfoDto.getUser_id() + "");
        kakaoPaymentReqDto.setPartner_order_id(orderInfoDto.getOrder_id());
        kakaoPaymentReqDto.setItem_code(item_id + "");
        kakaoPaymentReqDto.setItem_name("ufuf미션");
        kakaoPaymentReqDto.setQuantity(1);
        kakaoPaymentReqDto.setTotal_amount(totalReward);
        kakaoPaymentReqDto.setTax_free_amount(totalReward);

        merchanSqlMapper.insertKakaoPayReqInfoWithCid(kakaoPaymentReqDto);

        return kakaoPaymentReqDto;
    }

    public void insertKakaoPayResInfo(KakaoPaymentResDto kakaoPaymentResDto){
        merchanSqlMapper.insertKakaoPayResInfo(kakaoPaymentResDto);
    }

    public void insertKakaoPayAccReqInfo(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        merchanSqlMapper.insertKakaoPayAccReqInfo(kakaoPaymentAcceptReqDto);
    }

    public void insertKakaoPayAccResInfo(GetKakaoPaymentAcceptResDto params){

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionByOrderId(params.getPartner_order_id());
        missionMapSqlMapper.updateStatus(missionInfoDto.getMission_id(), "모집중");

        KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto = new KakaoPaymentAcceptResDto();

        AmountDto amountDto = params.getAmount();
        merchanSqlMapper.insertAmountInfo(amountDto);
        int amount_id = amountDto.getAmount_id();

        if(params.getCard_info() != null){
            CardInfoDto cardDto = params.getCard_info();
            merchanSqlMapper.insertCardInfo(cardDto);
            int card_id = cardDto.getCard_id();
            kakaoPaymentAcceptResDto.setCard_info(card_id);
        }else{
            kakaoPaymentAcceptResDto.setCard_info(0);
        }

        kakaoPaymentAcceptResDto.setTid(params.getTid());
        kakaoPaymentAcceptResDto.setAid(params.getAid());
        kakaoPaymentAcceptResDto.setAmount(amount_id);
        kakaoPaymentAcceptResDto.setPartner_order_id(params.getPartner_order_id());
        kakaoPaymentAcceptResDto.setPartner_user_id(params.getPartner_user_id());
        kakaoPaymentAcceptResDto.setPayment_method_type(params.getPayment_method_type());
        kakaoPaymentAcceptResDto.setApproved_at(params.getApproved_at());

        merchanSqlMapper.insertKakaoPayAccResInfo(kakaoPaymentAcceptResDto);
    }

    public List<KakaoPaymentCancelReqDto> cancelKakaoPayment(){

        List<KakaoPaymentCancelReqDto> kakaoPaymentCancelReqDtoList = new ArrayList<>();

        List<OrderInfoDto> orderInfoDtoList = missionMapSqlMapper.selectOrderInfoNotYetCanceled();

        if(orderInfoDtoList != null){

            for(OrderInfoDto orderInfoDto : orderInfoDtoList){

                String order_id = orderInfoDto.getOrder_id();

                MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionByOrderId(order_id);
                int mission_id = missionInfoDto.getMission_id();

                List<MissionCourseDto> missionCourseDtoList = missionMapSqlMapper.selectCourseByMission(mission_id);

                int totalReward = 0;
                for(MissionCourseDto missionCourseDto : missionCourseDtoList){
                    int courseReward = missionCourseDto.getReward();
                    totalReward = totalReward + courseReward;
                }

                KakaoPaymentAcceptResDto kakaoAccResDto = missionMapSqlMapper.selectKakaoPayAccResInfoByOrderId(order_id);
    
                KakaoPaymentCancelReqDto kakaoPaymentCancelReqDto = new KakaoPaymentCancelReqDto();
                kakaoPaymentCancelReqDto.setCid("TC0ONETIME");
                kakaoPaymentCancelReqDto.setTid(kakaoAccResDto.getTid());
                kakaoPaymentCancelReqDto.setCancel_amount(totalReward);
                kakaoPaymentCancelReqDto.setCancel_tax_free_amount(totalReward);

                kakaoPaymentCancelReqDtoList.add(kakaoPaymentCancelReqDto);
                merchanSqlMapper.insertKakaoPayCancelReqInfo(kakaoPaymentCancelReqDto);
            }

            return kakaoPaymentCancelReqDtoList;

        }else{
            return kakaoPaymentCancelReqDtoList;
        }
    }

    public void insertKakaoPayCancelInfo(KakaoPaymentCancelResDto kakaoPaymentCancelResDto){

        merchanSqlMapper.insertKakaoPayCancelResInfo(kakaoPaymentCancelResDto);

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setOrder_id(kakaoPaymentCancelResDto.getPartner_order_id());
        orderInfoDto.setStatus("결제취소");

        merchanSqlMapper.updateOrderStatus(orderInfoDto);
    }

















}
