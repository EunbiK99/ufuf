package com.cu.ufuf.dto;

import java.util.List;

import lombok.Data;

@Data
public class MissionRegRequestDto {
    private MissionInfoDto missionInfoDto;
    private List<MissionCourseDto> missionCourseDto;
}
