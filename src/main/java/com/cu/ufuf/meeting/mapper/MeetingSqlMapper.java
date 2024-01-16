package com.cu.ufuf.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cu.ufuf.dto.MeetingProfileDto;
import com.cu.ufuf.dto.MeetingSNSDto;

@Mapper
public interface MeetingSqlMapper {

    public int countProfilePk();

    public int countSNSUrlByInputUrlValue(String value);

    public void insertMeetingProfile(MeetingProfileDto meetingProfileDto);

    public void insertNewSNS(MeetingSNSDto meetingSNSDto);

    public int countMeetingProfileByUserId(int user_id);

    public MeetingProfileDto selectMeetingProfileByUserId(int user_id);

}
