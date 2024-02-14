package com.cu.ufuf.mission.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cu.ufuf.dto.GetKakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionProcessDto;
import com.cu.ufuf.dto.MissionRegRequestDto;
import com.cu.ufuf.dto.MissionReviewDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.mission.component.ParseJson;
import com.cu.ufuf.mission.service.MissionChatServiceImpl;
import com.cu.ufuf.mission.service.MissionMapServiceImpl;
import com.cu.ufuf.mission.service.MissionPaymentServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/mission/*")
public class MissionMapRestController {

    @Autowired
    private MissionMapServiceImpl missionMapService;
    @Autowired
    private MissionPaymentServiceImpl missionPaymentService;
    @Autowired
    private MissionChatServiceImpl MissionChatService;
    @Autowired
    private ParseJson parseJson;

    @PostMapping("registerMissionProcess")
    public RestResponseDto registerMissionProcess(@RequestBody MissionRegRequestDto params){

        RestResponseDto restResponseDto = new RestResponseDto();

        KakaoPaymentReqDto paymentReqDto = missionMapService.registerMissionProcess(params);
        
        restResponseDto.setData(paymentReqDto);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("doesUserLogin")
    public RestResponseDto doesUserLogin(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");

        if(sessionUserInfo != null){
            restResponseDto.setData(sessionUserInfo.getUser_id());
        }else{
            restResponseDto.setData(0);
        }
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("insertKakaoPayResInfo")
    public RestResponseDto insertKakaoPayResInfo(@RequestBody KakaoPaymentResDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionPaymentService.insertKakaoPayResInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("saveKakaoPayAccReqInfoToSession")
    public RestResponseDto saveKakaoPayAccReqInfoToSession(@RequestBody KakaoPaymentAcceptReqDto params, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        session.setAttribute("kakaoPayAccReqInfo", params);

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("getKakaoPayAccReqInfoFromSession")
    public RestResponseDto getKakaoPayAccReqInfoFromSession(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto = (KakaoPaymentAcceptReqDto)session.getAttribute("kakaoPayAccReqInfo");

        restResponseDto.setData(kakaoPaymentAcceptReqDto);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("removeSession")
    public RestResponseDto removeSession(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        session.removeAttribute("kakaoPayAccReqInfo");

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("insertkakaoPayAccReq")
    public RestResponseDto insertkakaoPayAccReq(@RequestBody KakaoPaymentAcceptReqDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionPaymentService.insertKakaoPayAccReqInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("insertKakaoPayAccRes")
    public RestResponseDto insertKakaoPayAccRes(@RequestBody GetKakaoPaymentAcceptResDto params){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        missionPaymentService.insertKakaoPayAccResInfo(params);
        
        restResponseDto.setData(params);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("getOrderMissionInfo")
    public RestResponseDto getOrderMissionInfo(@RequestBody String order_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(order_id);
    
            String orderId = jsonNode.get("order_id").asText();
    
            restResponseDto.setData(missionMapService.selectMissionByOrderId(orderId));
            restResponseDto.setResult("Success");

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            restResponseDto.setResult("Error");
        } 
        
        return restResponseDto;
    }

    @GetMapping("loadMissionList")
    public RestResponseDto loadMissionList(){

        RestResponseDto restResponseDto = new RestResponseDto();
        
        restResponseDto.setData(missionMapService.loadMissionList());
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }


    @PostMapping("getMissionDetail")
    public RestResponseDto getMissionDetail(@RequestBody String mission_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (mission_id != null && !mission_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mission_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("mission_id")) {

                    UserInfoDto sessionUserInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
                    int missionId = jsonNode.get("mission_id").asInt();

                    if(sessionUserInfoDto != null){
                        restResponseDto.setData(missionMapService.getMissionDetail(missionId, sessionUserInfoDto.getUser_id()));
                    }else{
                        restResponseDto.setData(missionMapService.getMissionDetail(missionId, 0));
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("applyMission")
    public RestResponseDto applyMission(@RequestBody String mission_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        try {
            if (mission_id != null && !mission_id.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(mission_id);

                // mission_id 필드가 존재하는지 확인
                if (jsonNode.has("mission_id")) {
                    // mission_id 필드 추출 및 정수로 변환
                    int missionId = jsonNode.get("mission_id").asInt();

                    UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
                    int user_id = sessionUserInfo.getUser_id();

                    MissionChatRoomDto missionChatRoomDto = new MissionChatRoomDto();
                    missionChatRoomDto.setMission_id(missionId);
                    missionChatRoomDto.setUser_id(user_id);

                    MissionChatService.applyMission(missionChatRoomDto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("loadMyPlayMission")
    public RestResponseDto loadMyPlayMission(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getMyPlayMissionList(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("loadMyResMission")
    public RestResponseDto loadMyResMission(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getmyResMissionList(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("loadResMissionInfoInRecruiting")
    public RestResponseDto loadResMissionInfoInRecruiting(@RequestBody String mission_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        int missionId = parseJson.toInt("mission_id", mission_id);

        restResponseDto.setData(missionMapService.getMyResMissionInfoInRecruiting(missionId));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("loadMyResMissionInProgress")
    public RestResponseDto loadMyResMissionInProgress(@RequestBody String chat_room_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);
    
        restResponseDto.setData(missionMapService.loadMyMissionInProgress(chatRoomId));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("submitMissionComplete")
    public RestResponseDto submitMissionComplete(MultipartHttpServletRequest request,
                                        @RequestParam("complete_img") MultipartFile complete_img,
                                        @RequestParam("complete_comment") String complete_comment,
                                        @RequestParam("mission_course_id") int mission_course_id,
                                        @RequestParam("chat_room_id") int chat_room_id){

        RestResponseDto restResponseDto = new RestResponseDto();
        MissionProcessDto missionProcessDto = new MissionProcessDto();

        if(complete_img != null) {
				
			String rootPath = "C:/uploadFiles/ufuf/completeImg";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
			String todayPath = sdf.format(new Date());
			
			File todayFolderForCreate = new File(rootPath + todayPath);
				
			if(!todayFolderForCreate.exists()) {
				todayFolderForCreate.mkdirs();
			}
			
			String originalFileName = complete_img.getOriginalFilename();
			
			String uuid = UUID.randomUUID().toString();
			long currentTime = System.currentTimeMillis();
			String fileName = uuid + "_" + currentTime;
			
			String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
			fileName += ext;
			
			try {
				complete_img.transferTo(new File(rootPath + todayPath + fileName));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
            missionProcessDto.setComplete_img(todayPath + fileName);
		}

        missionProcessDto.setComplete_comment(complete_comment);
        missionProcessDto.setChat_room_id(chat_room_id);
        missionProcessDto.setMission_course_id(mission_course_id);

        missionMapService.insertMissionProcess(missionProcessDto);

        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("giveup")
    public RestResponseDto giveup(@RequestBody String chat_room_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);
    
        missionMapService.giveup(chatRoomId);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("loadMyMissionApplyerList")
    public RestResponseDto loadMyMissionApplyerList(@RequestBody String mission_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        int missionId = parseJson.toInt("mission_id", mission_id);
        
        restResponseDto.setData(missionMapService.getMyMissionApplyerList(missionId));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    // 미션 신청자 수락
    @PostMapping("accMissionApplyer")
    public RestResponseDto accMissionApplyer(@RequestBody String chat_room_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);

        missionMapService.accMissionApplyer(chatRoomId, sessionUserInfo.getUser_id());

        restResponseDto.setData(chat_room_id);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("submitReview")
    public RestResponseDto submitReview(@RequestBody MissionReviewDto params , HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUser = (UserInfoDto)session.getAttribute("sessionUserInfo");

        missionMapService.submitReview(params, sessionUser);
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("loadMyInfo")
    public RestResponseDto loadMyInfo(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getMyPageInfo(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("loadMyPoint")
    public RestResponseDto loadMyPoint(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getMyPoint(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @GetMapping("loadRegMissionHistory")
    public RestResponseDto loadRegMissionHistory(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getMyregMisisonInfo(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }
    
    @GetMapping("loadMyReview")
    public RestResponseDto loadMyReview(HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();

        UserInfoDto sessionUserInfo = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        restResponseDto.setData(missionMapService.getMyReviewInfo(sessionUserInfo.getUser_id()));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }

    @PostMapping("loadMissionReview")
    public RestResponseDto loadMissionReview(@RequestBody String chat_room_id){

        RestResponseDto restResponseDto = new RestResponseDto();

        int chatRoomId = parseJson.toInt("chat_room_id", chat_room_id);
        
        restResponseDto.setData(missionMapService.getMissionReview(chatRoomId));
        restResponseDto.setResult("Success");
        
        return restResponseDto;
    }


    




}
