package com.cu.ufuf.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CircleDto {
    private int circle_id;
    private int circle_small_category_id;
    private int circle_grade_id;
    private int user_id;
    private String circle_name;
    private String circle_university;
    private String circle_content;
    private String circle_image;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime application_date;
}
