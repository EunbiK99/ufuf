package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
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

        KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto = new KakaoPaymentAcceptResDto();

        AmountDto amountDto = params.getAmount();
        merchanSqlMapper.insertAmountInfo(amountDto);
        int amount_id = amountDto.getAmount_id();

        if(params.getCard_info() != null){
            CardInfoDto cardDto = params.getCard_info();
            merchanSqlMapper.insertCardInfo(cardDto);
            int card_id = cardDto.getCard_id();
            kakaoPaymentAcceptResDto.setCard_info(card_id);
        }

        kakaoPaymentAcceptResDto.setTid(params.getTid());
        kakaoPaymentAcceptResDto.setAid(params.getAid());
        kakaoPaymentAcceptResDto.setAmount(amount_id);
        kakaoPaymentAcceptResDto.setCard_info(0);
        kakaoPaymentAcceptResDto.setPartner_order_id(params.getPartner_order_id());
        kakaoPaymentAcceptResDto.setPartner_user_id(params.getPartner_user_id());
        kakaoPaymentAcceptResDto.setApproved_at(params.getApproved_at());

        merchanSqlMapper.insertKakaoPayAccResInfo(kakaoPaymentAcceptResDto);

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionByOrderId(params.getPartner_order_id());
        missionMapSqlMapper.updateStatus(missionInfoDto.getMission_id(), "모집중");

    }






}
