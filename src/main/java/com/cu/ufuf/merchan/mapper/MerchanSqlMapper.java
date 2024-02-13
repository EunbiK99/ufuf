package com.cu.ufuf.merchan.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;

@Mapper
public interface MerchanSqlMapper {

    public void insertItemCategory(String category_name);
    
    public int createItemPk();
    public void insertItemInfo(ItemInfoDto iteminfoDto);

    public void insertOrderInfo(OrderInfoDto orderInfoDto);
    public void updateOrderStatus(OrderInfoDto orderInfoDto);

    public void insertKakaoPayReqInfo(KakaoPaymentReqDto kakaoPaymentReqDto);
    public void insertKakaoPayResInfo(KakaoPaymentResDto kakaoPaymentResDto);
    public void insertKakaoPayAccReqInfo(KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto);
    public void insertKakaoPayAccResInfo(KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto);
    public void insertKakaoPayCancelReqInfo(KakaoPaymentCancelReqDto kakaoPaymentCancelReqDto);
    public void insertKakaoPayCancelResInfo(KakaoPaymentCancelResDto kakaoPaymentCancelResDto);
    
    public void insertKakaoPayReqInfoWithCid(KakaoPaymentReqDto kakaoPaymentReqDto);

    public int createAmountPk();
    public void insertAmountInfo(AmountDto amountDto);
    public int createCardInfoPk();
    public void insertCardInfo(CardInfoDto cardInfoDto);

    

}
