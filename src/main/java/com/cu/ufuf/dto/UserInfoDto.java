package com.cu.ufuf.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserInfoDto {
    private String user_id;
	private String name;
	private String userid;
	private String password;
	private String university;
	private String department;
	private String address;
	private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth;
	private String gender;
	private String email;
	private String profile_img;
}
