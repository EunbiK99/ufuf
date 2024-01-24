package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class KakaoLoginResDto {
    private int user_id;
    private String userid;
    private String password;
    private String name;
    private String profile_img;
}
