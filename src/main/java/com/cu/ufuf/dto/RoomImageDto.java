package com.cu.ufuf.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomImageDto {

    private int room_image_id;
    private int room_info_id;
    private String location;
    private String  original_filename;
    private LocalDateTime created_at;
    
}
