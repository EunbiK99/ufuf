package com.cu.ufuf.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.MeetingFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupDto;
import com.cu.ufuf.dto.MeetingGroupFirstLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingGroupTagDto;
import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;
import com.cu.ufuf.dto.MeetingSecondLocationCategoryDto;
import com.cu.ufuf.dto.MeetingTagDto;

@Mapper
public interface MeetingSqlMapper {

    // * PK 생성용
    public int countProfilePk();
    public int countGroupPk();
    public int countFirstLocationCategoryForPk();
    public int countSecondLocationCategoryForPk();
    public int countTagForPk();

    
    public int countSNSUrlByInputUrlValue(String value);

    public void insertMeetingProfile(MeetingProfileDto meetingProfileDto);

    public void insertNewSNS(MeetingSNSDto meetingSNSDto);

    public int countMeetingProfileByUserId(int user_id);

    public MeetingProfileDto selectMeetingProfileByUserId(int user_id);

    public void insertNewGroup(MeetingGroupDto meetingGroupDto);

    public void insertFirstLocationCategory(MeetingFirstLocationCategoryDto meetingFirstLocationCategoryDto);

    public void insertSecondLocationCategory(MeetingSecondLocationCategoryDto meetingSecondLocationCategoryDto);

    public void insertGroupFirstLocationCategory(MeetingGroupFirstLocationCategoryDto meetingGroupFirstLocationCategoryDto);

    public void insertGroupSecondLocationCategory(MeetingGroupSecondLocationCategoryDto meetingGroupSecondLocationCategoryDto);

    public void insertTag(MeetingTagDto meetingTagDto);

    public void insertGroupTag(MeetingGroupTagDto meetingGroupTagDto);

    public List<MeetingGroupDto> selectGroupListAll();

}
