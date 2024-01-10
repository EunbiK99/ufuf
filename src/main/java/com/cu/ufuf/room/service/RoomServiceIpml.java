package com.cu.ufuf.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.room.mapper.RoomSqlMapper;

@Service
public class RoomServiceIpml {

    @Autowired
    private RoomSqlMapper roomSqlMapper;

    public void roomRegister(RoomInfoDto roomInfoDto){
       
        
        roomSqlMapper.roomInsert(roomInfoDto);
    }
}
