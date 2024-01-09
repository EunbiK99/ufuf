package com.cu.ufuf.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CircleDto {
    private int circle_id;
    private int circle_small_category_id;
    private int circle_grade_id;
    private int user_id;
    private String circle_name;
    private String circle_content;
    private String circle_image;
    private Date application_date;
}
