package com.cu.ufuf.room.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionDto;

@Mapper
public interface RoomSqlMapper {

    public int creatRoomInfoPk();
    public void roomInsert(RoomInfoDto roomInfoDto);
    public void insertRoomOption(RoomOptionDto roomOptionDto);
    public void insertRoomDetailImage(RoomImageDto roomImageDto);


}
