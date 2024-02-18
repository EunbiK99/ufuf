package com.cu.ufuf.mission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cu.ufuf.dto.ItemInfoDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.MissionChatDto;
import com.cu.ufuf.dto.MissionChatRoomDto;
import com.cu.ufuf.dto.MissionCourseDto;
import com.cu.ufuf.dto.MissionInfoDto;
import com.cu.ufuf.dto.MissionProcessDto;
import com.cu.ufuf.dto.MissionRegRequestDto;
import com.cu.ufuf.dto.MissionReviewDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.UserInfoDto;
import com.cu.ufuf.dto.UserPointDto;
import com.cu.ufuf.merchan.mapper.MerchanSqlMapper;
import com.cu.ufuf.mission.mapper.MissionChatSqlMapper;
import com.cu.ufuf.mission.mapper.MissionMapSqlMapper;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Service
@Transactional
public class MissionMapServiceImpl {

    @Autowired
    private MissionMapSqlMapper missionMapSqlMapper;
    @Autowired
    private MerchanSqlMapper merchanSqlMapper;
    @Autowired
    private MissionChatSqlMapper missionChatSqlMapper;
    @Autowired
    private MissionPaymentServiceImpl missionPaymentService;


    // 미션 등록
    public KakaoPaymentReqDto registerMissionProcess(MissionRegRequestDto missionRegRequestDto){

        missionMapSqlMapper.insertMission(missionRegRequestDto.getMissionInfoDto());
        MissionInfoDto missionInfoDto = missionRegRequestDto.getMissionInfoDto();
        int missionId = missionInfoDto.getMission_id();
        int userId = missionInfoDto.getUser_id();

        int totalReward = 0;

        List<MissionCourseDto> courseList = missionRegRequestDto.getMissionCourseDto();
        
        for(MissionCourseDto missionCourseDto : courseList){
            missionCourseDto.setMission_id(missionId);
            missionMapSqlMapper.insertMissionCourse(missionCourseDto);
            int reward = missionCourseDto.getReward();
            totalReward = totalReward + reward;
        }

        ItemInfoDto itemInfoDto = new ItemInfoDto();
        itemInfoDto.setItem_category_id(1);
        itemInfoDto.setMerchan_id(missionId);

        merchanSqlMapper.insertItemInfo(itemInfoDto);
        int itemId = itemInfoDto.getItem_id();

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        String order_id = "MI";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");
        ramdomUUID = ramdomUUID.substring(0, 10);
        ramdomUUID = ramdomUUID.toUpperCase();

        order_id = order_id + itemId + today + ramdomUUID;

        orderInfoDto.setOrder_id(order_id);
        orderInfoDto.setItem_id(itemId);
        orderInfoDto.setUser_id(userId);
        orderInfoDto.setStatus("결제미완료");

        merchanSqlMapper.insertOrderInfo(orderInfoDto);

        return missionPaymentService.kakaoPayReq(orderInfoDto, itemId, totalReward);
    }

    // 미션 등록
    public void submitMissionByPoint(MissionRegRequestDto missionRegRequestDto){

        missionMapSqlMapper.insertMission(missionRegRequestDto.getMissionInfoDto());
        MissionInfoDto missionInfoDto = missionRegRequestDto.getMissionInfoDto();
        int missionId = missionInfoDto.getMission_id();
        int userId = missionInfoDto.getUser_id();

        int totalReward = 0;

        List<MissionCourseDto> courseList = missionRegRequestDto.getMissionCourseDto();
        
        for(MissionCourseDto missionCourseDto : courseList){
            missionCourseDto.setMission_id(missionId);
            missionMapSqlMapper.insertMissionCourse(missionCourseDto);
            int reward = missionCourseDto.getReward();
            totalReward = totalReward + reward;
        }

        UserPointDto userPointDto = new UserPointDto();
        userPointDto.setUser_id(userId);
        userPointDto.setPoint_plus_minus(-totalReward);
        userPointDto.setDetail("미션등록");

        missionMapSqlMapper.insertPoint(userPointDto);

        ItemInfoDto itemInfoDto = new ItemInfoDto();
        itemInfoDto.setItem_category_id(1);
        itemInfoDto.setMerchan_id(missionId);

        merchanSqlMapper.insertItemInfo(itemInfoDto);
        int itemId = itemInfoDto.getItem_id();

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        String order_id = "MI";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");
        ramdomUUID = ramdomUUID.substring(0, 10);
        ramdomUUID = ramdomUUID.toUpperCase();

        order_id = order_id + itemId + today + ramdomUUID;

        orderInfoDto.setOrder_id(order_id);
        orderInfoDto.setItem_id(itemId);
        orderInfoDto.setUser_id(userId);
        orderInfoDto.setStatus("결제완료");

        merchanSqlMapper.insertOrderInfo(orderInfoDto);
    }

    public MissionInfoDto selectMissionByOrderId(String order_id){
        return missionMapSqlMapper.selectMissionByOrderId(order_id);
    }

    // 미션 전체 리스트 출력
    public List<Map<String, Object>> loadMissionList(){
        
        List<Map<String, Object>> missionInfoList = new ArrayList<>();

        List<MissionInfoDto> missionDtoList = missionMapSqlMapper.selectAllMission();
        for(MissionInfoDto missionInfoDto : missionDtoList){
            int mission_id = missionInfoDto.getMission_id();
            int user_id = missionInfoDto.getUser_id();

            Map<String, Object> missionInfo = new HashMap();

            missionInfo.put("missionInfoDto", missionInfoDto);
            missionInfo.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));
            missionInfo.put("userInfoDto", missionMapSqlMapper.selectUserById(user_id));

            missionInfoList.add(missionInfo);
        }

        return missionInfoList;
    }

    // 미션 상세 출력
    public Map<String, Object> getMissionDetail(int mission_id, int user_id){

        Map<String, Object> missionDetail = new HashMap<>();

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);

        int missionUser_id = missionInfoDto.getUser_id();

        MissionChatRoomDto missionChatRoomDto = new MissionChatRoomDto();
        missionChatRoomDto.setMission_id(mission_id);
        missionChatRoomDto.setUser_id(user_id);

        missionDetail.put("missionInfoDto", missionInfoDto);
        missionDetail.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));
        missionDetail.put("userInfoDto", missionMapSqlMapper.selectUserById(missionUser_id));
        missionDetail.put("isUserApplied", missionMapSqlMapper.isUserApplied(missionChatRoomDto));

        return missionDetail;
    }

    public void accMissionPlayer(int chat_room_id, int user_id){

        missionMapSqlMapper.updateAccStatus(chat_room_id);

        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(chat_room_id);
        int mission_id = missionInfoDto.getMission_id();

        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id);
        int player_id = missionChatRoomDto.getUser_id();
        UserInfoDto playerDto = missionMapSqlMapper.selectUserById(player_id);
        UserInfoDto senderDto = missionMapSqlMapper.selectUserById(user_id);

        List<MissionCourseDto> missionCourseDtoList = missionMapSqlMapper.selectCourseByMission(mission_id);
        MissionCourseDto firstMissionCourse = missionCourseDtoList.get(0);

        BigDecimal lat = firstMissionCourse.getLat();
        BigDecimal lng = firstMissionCourse.getLng();
        String senerName = senderDto.getName();
        String playerName = playerDto.getName();
        String title = missionInfoDto.getTitle();

        String text = String.format("""
            <div class="row">
                <div class="col px-0">
                    <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 14.85rem; height: 8rem;" onload="loadMap(%f, %f)">
                    </div>
                </div>
            </div>
            <div class="row py-3">
                <div class="col">
                    <div class="row">
                        <div class="col" style="word-wrap: break-word;">
                            ^alert!^%s님께서 "%s" 미션의 플레이어로 %s님을 수락하셨습니다. 
                            지금부터 제한시간 카운트다운이 시작됩니다!<br> 
                            ^alert!^
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col missionDetailBtn d-grid"></div>
                    </div>
                </div>    
            </div>
            """, lat, lng, senerName, title, playerName);
     
        MissionChatDto missionChatDto = new MissionChatDto();
        missionChatDto.setChat_room_id(chat_room_id);
        missionChatDto.setChat_category_id(3);
        missionChatDto.setUser_id(user_id);
        missionChatDto.setMessage(text);
        missionChatDto.setIs_read("N");

        missionChatSqlMapper.insertChat(missionChatDto);

        missionMapSqlMapper.updateStatus(mission_id, "미션진행중");
    }

    // 미션 코스 완료
    public void insertMissionProcess(MissionProcessDto missionProcessDto){
        missionMapSqlMapper.insertMissionProcess(missionProcessDto);
        updateStatusToAllComplete(missionProcessDto.getChat_room_id());
    }

    // 모든 미션 완료시 미션상태 업데이트
    public void updateStatusToAllComplete(int chat_room_id){

        int countCompleteCourse = missionMapSqlMapper.countCompleteCourse(chat_room_id);
        int countTotalCourse = missionMapSqlMapper.countTotalCourseByChatRoomId(chat_room_id);

        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(chat_room_id);

        if(countCompleteCourse == countTotalCourse){
            missionMapSqlMapper.updateStatus(missionInfoDto.getMission_id(), "결과대기중");
        }

        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id);
        UserInfoDto userInfoDto = missionMapSqlMapper.selectUserById(missionChatRoomDto.getUser_id());

        String playerName = userInfoDto.getName();
        String title = missionInfoDto.getTitle();
        int mission_id = missionInfoDto.getMission_id();

        List<MissionCourseDto> missionCourseDtos = missionMapSqlMapper.selectCourseByMission(mission_id);
        int lastIndex = missionCourseDtos.size()-1;

        BigDecimal lat = missionCourseDtos.get(lastIndex).getLat();
        BigDecimal lng = missionCourseDtos.get(lastIndex).getLng();

        String text = String.format("""
            <div class="row">
                <div class="col px-0">
                    <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 14.85rem; height: 8rem;" onload="loadMap(%f, %f)">
                    </div>
                </div>
            </div>
            <div class="row py-3">
                <div class="col">
                    <div class="row">
                        <div class="col" style="word-wrap: break-word;">
                            ^alert!^%s님께서 "%s" 미션을 완료하셨습니다.<br>
                            아래 버튼을 클릭하시고 리뷰를 작성해주세요!<br> 
                            ^alert!^
                            <span class="py-1" style="font-size:0.8rem">
                                미션 완료 후 3일내로 리뷰 작성을 하지 않으면 자동 성공처리 됩니다.
                            <span>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col missionDetailBtn d-grid"></div>
                    </div>
                </div>
            </div>
            """, lat, lng, playerName, title);
    }


    // 채팅에서 미션 진행보기 누르면 나오는 창
    public Map<String, Object> getMissionProgress(int chat_room_id){

        Map<String, Object> missionProgress = new HashMap<>();

        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(chat_room_id);

        missionProgress.put("missionInfoDto", missionInfoDto);

        return missionProgress;
    }

    public List<Map<String, Object>> getMyPlayMissionList(int user_id){

        List<Map<String, Object>> playMissionList = new ArrayList<>();

        List<MissionChatRoomDto> missionChatRoomList = missionMapSqlMapper.selectMyPlayMission(user_id);

        for(MissionChatRoomDto missionChatRoomDto : missionChatRoomList){

            Map<String, Object> myPlayMission = new HashMap<>();

            int mission_id = missionChatRoomDto.getMission_id();

            myPlayMission.put("missionChatRoomDto", missionChatRoomDto);
            myPlayMission.put("missionInfoDto", missionMapSqlMapper.selectMissionById(mission_id));
            myPlayMission.put("progressPercent", missionMapSqlMapper.countProgressPercent(mission_id));

            playMissionList.add(myPlayMission);
        }
        return playMissionList;
    }

    public List<Map<String, Object>> getmyResMissionList(int user_id){

        List<Map<String, Object>> myResMissionList = new ArrayList<>();

        List<MissionInfoDto> myResMissionDtoList = missionMapSqlMapper.selectMyResMission(user_id);

        for(MissionInfoDto missionInfoDto : myResMissionDtoList){

            Map<String, Object> myResMission = new HashMap<>();

            int mission_id = missionInfoDto.getMission_id();

            myResMission.put("missionInfoDto", missionInfoDto);
            myResMission.put("missionChatRoomDto", missionChatSqlMapper.selectChatRoomListByMission(mission_id));
            myResMission.put("progressPercent", missionMapSqlMapper.countProgressPercent(mission_id));
            myResMission.put("countChatRoom", missionChatSqlMapper.countChatRoomByMission(mission_id));

            myResMissionList.add(myResMission);
        }
        return myResMissionList;
    }

    // 미션 진행자 모집중 가져올때
    public Map<String, Object> getMyResMissionInfoInRecruiting(int mission_id){

        Map<String, Object> missionInfo = new HashMap<>();

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);

        missionInfo.put("missionInfoDto", missionInfoDto);
        missionInfo.put("missionCourseList", missionMapSqlMapper.selectCourseByMission(mission_id));
        missionInfo.put("missionChatRoomList", missionChatSqlMapper.selectChatRoomListByMission(mission_id));

        return missionInfo;
    }

    // 진행중인 미션 가져올때
    public Map<String, Object> loadMyMissionInProgress(int chat_room_id){

        Map<String, Object> missionInfo = new HashMap<>();

        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id);
        int mission_id = missionChatRoomDto.getMission_id();

        List<MissionCourseDto> MissionCourseList = missionMapSqlMapper.selectCourseByMission(mission_id);

        List<Map<String, Object>> courseInfoList = new ArrayList<>();

        for(MissionCourseDto missionCourseDto : MissionCourseList){
            
            Map<String, Object> courseInfo = new HashMap<>();

            int course_id = missionCourseDto.getMission_course_id();
            
            courseInfo.put("missionCourseDto", missionCourseDto);
            courseInfo.put("missionProcessDto", missionMapSqlMapper.selectMissionProcessByChatRoomId(course_id));
            
            courseInfoList.add(courseInfo);
        }

        missionInfo.put("missionChatRoomDto", missionChatRoomDto);
        missionInfo.put("missionInfoDto", missionChatSqlMapper.selectMissionByChatRoom(chat_room_id));
        missionInfo.put("courseInfoList", courseInfoList);
        missionInfo.put("progressPercent", missionMapSqlMapper.countProgressPercent(mission_id));
        missionInfo.put("countCompleteCourse", missionMapSqlMapper.countCompleteCourse(chat_room_id));
        missionInfo.put("isReviewExist", missionMapSqlMapper.isReviewExist(chat_room_id));

        return missionInfo;
    }

    // 미션 포기했을때
    public void giveup(int chat_room_id){

        missionMapSqlMapper.updateGiveup(chat_room_id);
        
        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(chat_room_id);
        missionMapSqlMapper.updateStatus(missionInfoDto.getMission_id(), "미션종료");
    }

    // 내 미션 지원자 리스트
    public List<Map<String, Object>> getMyMissionApplyerList(int mission_id){

        List<Map<String, Object>> applyerInfoList = new ArrayList<>();

        List<MissionChatRoomDto> missionChatRoomList = missionChatSqlMapper.selectChatRoomListByMission(mission_id);
        for(MissionChatRoomDto missionChatRoomDto : missionChatRoomList){

            Map<String, Object> applyerInfo = new HashMap<>();

            int user_id = missionChatRoomDto.getUser_id();

            applyerInfo.put("missionChatRoomDto", missionChatRoomDto);
            applyerInfo.put("userInfoDto", missionMapSqlMapper.selectUserById(user_id));

            applyerInfoList.add(applyerInfo);
        }

        return applyerInfoList;
    }

    // 진행상태에서 미션 수락
    public void accMissionApplyer(int chat_room_id, int user_id){

        missionMapSqlMapper.updateAccStatus(chat_room_id);

        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id);

        int mission_id = missionChatRoomDto.getMission_id();
        int player_id = missionChatRoomDto.getUser_id();
        UserInfoDto playerDto = missionMapSqlMapper.selectUserById(player_id);
        UserInfoDto senderDto = missionMapSqlMapper.selectUserById(user_id);

        MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);

        List<MissionCourseDto> missionCourseDtoList = missionMapSqlMapper.selectCourseByMission(mission_id);
        MissionCourseDto firstMissionCourse = missionCourseDtoList.get(0);

        BigDecimal lat = firstMissionCourse.getLat();
        BigDecimal lng = firstMissionCourse.getLng();
        String senderName = senderDto.getName();
        String playerName = playerDto.getName();
        String title = missionInfoDto.getTitle();

        String text = String.format("""
            <div class="row">
                <div class="col px-0">
                    <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 14.85rem; height: 8rem;" onload="loadMap(%f, %f)">
                    </div>
                </div>
            </div>
            <div class="row py-3">
                <div class="col">
                    <div class="row">
                        <div class="col" style="word-wrap: break-word;">
                            ^alert!^%s님께서 "%s" 미션의 플레이어로 %s님을 수락하셨습니다. 
                            지금부터 제한시간 카운트다운이 시작됩니다!<br> 
                            ^alert!^
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col missionDetailBtn d-grid"></div>
                    </div>
                </div>    
            </div>
            """, lat, lng, senderName, title, playerName);
     
        MissionChatDto missionChatDto = new MissionChatDto();
        missionChatDto.setChat_room_id(missionChatRoomDto.getChat_room_id());
        missionChatDto.setChat_category_id(3);
        missionChatDto.setUser_id(user_id);
        missionChatDto.setMessage(text);
        missionChatDto.setIs_read("N");

        missionChatSqlMapper.insertChat(missionChatDto);

        missionMapSqlMapper.updateStatus(mission_id, "미션진행중");

    }

    // 미션 리뷰 작성
    public void submitReview(MissionReviewDto missionReviewDto, UserInfoDto userInfoDto){

        missionMapSqlMapper.insertReview(missionReviewDto);

        MissionInfoDto missionInfoDto = missionChatSqlMapper.selectMissionByChatRoom(missionReviewDto.getChat_room_id());

        String senderName = userInfoDto.getName();
        String title = missionInfoDto.getTitle();
        int mission_id = missionInfoDto.getMission_id();

        List<MissionCourseDto> missionCourseDtos = missionMapSqlMapper.selectCourseByMission(mission_id);
        int lastIndex = missionCourseDtos.size()-1;

        BigDecimal lat = missionCourseDtos.get(lastIndex).getLat();
        BigDecimal lng = missionCourseDtos.get(lastIndex).getLng();

        String text = String.format("""
            <div class="row">
                <div class="col px-0">
                    <div id="staticMap" class="rounded-bottom-0 rounded-2 border-bottom border-dark-subtle" style="width: 14.85rem; height: 8rem;" onload="loadMap(%f, %f)">
                    </div>
                </div>
            </div>
            <div class="row py-3">
                <div class="col">
                    <div class="row">
                        <div class="col" style="word-wrap: break-word;">
                            ^alert!^%s님께서 "%s" 미션의 리뷰를 작성완료하였습니다.<br>
                            아래 버튼을 클릭하시고 미션 결과를 확인하세요!<br> 
                            ^alert!^
                            <span class="py-1" style="font-size:0.8rem">
                                미션 성공시, 성공 보상 포인트가 즉시 지급되어 마이페이지에서 바로 확인하실 수 있습니다.
                            <span>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col missionDetailBtn d-grid"></div>
                    </div>
                </div>
            </div>
            """, lat, lng, senderName, title);
        
        MissionChatDto missionChatDto = new MissionChatDto();
        missionChatDto.setChat_room_id(missionReviewDto.getChat_room_id());
        missionChatDto.setChat_category_id(3);
        missionChatDto.setUser_id(userInfoDto.getUser_id());
        missionChatDto.setMessage(text);
        missionChatDto.setIs_read("N");

        missionChatSqlMapper.insertChat(missionChatDto);

        MissionChatRoomDto missionChatRoomDto = missionChatSqlMapper.selectChatRoomByChatRoom(missionReviewDto.getChat_room_id());

        if(missionReviewDto.getIs_success().equals("S")){

            missionMapSqlMapper.updateStatus(mission_id, "미션성공");

            // 포인트 추가
            UserPointDto userPointDto = new UserPointDto();
            
            int totalReward = 0;
            for(MissionCourseDto missionCourseDto : missionCourseDtos){
                int courseReward = missionCourseDto.getReward();
                totalReward = totalReward + courseReward;
            }
            
            userPointDto.setUser_id(missionChatRoomDto.getUser_id());
            userPointDto.setPoint_plus_minus(totalReward);
            userPointDto.setDetail("미션성공");
            
            missionMapSqlMapper.insertPoint(userPointDto);

            String order_id = missionMapSqlMapper.selectOrderIdByMissionId(mission_id);
            OrderInfoDto orderInfoDto = new OrderInfoDto();

            orderInfoDto.setOrder_id(order_id);
            orderInfoDto.setStatus("주문완료");

            merchanSqlMapper.updateOrderStatus(orderInfoDto);

        }else{

            missionMapSqlMapper.updateStatus(mission_id, "미션실패");

            int user_id = missionInfoDto.getUser_id();

            int sum = 0;

            for(MissionCourseDto missionCourseDto : missionCourseDtos){
                int reward = missionCourseDto.getReward();
                sum = sum + reward;
            }
            
            UserPointDto userPointDto = new UserPointDto();
            userPointDto.setUser_id(user_id);
            userPointDto.setPoint_plus_minus(sum);
            userPointDto.setDetail("미션실패 환불");

            missionMapSqlMapper.insertPoint(userPointDto);
        }
    }

    // 미션 실패 업데이트
    public void updateMissionFail(){

        List<MissionInfoDto> FailMissionInfoDtoList = missionMapSqlMapper.selectFailMissionForScheduleMethod();

        if(FailMissionInfoDtoList != null){

            for(MissionInfoDto missionInfoDto : FailMissionInfoDtoList){
    
                int mission_id = missionInfoDto.getMission_id();
                missionMapSqlMapper.updateStatus(mission_id, "미션실패");

                int user_id = missionInfoDto.getUser_id();
                
                List<MissionCourseDto> missionCourseDtos = missionMapSqlMapper.selectCourseByMission(missionInfoDto.getMission_id());

                int sum = 0;

                for(MissionCourseDto missionCourseDto : missionCourseDtos){
                    int reward = missionCourseDto.getReward();
                    sum = sum + reward;
                }
                
                UserPointDto userPointDto = new UserPointDto();
                userPointDto.setUser_id(user_id);
                userPointDto.setPoint_plus_minus(sum);
                userPointDto.setDetail("미션실패 환불");

                missionMapSqlMapper.insertPoint(userPointDto);

            }
        }
    }

    // 마이페이지 내 정보
    public Map<String, Object> getMyPageInfo(int user_id){

        Map<String, Object> myInfo = new HashMap<>();

        myInfo.put("userInfoDto", missionMapSqlMapper.selectUserById(user_id));
        myInfo.put("countMyRegMission", missionMapSqlMapper.countMyRegMission(user_id));
        myInfo.put("countMyCompleteMission", missionMapSqlMapper.countMyCompleteMission(user_id));

        return myInfo;
    }

    public int getMyPoint(int user_id){
        return missionMapSqlMapper.countTotalPoint(user_id);
    }

    public List<Map<String, Object>> getMyregMisisonInfo(int user_id){

        List<Map<String, Object>> regMissionInfoList = new ArrayList<>();

        List<MissionInfoDto> myRegMissionDtoList = missionMapSqlMapper.selectMyResMission(user_id);

        for(MissionInfoDto missionInfoDto : myRegMissionDtoList){

            Map<String, Object> myResMission = new HashMap<>();

            int mission_id = missionInfoDto.getMission_id();

            List<MissionCourseDto> missionCourseList = missionMapSqlMapper.selectCourseByMission(mission_id);

            int totalReward = 0;
            for(MissionCourseDto missionCourseDto : missionCourseList){
                int courseReward = missionCourseDto.getReward();
                totalReward = totalReward + courseReward;
            }

            myResMission.put("missionInfoDto", missionInfoDto);
            myResMission.put("totalReward", totalReward);

            List<MissionChatRoomDto> list = missionChatSqlMapper.selectChatRoomListByMission(mission_id);

            if(list != null){
                for(MissionChatRoomDto missionChatRoomDto : list){
                    if(missionChatRoomDto.getAccept_at() != null){
                        myResMission.put("missionChatRoomDto", list);
                    }
                }
            }

            regMissionInfoList.add(myResMission);
        }
        return regMissionInfoList;
    }

    public List<Map<String, Object>> getMyPlayMisisonInfo(int user_id){

        List<Map<String, Object>> regMissionInfoList = new ArrayList<>();

        List<MissionChatRoomDto> myPlayMissionChatRoomDtoList = missionMapSqlMapper.selectMyPlayMission(user_id);

        for(MissionChatRoomDto chatRoomDto : myPlayMissionChatRoomDtoList){

            Map<String, Object> myResMission = new HashMap<>();

            int mission_id = chatRoomDto.getMission_id();

            MissionInfoDto missionInfoDto = missionMapSqlMapper.selectMissionById(mission_id);
            List<MissionCourseDto> missionCourseList = missionMapSqlMapper.selectCourseByMission(mission_id);

            int totalReward = 0;
            for(MissionCourseDto missionCourseDto : missionCourseList){
                int courseReward = missionCourseDto.getReward();
                totalReward = totalReward + courseReward;
            }

            myResMission.put("missionInfoDto", missionInfoDto);
            myResMission.put("totalReward", totalReward);

            List<MissionChatRoomDto> list = missionChatSqlMapper.selectChatRoomListByMission(mission_id);

            if(list != null){
                for(MissionChatRoomDto missionChatRoomDto : list){
                    if(missionChatRoomDto.getAccept_at() != null){
                        myResMission.put("missionChatRoomDto", list);
                    }
                }
            }

            regMissionInfoList.add(myResMission);
        }
        return regMissionInfoList;
    }

    public List<Map<String, Object>> getMyReviewInfo(int user_id){

        List<Map<String, Object>> reviewInfo = new ArrayList<>();

        List<MissionReviewDto> missionReviewDtos = missionMapSqlMapper.selectReviewByUserId(user_id);

        for(MissionReviewDto missionReviewDto : missionReviewDtos){

            int chat_room_id = missionReviewDto.getChat_room_id();

            Map<String, Object> map = new HashMap<>();

            map.put("missionReviewDto", missionReviewDto);
            map.put("missionChatRoomDto", missionChatSqlMapper.selectChatRoomByChatRoom(chat_room_id));
            map.put("missionInfoDto", missionChatSqlMapper.selectMissionByChatRoom(chat_room_id));

            reviewInfo.add(map);
        }

        return reviewInfo;
    }

    // 3일 후 미션 성공
    public void updateSuccessAfter3days(){

        List<MissionInfoDto> missionInfoDtos = missionMapSqlMapper.selectMissionCompleteAfter3days();

        if(missionInfoDtos != null){

            for(MissionInfoDto missionInfoDto : missionInfoDtos){
                missionMapSqlMapper.updateStatus(missionInfoDto.getMission_id(), "미션성공");

                MissionChatRoomDto missionChatRoomDto = missionMapSqlMapper.selectAcceptedChatRoom(missionInfoDto.getMission_id());
                int user_id = missionChatRoomDto.getUser_id();

                List<MissionCourseDto> missionCourseDtos = missionMapSqlMapper.selectCourseByMission(missionInfoDto.getMission_id());

                int sum = 0;

                for(MissionCourseDto missionCourseDto : missionCourseDtos){
                    int reward = missionCourseDto.getReward();
                    sum = sum + reward;
                }
                
                UserPointDto userPointDto = new UserPointDto();
                userPointDto.setUser_id(user_id);
                userPointDto.setPoint_plus_minus(sum);
                userPointDto.setDetail("미션성공");

                missionMapSqlMapper.insertPoint(userPointDto);
            }
        }

    }

    // 리뷰 열람
    public MissionReviewDto getMissionReview(int chat_room_id){
        return missionMapSqlMapper.selectReviewByMission(chat_room_id);
    }

    // 포인트 충전
    public KakaoPaymentReqDto chargePoint(UserPointDto userPointDto){

        ItemInfoDto itemInfoDto = new ItemInfoDto();
        itemInfoDto.setItem_category_id(5);
        itemInfoDto.setMerchan_id(0);

        merchanSqlMapper.insertItemInfo(itemInfoDto);
        int itemId = itemInfoDto.getItem_id();

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        String order_id = "P";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());

        UUID uuid = UUID.randomUUID();
        String ramdomUUID = uuid.toString().replace("-", "");
        ramdomUUID = ramdomUUID.substring(0, 10);
        ramdomUUID = ramdomUUID.toUpperCase();

        order_id = order_id + itemId + today + ramdomUUID;

        orderInfoDto.setOrder_id(order_id);
        orderInfoDto.setItem_id(itemId);
        orderInfoDto.setUser_id(userPointDto.getUser_id());
        orderInfoDto.setStatus("결제미완료");

        return missionPaymentService.KakaoPayReqForPoint(orderInfoDto, itemId, userPointDto.getPoint_plus_minus());
    }

    // 미션 삭제
    public void deleteMission(int mission_id, int user_id){
        
        List<MissionCourseDto> missionCourseDtos = missionMapSqlMapper.selectCourseByMission(mission_id);

        int sum = 0;

        for(MissionCourseDto missionCourseDto : missionCourseDtos){
            int reward = missionCourseDto.getReward();
            sum = sum + reward;
        }
        
        UserPointDto userPointDto = new UserPointDto();
        userPointDto.setUser_id(user_id);
        userPointDto.setPoint_plus_minus(sum);
        userPointDto.setDetail("미션삭제 환불");

        missionMapSqlMapper.insertPoint(userPointDto);

        missionMapSqlMapper.deleteMission(mission_id);
    }











    

    

}
