package com.cu.ufuf.room.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cu.ufuf.dto.RoomImageDto;
import com.cu.ufuf.dto.RoomInfoDto;
import com.cu.ufuf.dto.RoomOptionCategoryDto;
import com.cu.ufuf.dto.RoomOptionDto;
import com.cu.ufuf.room.mapper.RoomSqlMapper;

@Service
public class RoomServiceIpml {

    @Autowired
    private RoomSqlMapper roomSqlMapper;

    public void roomRegister(RoomInfoDto roomInfoDto,int[] roomOptionCategoryIdList,List<RoomImageDto> roomImageDtoList){
       
        int roomPk =roomSqlMapper.creatRoomInfoPk();
                
        roomInfoDto.setRoom_info_id(roomPk);

        //사진
        for(RoomImageDto roomImageDto : roomImageDtoList) {
            roomImageDto.setRoom_info_id(roomPk);
            roomSqlMapper.insertRoomDetailImage(roomImageDto);
        }

        //옵션
        if(roomOptionCategoryIdList != null) {
            for(int roomOptionCategoryId : roomOptionCategoryIdList) {
                RoomOptionDto roomOptionDto=new RoomOptionDto();
                roomOptionDto.setRoom_option_category_id(roomOptionCategoryId); 
                roomOptionDto.setRoom_info_id(roomPk);
                
                roomSqlMapper.insertRoomOption(roomOptionDto);
            }
        }
    

        
        roomSqlMapper.roomInsert(roomInfoDto);
    }

    public List<RoomOptionCategoryDto> getRoomOptionlist(){
        return roomSqlMapper.selectRoomOptionCategoryAll();
    }
}
