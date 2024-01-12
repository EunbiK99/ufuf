package com.cu.ufuf.merchan.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.OrderInfoDto;

@Mapper
public interface MerchanSqlMapper {
    
    public void insertItemInfo(String item_id);

    public void insertOrderInfo(OrderInfoDto orderInfoDto);

}
