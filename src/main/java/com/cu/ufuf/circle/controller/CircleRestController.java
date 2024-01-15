package com.cu.ufuf.circle.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cu.ufuf.circle.service.CircleService;
import com.cu.ufuf.dto.CircleBoardDto;
import com.cu.ufuf.dto.CircleDto;
import com.cu.ufuf.dto.CircleJoinApplyDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleMiddleCategoryDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.CircleSmallCategoryDto;
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
            @RequestParam("board_content") String board_content,@RequestParam("circle_id") int circle_id, @RequestParam("file") MultipartFile file, HttpSession session) {
                
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
        public RestResponseDto asdfasdfasdfasdf(@RequestParam("circle_id") int circle_id){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(circleService.circleInfoVarious(circle_id));
        responseDto.setResult("success");
        
        return responseDto;
    }

    // RESTAPI 양식
    /*
    @RequestMapping("asdfasdfasdf")
        public RestResponseDto asdfasdfasdfasdf(){
        
        RestResponseDto responseDto = new RestResponseDto();
        
        responseDto.setData(null);
        responseDto.setResult("success");
        
        return responseDto;
    }
     */

}
