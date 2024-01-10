package com.cu.ufuf.meeting.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cu.ufuf.meeting.service.MeetingServiceImpl;

@RestController
public class RestMeetingController {

    @Autowired
    private MeetingServiceImpl meetingService;

}
