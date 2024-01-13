package com.cu.ufuf.merchan.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;

@Mapper
public interface MerchanSqlMapper {

    public void insertItemCategory(String category_name);
    
    public int createItemPk();
    public void insertItemInfo(ItemInfoDto iteminfoDto);

    public void insertOrderInfo(OrderInfoDto orderInfoDto);

    public void insertKakaoPayReqInfo(KakaoPaymentReqDto kakaoPaymentReqDto);
    public void insertKakaoPayResInfo(KakaoPaymentResDto kakaoPaymentResDto);

}
