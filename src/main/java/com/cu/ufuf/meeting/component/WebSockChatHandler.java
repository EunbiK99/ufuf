package com.cu.ufuf.meeting.component;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.cu.ufuf.dto.MeetingChatMessageDto;
import com.cu.ufuf.dto.MeetingChatRoomDto;
import com.cu.ufuf.meeting.service.MeetingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MeetingServiceImpl meetingService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception{
        String payload = textMessage.getPayload();
        MeetingChatMessageDto meetingChatMessageDto = objectMapper.readValue(payload, MeetingChatMessageDto.class);
        MeetingChatRoomDto chatRoom = meetingService.getChatRoomDto(meetingChatMessageDto.getChatRoomId());
        Set<WebSocketSession> sessions = chatRoom.getSessions();

        if(meetingChatMessageDto.getMessageType().equals(MeetingChatMessageDto.MessageType.ENTER)){
            sessions.add(session);
            meetingChatMessageDto.setChatComment(meetingChatMessageDto.getChatRoomUserId() + " 님이 입장하셨다!");
            sendToEachSocket(sessions, new TextMessage(objectMapper.writeValueAsString(meetingChatMessageDto)));
        }
        else if(meetingChatMessageDto.getMessageType().equals(MeetingChatMessageDto.MessageType.QUIT)){
            sessions.remove(session);
            meetingChatMessageDto.setChatComment(meetingChatMessageDto.getChatRoomUserId() + " 님이 퇴장했다!");
            sendToEachSocket(sessions, new TextMessage(objectMapper.writeValueAsString(meetingChatMessageDto)));
        }
        else{
            sendToEachSocket(sessions, textMessage);
        }
    }

    private void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        sessions.parallelStream().forEach(roomSession -> {
            try{
                roomSession.sendMessage(message);
            }catch(IOException e){
                throw new RuntimeException(e);
            }            
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{

    }
}
