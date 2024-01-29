package com.cu.ufuf.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MissionCourseDto {
    private int mission_course_id;
	private int mission_id;
	private String content;
	private int reward;
	private BigDecimal lat;
	private BigDecimal lng;
}
