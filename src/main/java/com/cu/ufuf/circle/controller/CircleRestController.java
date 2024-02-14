package com.cu.ufuf.circle.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.circle.service.CircleService;
import com.cu.ufuf.dto.AmountDto;
import com.cu.ufuf.dto.CardInfoDto;
import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleBoardLikeDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleLikeDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleScheduleApplyDto;
import com.cu.ufuf.dto.CircleScheduleAttendanceDto;
import com.cu.ufuf.dto.CircleScheduleDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
import com.cu.ufuf.dto.CircleVoteCompleteDto;
import com.cu.ufuf.dto.CircleVoteDto;
import com.cu.ufuf.dto.CircleVoteOptionDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptReqDto;
import com.cu.ufuf.dto.KakaoPaymentAcceptResDto;
import com.cu.ufuf.dto.KakaoPaymentCancelReqDto;
import com.cu.ufuf.dto.KakaoPaymentCancelResDto;
import com.cu.ufuf.dto.KakaoPaymentReqDto;
import com.cu.ufuf.dto.KakaoPaymentResDto;
import com.cu.ufuf.dto.OrderInfoDto;
import com.cu.ufuf.dto.RestResponseDto;
import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/circle")
public class CircleRestController {

    @Autowired
    private CircleService circleService;

    @RequestMapping("smallCategoryAll")
    public RestResponseDto smallCategoryAll() {

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> CircleSmallCategoryDtos = circleService.circleSmallCategoryDtos();

        responseDto.setData(CircleSmallCategoryDtos);
        responseDto.setResult("success");

        return responseDto;
    }
    
    @RequestMapping("circlesmallcategoryInfoByMiddleId")
    public RestResponseDto circlesmallcategoryInfoByMiddleId(
            @RequestParam(name = "circle_middle_category_id") int circle_middle_category_id) {

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> CircleSmallCategoryDtos = circleService
                .circlesmallcategoryInfoByMiddleId(circle_middle_category_id);

        responseDto.setData(CircleSmallCategoryDtos);
        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("middleCategoryAll")
    public RestResponseDto middleCategoryAll() {

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleMiddleCategoryDto> CircleMiddleCategoryDtos = circleService.circlemiddlecategoryInfoAll();

        responseDto.setData(CircleMiddleCategoryDtos);
        responseDto.setResult("success");

        return responseDto;
    }

    // circleComplete insert만하니까 데이터는 안보내줘도 됨
    // 여기 세션 정보값 넣어줘야되는데.. 지금 세션정보있나..? 음 아직없으면 만들지말자..
    @RequestMapping("circleComplete")
    public RestResponseDto circleComplete(@RequestParam("file") MultipartFile file,
            @RequestParam("data") int circle_small_category_id,
            @RequestParam("circle_name") String circle_name,
            @RequestParam("circle_content") String circle_content, HttpSession session) {

        // 세션정보들
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        String circle_university = userInfoDto.getUniversity();

        // 인스턴스 생성
        RestResponseDto responseDto = new RestResponseDto();

        // 동아리 대표 배너이미지
        String filename = file.getOriginalFilename();
        long randomFilename = System.currentTimeMillis();
        String Path = "C:/uploadFiles/";

        File realtodayPath = new File(Path);

        if (!realtodayPath.exists()) {
            realtodayPath.mkdirs();
        }

        String commafile = filename.substring(filename.lastIndexOf("."));
        String fileLink = randomFilename + commafile;

        try {
            file.transferTo(new File(Path + fileLink));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 이건 동아리 세팅
        CircleDto circleDto = new CircleDto();
        circleDto.setCircle_small_category_id(circle_small_category_id);
        circleDto.setCircle_name(circle_name);
        circleDto.setCircle_content(circle_content);
        circleDto.setCircle_grade_id(1);
        circleDto.setCircle_image(fileLink);
        circleDto.setUser_id(user_id);
        circleDto.setCircle_university(circle_university);

        circleService.circleInfoInsert(circleDto);
        // 여기까지

        // 동아리를 만들었을 시점에 동아리 회원정보에 지금 세션정보의 값을 넣어주는데 직책은 => 동아리장 P 으로 insert
        int circleId = circleService.circleIdMaxByUserId(user_id);
        String circlePosition = "P";
        CircleMemberDto circleMemberDto = new CircleMemberDto();
        circleMemberDto.setCircle_id(circleId);
        circleMemberDto.setUser_id(user_id);
        circleMemberDto.setCircle_position(circlePosition);
        circleService.cirlceMemberinfoInsert(circleMemberDto);
        // 여기까지

        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("smallCategoryList")
    public RestResponseDto smallCategoryList() {

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleSmallCategoryDto> circleSmallCategoryDtos = circleService.circleSmallCategoryDtos();
        // 여기서 고민 6개만 넣을까?
        List<CircleSmallCategoryDto> circleSmallCategoryDtos2 = new ArrayList<>();
        int sizeToCopy = Math.min(6, circleSmallCategoryDtos.size());
        for (int x = 0; x < sizeToCopy; x++) {
            circleSmallCategoryDtos2.add(circleSmallCategoryDtos.get(x));
        }

        responseDto.setData(circleSmallCategoryDtos2);
        responseDto.setResult("success");

        return responseDto;
    }

    // 동아리 리스트들 목록 출력
    // circleNewListOrderByCircleId
    @RequestMapping("circleNewList")
    public RestResponseDto circleNewList() {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleNewListOrderByCircleId());
        responseDto.setResult("success");

        return responseDto;
    }

    // 동아리 상세페이지 이미지 리스트
    @RequestMapping("circleArticleImageList")
    public RestResponseDto circleArticleImageList(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        List<CircleNoticeImageDto> circleNoticeImageDtos = circleService.circleNoticeImageInfoByCircleId(circle_id);

        responseDto.setData(circleNoticeImageDtos);
        responseDto.setResult("success");

        return responseDto;
    }

    // 동아리 상세페이지 정보들
    @RequestMapping("circleInfoVarious")
    public RestResponseDto circleInfoVarious(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleInfoVarious(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // 동아리 가입신청
    @RequestMapping("circleJoinApplyInsert")
    public RestResponseDto circleJoinApplyInsert(@RequestParam("circle_id") int circle_id, HttpSession session) {

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        String joinsubmit = "N"; // 가입승인여부 N ==> 나중에는 이거 그냥 전부가입으로 풀지 생각중 그러면.. 그냥Y넣으면됨

        RestResponseDto responseDto = new RestResponseDto();

        CircleJoinApplyDto circleJoinApplyDto = new CircleJoinApplyDto();
        circleJoinApplyDto.setCircle_id(circle_id);
        circleJoinApplyDto.setJoin_submit(joinsubmit);
        circleJoinApplyDto.setUser_id(user_id);

        circleService.circleJoinApplyInsert(circleJoinApplyDto);

        responseDto.setResult("success");

        return responseDto;
    }

    // 동아리 hot3개 리스트
    @RequestMapping("circleInfoHotThree")
    public RestResponseDto circleInfoHotThree() {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleInfoHotThree());
        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("myCircleList")
    public RestResponseDto myCircleList(HttpSession session) {
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        List<Map<String, Object>> circleListAndPosition = circleService.myCircleInfoListAndPosition(user_id);

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleListAndPosition);
        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("circleboardList")
    public RestResponseDto circleboardList(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleboardList(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("circleboardApplyComplete")
    public RestResponseDto circleboardApplyComplete(@RequestParam("board_title") String board_title,
            @RequestParam("board_content") String board_content, @RequestParam("circle_id") int circle_id,
            @RequestParam("file") MultipartFile file, HttpSession session) {

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        // circle_id랑 user_id를 통해서 memberid를 찾아서 게시글 등록하는데 insert시켜줌
        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        // 게시글 대표이미지
        String filename = file.getOriginalFilename();
        long randomFilename = System.currentTimeMillis();
        String Path = "C:/uploadFiles/";

        File realtodayPath = new File(Path);

        if (!realtodayPath.exists()) {
            realtodayPath.mkdirs();
        }

        String commafile = filename.substring(filename.lastIndexOf("."));
        String fileLink = randomFilename + commafile;

        try {
            file.transferTo(new File(Path + fileLink));
        } catch (Exception e) {
            e.printStackTrace();
        }

        CircleBoardDto circleBoardDto = new CircleBoardDto();
        circleBoardDto.setBoard_title(board_title);
        circleBoardDto.setBoard_content(board_content);
        circleBoardDto.setCircle_member_id(circle_member_id);
        circleBoardDto.setMain_image(fileLink);
        circleBoardDto.setBoard_cnt(0);

        circleService.circleboardDtoInsert(circleBoardDto);

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("circleInfoOne")
    public RestResponseDto asdfasdfasdfasdf(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleInfoVarious(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // 여긴 투표항목 등록임 잘생각해야됨 맴버아이디랑 투표번호 max값 가져와서 등록해주어야함
    // 왜 String배열안되냐 소리지르고싶네.. 안되면 걍 나눠서 해도되긴함
    // (value = "/voteOptionRegister", consumes =
    // MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping("voteOptionRegister")
    public RestResponseDto voteOptionRegister(
            @RequestParam("circle_id") int circle_id,
            @RequestParam("files") MultipartFile[] imagFiles,
            @RequestParam("option_content") String[] option_content,
            HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);

        int circle_member_id = circleMemberDto.getCircle_member_id(); // 이 키 이용

        int circle_vote_id = circleService.circleVoteMaxCircleVoteId(circle_member_id);

        // 일단 여기까진 건들지말고 값넘어오는지 확인부터 ㄱㄱ
        // 이거근데 .. 값을넘겨주는게 맞나 싶다..

        for (int i = 0; i < option_content.length; i++) {
            String filename = imagFiles[i].getOriginalFilename();
            long randomFilename = System.currentTimeMillis();
            String Path = "C:/uploadFiles/";

            File realtodayPath = new File(Path);

            if (!realtodayPath.exists()) {
                realtodayPath.mkdirs();
            }

            String commafile = filename.substring(filename.lastIndexOf("."));
            String fileLink = randomFilename + commafile;

            try {
                imagFiles[i].transferTo(new File(Path + fileLink));
            } catch (Exception e) {
                e.printStackTrace();
            }

            CircleVoteOptionDto circleVoteOptionDto = new CircleVoteOptionDto();
            circleVoteOptionDto.setCircle_vote_id(circle_vote_id);
            circleVoteOptionDto.setOption_image(fileLink);
            circleVoteOptionDto.setOption_content(option_content[i]);
            circleService.circleVoteOptionInsert(circleVoteOptionDto);

        }

        responseDto.setResult("success");

        return responseDto;
    }

    // 여긴 투표글 등록
    @RequestMapping("voteRegister")
    public RestResponseDto voteRegister(@RequestParam("circle_id") int circle_id,
            @RequestBody CircleVoteDto circleVoteDto, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id(); // 이 키 이용

        circleVoteDto.setCircle_member_id(circle_member_id);
        circleService.circleVoteInsert(circleVoteDto);

        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("voteAllList")
    public RestResponseDto voteAllList(@RequestParam("circle_id") int circle_id) {

        // 동아리 관련된 동아리회원들 모두가져오고 회원들이 쓴 모든투표글 가져올거임

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleVoteAllListInfo(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // voteArticle에 들어갈 정보
    @RequestMapping("voteInfoOne")
    public RestResponseDto voteInfoOne(@RequestParam("circle_vote_id") int circle_vote_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.voteInfoOne(circle_vote_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // option 정보꺼냄
    @RequestMapping("voteOptionInfoArticle")
    public RestResponseDto voteOptionInfoArticle(@RequestParam("circle_vote_id") int circle_vote_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleVoteOptionInfoByCircleVoteId(circle_vote_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // voteCompleteDto에 insert
    //
    @RequestMapping("voteComplete")
    public RestResponseDto voteComplete(@RequestParam("circle_id") int circle_id,
            @RequestParam("circle_vote_id") int circle_vote_id, HttpSession session,
            @RequestBody CircleVoteCompleteDto circleVoteCompleteDto) {
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circleMemberId = circleMemberDto.getCircle_member_id();
        circleVoteCompleteDto.setCircle_member_id(circleMemberId);

        circleService.circleVoteCompleteInfoInsert(circleVoteCompleteDto);

        responseDto.setResult("success");

        return responseDto;
    }

    // 체크한 항목이 존재한가? true false로 하면 될거 같은데.
    // 투표글에는 항목이 여러개 존재 ==> 여러개 항목 중에서 하나라도 체크를 했으면 true가 나와야 함
    @RequestMapping("voteChecked")
    public RestResponseDto voteChecked(@RequestParam("circle_id") int circle_id,
            @RequestParam("circle_vote_id") int circle_vote_id, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        // 일단 동아리 회원번호
        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        // 이 리스트안에 여러개 항목들존재하는데 항목번호, 동아리회원번호 넣고 찾아서 있는지만 체크
        List<CircleVoteOptionDto> circleVoteOptionDto = circleService
                .circleVoteOptionInfoByCircleVoteId(circle_vote_id); // 투표의 항목들
        int cntSwitch = 0;
        for (CircleVoteOptionDto e : circleVoteOptionDto) {
            int vote_option_id = e.getVote_option_id();
            Boolean check = circleService.voteChecked(vote_option_id, circle_member_id);

            if (check) {
                cntSwitch = 1;
                break;
            }

        }
        if (cntSwitch == 0) {
            responseDto.setData(true); // 없으니까 투표가능
        } else {
            responseDto.setData(false); // 있으니까 투표불가
        }

        responseDto.setResult("success");

        return responseDto;
    }

    // scheduleApplyInsert ==> 일정등록하는곳
    @RequestMapping("scheduleApplyInsert")
    public RestResponseDto scheduleApplyInsert(@RequestParam("circle_id") int circle_id,
            @RequestBody CircleScheduleDto circleScheduleDto, HttpSession session) {
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        circleScheduleDto.setCircle_member_id(circle_member_id);
        circleService.circleScheduleInfoInsert(circleScheduleDto);
        // insert 완료
        
        // 카카오페이 결제 상품 insert
        int circle_schedule_id = circleService.circleScheduleIdMaxValue();

        circleService.itemInfoInsert(circle_schedule_id);  // circle_schedule_id들어가야함 selectMax값
        

        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("circleScheduleListAll")
    public RestResponseDto circleScheduleListAll(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleScheduleListAll(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // circleScheduleApplyInsert
    @RequestMapping("circleScheduleApplyInsert")
    public RestResponseDto circleScheduleApplyInsert(@RequestParam("circle_id") int circle_id,
            @RequestParam("circle_schedule_id") int circle_schedule_id, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        CircleScheduleApplyDto circleScheduleApplyDto = new CircleScheduleApplyDto();
        circleScheduleApplyDto.setCircle_member_id(circle_member_id);
        circleScheduleApplyDto.setCircle_schedule_id(circle_schedule_id);

        circleService.circleScheduleApplicationInfoInsert(circleScheduleApplyDto);
        
        // 여기서 이제 max값 받아와서 일정 출석부 insert시켜주면됨 ==> 이러면 일정출석부도 insert 완료
        // 밑에 받아올때 파라미터로 동아리 맴버번호 보내는 이유 ==> 혹시라도 동시에 다른사람이랑 하면 꼬일까봐..
        int maxValue = circleService.circleScheduleApplicationMaxIdByCircleMemId(circle_member_id);
        
        CircleScheduleAttendanceDto circleScheduleAttendanceDto = new CircleScheduleAttendanceDto();
        circleScheduleAttendanceDto.setCircle_schedule_application_id(maxValue);
        circleScheduleAttendanceDto.setAttendance("N");
        
        circleService.circleScheduleAttenDanceInfoInfoInsert(circleScheduleAttendanceDto);

        responseDto.setResult("success");

        return responseDto;
    }

    // circleScheduleAppleyCheck
    @RequestMapping("circleScheduleAppleyCheck")
    public RestResponseDto circleScheduleAppleyCheck(@RequestParam("circle_id") int circle_id,
            @RequestParam("circle_schedule_id") int circle_schedule_id, HttpSession session) {
                
        RestResponseDto responseDto = new RestResponseDto();
        
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();
        
        Boolean check = circleService.scheduleApplyCheckByCircleScheduleIdAndCircleMemberId(circle_member_id,
                circle_schedule_id);

        responseDto.setData(check);
        responseDto.setResult("success");

        return responseDto;
    }

    // circleScheduleRegisterCheck
    @RequestMapping("circleScheduleRegisterCheck")
    public RestResponseDto circleScheduleRegisterCheck(@RequestParam("circle_id") int circle_id, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        Boolean RegisterCheck = circleService.scheduleApplyCheckPAndAByCircleMemberId(circle_member_id);

        responseDto.setData(RegisterCheck);
        responseDto.setResult("success");

        return responseDto;
    }

    // circleListOnlyManager
    @RequestMapping("circleListOnlyManager")
    public RestResponseDto circleListOnlyManager(HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        // 여기서 user_id 넣었을때 동아리회원목록이 전부나오게해야함

        responseDto.setData(circleService.circleListOnlyManager(user_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // ApprovalJoinAllList
    @RequestMapping("ApprovalJoinAllList")
    public RestResponseDto ApprovalJoinAllList(@RequestParam("circle_id") int circle_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.ApprovalJoinAllListByCircleIdAndSubmitN(circle_id));
        responseDto.setResult("success");

        return responseDto;
    }

    // approvalJoin
    @RequestMapping("approvalJoin")
    public RestResponseDto approvalJoin(@RequestParam("circle_id") int circle_id,
            @RequestParam("circle_join_apply_id") int circle_join_apply_id, @RequestParam("user_id") int user_id) {

        RestResponseDto responseDto = new RestResponseDto();

        // 가입신청 Y로바꿔줌
        circleService.circleJoinApplyCompleteUpdateByCircleJoinApplyId(circle_join_apply_id);
        // 그리고 회원정보 넣어주기 M으로
        CircleMemberDto circleMemberDto = new CircleMemberDto();
        circleMemberDto.setCircle_id(circle_id);
        // 헐이거 가입해시켜주는애 세션으로 하면안됨..
        circleMemberDto.setUser_id(user_id);
        circleMemberDto.setCircle_position("M");

        circleService.cirlceMemberinfoInsert(circleMemberDto);

        responseDto.setResult("success");

        return responseDto;
    }

    // verificationjoinApply
    @RequestMapping("verificationjoinApply")
    public RestResponseDto verificationjoinApply(@RequestParam("circle_id") int circle_id, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        responseDto.setData(circleService.verificationjoinApplyByUserIdAndCircleId(circle_id, user_id));
        responseDto.setResult("success");

        return responseDto;
    }

    @RequestMapping("verificationUniversity")
    public RestResponseDto verificationUniversity(@RequestParam("circle_id") int circle_id, HttpSession session) {

        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        String user_university = userInfoDto.getUniversity();

        CircleDto circleDto = circleService.circleInfo(circle_id);
        String circle_university = circleDto.getCircle_university();

        boolean check = false;
        if (user_university.equals(circle_university)) {
            check = true;
        }

        responseDto.setData(check);
        responseDto.setResult("success");

        return responseDto;
    }

    // boardSubImageSelect
    @RequestMapping("boardSubImageSelect")
    public RestResponseDto boardSubImageSelect(@RequestParam("circle_board_id") int circle_board_id) {

        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.circleBoardImageInfoByCircleBoardId(circle_board_id));
        responseDto.setResult("success");

        return responseDto;
    }
    //kakaoPaymentInfoInsert 결제요청
    @RequestMapping("kakaoPaymentReqInsert")
      public RestResponseDto kakaoPaymentReqInsert(@RequestBody KakaoPaymentReqDto kakaoPaymentReqDto){
      
      circleService.kakaoPaymentReqInsert(kakaoPaymentReqDto);

      RestResponseDto responseDto = new RestResponseDto();

      
      
      responseDto.setResult("success");
      
      return responseDto;
    }
    //kakaoPaymentResInsert 결제준비응답
    @RequestMapping("kakaoPaymentResInsert")
      public RestResponseDto kakaoPaymentResInsert(@RequestBody KakaoPaymentResDto kakaoPaymentResDto){
      
      RestResponseDto responseDto = new RestResponseDto();

      circleService.kakaoPaymentResInsert(kakaoPaymentResDto);
      
      responseDto.setResult("success");
      
      return responseDto;
    }

    //itemPkget
    @RequestMapping("itemPkget")
      public RestResponseDto itemPkget(@RequestParam("circle_schedule_id") int circle_schedule_id){
      
      RestResponseDto responseDto = new RestResponseDto();
    
      responseDto.setData(circleService.itemPkGetByCircleScheduleId(circle_schedule_id));
      responseDto.setResult("success");
      
      return responseDto;
      }
      // orderInfoInsert
      // 주문정보 insert는 잘됨
    @RequestMapping("orderInfoInsert")
        public RestResponseDto orderInfoInsert(@RequestParam("item_id") int item_id, @RequestParam("circle_schedule_id") int circle_schedule_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();
        // 하. 이거왜함? 구매자 정보가 들어가야함
        int scheduleUserId = circleService.userPkByCircleScheduleId(circle_schedule_id);
        String scheduleUserIdString = String.valueOf(scheduleUserId); // string값으로 변환시켜줘야됨
        

        // 여긴 insert를 위한 코드 
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        // 주문번호 생성
        // MI + 상품코드 + 주문이 발생한 연도 + 주문이 발생한 월 + 주문이 발생한 날짜 + UUID(substring(0, 10))
        String orderNumber = "CC" + item_id;

        // 현재 날짜 정보 가져오기
        
        Date currentDate = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        // 주문이 발생한 연도, 월, 날짜 추가
        orderNumber += yearFormat.format(currentDate);
        orderNumber += monthFormat.format(currentDate);
        orderNumber += dayFormat.format(currentDate);
        
        // UUID 생성 및 substring(0, 10)으로 처리
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        orderNumber += uuid;
        
        // 대입
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setItem_id(item_id);
        orderInfoDto.setUser_id(user_id);
        orderInfoDto.setStatus("READY");
        orderInfoDto.setOrder_id(orderNumber);        

        // insert
        circleService.orderInfoInsert(orderInfoDto);
        
        // 데이터 보낼거 ==> 주문정보pk + 판매자userpk
        Map<String, Object> map = new HashMap<>();
        map.put("schedule_user_id", scheduleUserIdString);
        map.put("order_id", orderNumber); // ?????????????????????????? 이걸 잘못넣었네..휴,.. max값을 넣네 미친놈..
        map.put("item_id", item_id); // 그냥한번넣어봄 test 지워도 무관
        
        responseDto.setData(map);
        responseDto.setResult("success");
        
        return responseDto;
    }
    // kakaoPaymentAcceptReqInsert  요청값 insert
    @RequestMapping("kakaoPaymentAcceptReqInsert")
        public RestResponseDto kakaoPaymentAcceptReqInsert(@RequestBody KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        circleService.kakaoPaymentAcceptReqInsert(kakaoPaymentAcceptReqDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 승인응답테이블 ==> 여기서 amount는 서버에서 dto로 응답받음 insert시키고 max값 받아서 insert시켜야됨 (amount먼저)
    @RequestMapping("kakaoPaymentAcceptResInsert") 
        public RestResponseDto kakaoPaymentAcceptResInsert(@RequestBody KakaoPaymentAcceptResDto kakaoPaymentAcceptResDto){
        
        RestResponseDto responseDto = new RestResponseDto();
        // 여기서 amountpk랑 cardpk받아서 insert 시켜야함
        kakaoPaymentAcceptResDto.setAmount(circleService.amountIdMax());
        kakaoPaymentAcceptResDto.setCard_info(circleService.cardIdMax());
        
        // 이거 승인 완료시간이 이상함??? 21시 몇분 이렇게 나옴 => 난 12시에 함 !!!!!!
        circleService.kakaoPaymentAcceptResInsert(kakaoPaymentAcceptResDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 세션값에 cid tid partner user_id, order_id 저장
    @RequestMapping("sessionPaymentInfoRestore")
        public RestResponseDto sessionPaymentInfoRestore(HttpSession session, @RequestBody KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        session.setAttribute("kakaoPaymentAcceptReqValue", kakaoPaymentAcceptReqDto); // 세션값에 value를 집어넣은다음에 성공페이지에서 값을 insert해줌

        responseDto.setResult("success");
        
        return responseDto;
    }
    // sessionKakaoAcceptReqValue
    @RequestMapping("sessionKakaoAcceptReqValue")
        public RestResponseDto sessionKakaoAcceptReqValue(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        KakaoPaymentAcceptReqDto kakaoPaymentAcceptReqDto = (KakaoPaymentAcceptReqDto)session.getAttribute("kakaoPaymentAcceptReqValue");
        
        responseDto.setData(kakaoPaymentAcceptReqDto);
        responseDto.setResult("success");
        
        return responseDto;
    }
    //amountInfoInsert
    @RequestMapping("amountInfoInsert")
        public RestResponseDto amountInfoInsert(@RequestBody AmountDto amountDto){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.amountInfoInsert(amountDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // cardInfoInsert
    @RequestMapping("cardInfoInsert")
        public RestResponseDto cardInfoInsert(@RequestBody CardInfoDto cardInfoDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        circleService.cardInfoInsert(cardInfoDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    //orderStatusChange
    @RequestMapping("orderStatusChange")
        public RestResponseDto orderStatusChange(@RequestParam("order_id") String order_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.orderInfoStatusByOrderId(order_id);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 카테고리 수정 circleListByCircleSmallcategory
    @RequestMapping("circleListByCircleSmallcategory")
        public RestResponseDto circleListByCircleSmallcategory(@RequestParam("circle_small_category_id") int circle_small_category_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        // 리스트값 들어가야함
        responseDto.setData(circleService.circleListByCircleSmallcategory(circle_small_category_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circlePopularList
    @RequestMapping("circlePopularList")
        public RestResponseDto circlePopularList(){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.circleMemberListMemberCntOrderByCircleId());
        responseDto.setResult("success");
        
        return responseDto;
    }
    @RequestMapping("circleGradeList")
        public RestResponseDto circleGradeList(){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.circleInfoListOrderByGradeId());
        responseDto.setResult("success");
        
        return responseDto;
    } 
    //circleLikeInfoInsert
    @RequestMapping("circleLikeInfoInsert")
        public RestResponseDto circleLikeInfoInsert(@RequestParam("circle_id") int circle_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        CircleLikeDto circleLikeDto = new CircleLikeDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        circleLikeDto.setUser_id(user_id);
        circleLikeDto.setCircle_id(circle_id);
        
        circleService.circleLikeInfoInsert(circleLikeDto);

        responseDto.setResult("success");
        
        return responseDto;
    }
    @RequestMapping("circleLikeCheck")
        public RestResponseDto circleLikeCheck(@RequestParam("circle_id") int circle_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        CircleLikeDto circleLikeDto = new CircleLikeDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        circleLikeDto.setUser_id(user_id);
        circleLikeDto.setCircle_id(circle_id);
        
        responseDto.setData(circleService.circleLikeInfoCheck(circleLikeDto));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleLikeDelete
    @RequestMapping("circleLikeDelete")
        public RestResponseDto circleLikeDelete(@RequestParam("circle_id") int circle_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        CircleLikeDto circleLikeDto = new CircleLikeDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        circleLikeDto.setUser_id(user_id);
        circleLikeDto.setCircle_id(circle_id);
        
        circleService.circleLikeInfoDelete(circleLikeDto);

        responseDto.setResult("success");
        
        return responseDto;
    }

    @RequestMapping("circleMyLikeList")
        public RestResponseDto circleMyLikeList(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        responseDto.setData(circleService.circleLikeInfo(user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleMyVoteList
    @RequestMapping("circleMyVoteList")
        public RestResponseDto circleMyVoteList(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        

        responseDto.setData(circleService.circleMyVoteList(user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleSchedulePeopleCheck
    @RequestMapping("circleSchedulePeopleCheck")
        public RestResponseDto circleSchedulePeopleCheck(@RequestParam("circle_schedule_id") int circle_schedule_id){
        
        RestResponseDto responseDto = new RestResponseDto();

        CircleScheduleDto circleScheduleDto = circleService.circleScheduleByCircleScheduleId(circle_schedule_id);
        int participationPeople = circleScheduleDto.getParticipation(); // 참여인원

        int applicationPeople = circleService.circleScheduleApplicationByCircleScheduleId(circle_schedule_id); // 신청한인원

        Boolean check = true; // 초기값 ==> 일정을 신청할 수 있음
        if(participationPeople <= applicationPeople){ // 신청한인원이 참여인원이랑 인원수가 같거나 클경우 check값은 false가됨 ==> 일정 신청 불가
            check = false;
        }

        responseDto.setData(check);
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleScheduleStartTimeCheck
    @RequestMapping("circleScheduleStartTimeCheck")
        public RestResponseDto circleScheduleStartTimeCheck(@RequestParam("circle_schedule_id") int circle_schedule_id){
        
        RestResponseDto responseDto = new RestResponseDto();

        CircleScheduleDto circleScheduleDto = circleService.circleScheduleByCircleScheduleId(circle_schedule_id);

        LocalDateTime startTime = circleScheduleDto.getStart_time();
        LocalDateTime currentTime = LocalDateTime.now();
        
        Boolean check = true;
        if(startTime.isAfter(currentTime)){ // 일정시작일이 현재시간보다 이후일때 실행 곧, 지금시간은 일정시작일보다 이전이라는뜻 // 일정신청가능
        
        }else if(startTime.isBefore(currentTime)){ // 일정시작일이 현재시간보다 이전일때 실행 // 일정신청 불가
            check = false;
        }else{ // 같을때 실행 // 일정신청불가
            check = false;
        }
        
        responseDto.setData(check);
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleScheduleAttendanceByUserId
    @RequestMapping("circleScheduleApplyByUserId")
        public RestResponseDto circleScheduleApplyByUserId(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        // 일정리스트에 담을내용들
        responseDto.setData(circleService.circleScheduleApplyByUserId(user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // kakakPaymentCancleReqInsert
    @RequestMapping("kakakPaymentCancleReqInsert")
        public RestResponseDto kakakPaymentCancleReqInsert(@RequestBody KakaoPaymentCancelReqDto kakaoPaymentCancelReqDto){
        
        RestResponseDto responseDto = new RestResponseDto();

        circleService.kakakPaymentCancleReqInsert(kakaoPaymentCancelReqDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // kakakPaymentCancleResInsert
    @RequestMapping("kakakPaymentCancleResInsert")
    public RestResponseDto kakakPaymentCancleResInsert(@RequestBody KakaoPaymentCancelResDto kakaoPaymentCancelResDto){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.kakakPaymentCancleResInsert(kakaoPaymentCancelResDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // paymentCancelStatusChange
    @RequestMapping("paymentCancelStatusChange")
        public RestResponseDto paymentCancelStatusChange(@RequestParam("order_id") String order_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.paymentCancelStatusChangeOrderInfoStatus(order_id);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // scheduleApplicationTableDelete
    @RequestMapping("scheduleApplicationTableDelete")
        public RestResponseDto scheduleApplicationTableDelete(@RequestParam("circle_schedule_application_id") int circle_schedule_application_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.scheduleApplicationTableDelete(circle_schedule_application_id);

        responseDto.setResult("success");
        
        return responseDto;
    }
    // votePopularList
    @RequestMapping("votePopularList")
        public RestResponseDto votePopularList(){
        
        RestResponseDto responseDto = new RestResponseDto();

        
        responseDto.setResult("success");
        
        return responseDto;
    }
    //voteThreeNewList
    @RequestMapping("voteThreeNewList")
        public RestResponseDto voteThreeNewList(){
        
        RestResponseDto responseDto = new RestResponseDto();

        responseDto.setData(circleService.voteThreeNewList());
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleJoinCheckVoteAssociation 이거 투표글 확인하면서 만약 동아리
    @RequestMapping("circleJoinCheckVoteAssociation")
        public RestResponseDto circleJoinCheckVoteAssociation(@RequestParam("circle_id") int circle_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        Boolean check;
        if(circleMemberDto == null){
            check = false;
        }else{
            check = true;
        }
        
        responseDto.setData(check);
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 일정출석부 insert 관해서 넣어보겠슴.. 이건 거의 날짜기준이라고 봐도 무방함
    // 이건 일정번호기준으로 여러명의 일정신청테이블이 있고 일정신청에따른 일정 출석부 테이블이 있다.. 일정이 갑자기 철회되지 않는이상..
    // 잠깐.. 이거 할필요가 있나..? ==> 일정 신청이되면 자동으로 일정출석부를 같이 insert시켜주면 되잖아...
    @RequestMapping("circleAttendanceList")
        public RestResponseDto circleAttendanceList(@RequestParam("circle_id") int circle_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        // 이렇게하면 내 동아리맴버 번호가 나오고 내가 작성한 일정을 찾아오면됨 (차피 일정작성은 P,A권한만 쓸 수 있으니까 따로제약안걸어도될듯?)
        
        // 일정리스트 나옴 근데 한가지 안한게 있음.. ==> 일정종료일 기준으로 생각해야할듯..? 지금 여기서 서버에서 조정을해서 가져갈지 자바스크립트로 조정을 할지?
        // 일정이 종료되고나서 2일동안은 출석을 할 수 있게끔 풀어놓는.. 시스템느낌으로 할거같음
        responseDto.setData(circleService.circleAttendanceList(circle_id, user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 일정신청+일정출석부 엮으면됨 ==> 이건 그 일정리스트를 클릭했을때 circle_schedule_id를 보내고 신청or출석부 리스트를 가져온다.
    // 이거 누가신청했는지 동아리 맴버도 엮어서 유저정보를 가져온다.
    @RequestMapping("circleAttendanceApplicationList")
        public RestResponseDto circleAttendanceApplicationList(@RequestParam("circle_schedule_id") int circle_schedule_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        
        responseDto.setData(circleService.circleAttendanceApplicationList(circle_schedule_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    // 다음에 해야할건 이제 출석부 update ==> N => Y로 바꿈
    @RequestMapping("circleAttendanceChangeY")
        public RestResponseDto circleAttendanceChangeY(@RequestParam("circle_schedule_application_id") int circle_schedule_application_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.circleAttendanceChangeY(circle_schedule_application_id);

        responseDto.setResult("success");
        
        return responseDto;
    }
    // 그리고 출석부 버튼을 바꾸는 검증같은게 있어야할거임..
    //circleAttendanceChangeN
    @RequestMapping("circleAttendanceChangeN")
        public RestResponseDto circleAttendanceChangeN(@RequestParam("circle_schedule_application_id") int circle_schedule_application_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        circleService.circleAttendanceChangeN(circle_schedule_application_id);
        
        responseDto.setResult("success");
        
        return responseDto; 
    }
    // getMyInfoSelect
    @RequestMapping("getMyInfoSelect")
        public RestResponseDto getMyInfoSelect(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();
        
        responseDto.setData(null);
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleBoardLikeInsert
    @RequestMapping("circleBoardLikeInsert")
        public RestResponseDto circleBoardLikeInsert(@RequestParam("circle_id") int circle_id, @RequestParam("circle_board_id") int circle_board_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();
        
        CircleBoardLikeDto circleBoardLikeDto = new CircleBoardLikeDto();
        circleBoardLikeDto.setCircle_board_id(circle_board_id);
        circleBoardLikeDto.setCircle_member_id(circle_member_id);

        circleService.circleBoardLikeInsert(circleBoardLikeDto);
        
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleBoardLikeCheck
    @RequestMapping("circleBoardLikeCheck")
        public RestResponseDto circleBoardLikeCheck(@RequestParam("circle_id") int circle_id, @RequestParam("circle_board_id") int circle_board_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();

        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();
        
        CircleBoardLikeDto circleBoardLikeDto = new CircleBoardLikeDto();
        circleBoardLikeDto.setCircle_board_id(circle_board_id);
        circleBoardLikeDto.setCircle_member_id(circle_member_id);
        
        // 여기서 이제 체크 이번엔 리턴값 Boolean을 받아올거임
        Boolean check = circleService.circleBoardLikeCheck(circleBoardLikeDto);

        responseDto.setData(check);
        responseDto.setResult("success");
        
        return responseDto;
    }
    // circleBoardLikeDelete
    @RequestMapping("circleBoardLikeDelete")
        public RestResponseDto circleBoardLikeDelete(@RequestParam("circle_id") int circle_id, @RequestParam("circle_board_id") int circle_board_id, HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();
        
        CircleBoardLikeDto circleBoardLikeDto = new CircleBoardLikeDto();
        circleBoardLikeDto.setCircle_board_id(circle_board_id);
        circleBoardLikeDto.setCircle_member_id(circle_member_id);

        circleService.circleBoardLikeDelete(circleBoardLikeDto);

        responseDto.setResult("success");
        
        return responseDto;
    }
    //voteChartInfo
    @RequestMapping("voteChartInfo")
        public RestResponseDto voteChartInfo(@RequestParam("circle_vote_id") int circle_vote_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.voteChartInfo(circle_vote_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleJoinCheck 가입신청체크
    @RequestMapping("circleJoinCheck")
        public RestResponseDto circleJoinCheck(HttpSession session, @RequestParam("circle_id") int circle_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        responseDto.setData(circleService.circlejoinApplyPossibleCheck(circle_id, user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    //myPageInfo
    @RequestMapping("myPageInfo")
        public RestResponseDto myPageInfo(HttpSession session){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        UserInfoDto userInfoDto = (UserInfoDto) session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        responseDto.setData(circleService.myPageInfo(user_id));
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleSearchList
    @RequestMapping("circleSearchList")
        public RestResponseDto circleSearchList(@RequestParam("searchword") String searchword){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.circleSearchList(searchword)); // 검색한 리스트들이 나와야함
        responseDto.setResult("success");
        
        return responseDto;
    }
    //circleNameDuplicateCheck
    @RequestMapping("circleNameDuplicateCheck")
        public RestResponseDto circleNameDuplicateCheck(@RequestParam("circle_name") String circle_name){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.circleNameDuplicateCheck(circle_name)); // 검색한 리스트들이 나와야함
        responseDto.setResult("success");
        
        return responseDto;
    }

    // RESTAPI 양식 
    /*
    -------------
    
    @RequestMapping("asdfasdfasdf")
        public RestResponseDto asdfasdfasdfasdf(){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(null);
        responseDto.setResult("success");
        
        return responseDto;
    }
    --------------
    */

}
