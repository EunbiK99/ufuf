package com.cu.ufuf.circle.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cu.ufuf.circle.service.CircleService;
import com.cu.ufuf.dto.CircleBoardImageDto;
import com.cu.ufuf.dto.CircleMemberDto;
import com.cu.ufuf.dto.CircleNoticeImageDto;
import com.cu.ufuf.dto.UserInfoDto;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/circle")
public class CircleController {
    
    @Autowired
    private CircleService circleService;

    @RequestMapping("test")
    public String test(){
        
        return "circle/test";
    }

    @RequestMapping("circleMainPage")
    public String circleMainPage(){

        

        return "circle/circleMainPage";
    }
    // !!!!!!!!!!!!!이거 일단 사용하지말자
    @RequestMapping("circleApplyPage")
    public String circleApplyPage(HttpSession session, Model model){
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        model.addAttribute("userInfoDto", userInfoDto);
        
        return "circle/circleApplyPage";
    }
    // !!!!!!!!!!!!!이거 일단 사용하지말자
    @RequestMapping("circleNoticeApplyPage")
    public String circleNoticeApplyPage(HttpSession session, Model model){
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        model.addAttribute("userInfoDto", userInfoDto);
        return "circle/circleNoticeApplyPage";
    }

    @RequestMapping("circleNoticeImagePage")
    public String circleNoticeImagePage(HttpSession session, Model model){
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        
        model.addAttribute("userInfoDto", userInfoDto);
        
        return "circle/circleNoticeImagePage";
    }
    
    @RequestMapping("circleNoticeApplyProcess")
    public String circleNoticeApplyProcess(HttpSession session, @RequestParam("circle_notice_image") MultipartFile[] images){
            // userid 기준으로 max 값 가져와야함 => 다른사람이랑 같이만들고 있으면 충돌날듯?
            UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
            int user_id = userInfoDto.getUser_id();
            int circleId = circleService.circleIdMaxByUserId(user_id);

            for(MultipartFile multipartFile : images){
                // 빈공간 처리
                if(multipartFile.isEmpty()) {
        			continue;
        		}
                // 이게 원본 파일명
                String filename = multipartFile.getOriginalFilename();
                long randomFilename = System.currentTimeMillis();
                String Path = "C:/uploadFiles/";
                

                File realtodayPath = new File(Path);

                // 디렉토리 생성
                if(!realtodayPath.exists()){
                    realtodayPath.mkdirs();
                }

                String commafile = filename.substring(filename.lastIndexOf("."));
                String fileLink = randomFilename + commafile;

                try {
        			multipartFile.transferTo(new File(Path + fileLink));
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
                // 반복문 돌리면서 insert
                CircleNoticeImageDto circleNoticeImageDto = new CircleNoticeImageDto();
                circleNoticeImageDto.setCircle_notice_image(fileLink);
                circleNoticeImageDto.setCircle_id(circleId);

                circleService.circleNoticeImageInfoInsert(circleNoticeImageDto);

                
            }

            return "redirect:./circleMainPage";
    
        }
    // @RequestMapping("circleNoticeApplyProcess")
    // public String circleNoticeApplyProcess(CircleNoticeDto circleNoticeDto, @RequestParam("circle_notice_image") MultipartFile[] images){
    //     // 모집공고 등록 insert(무조건 여기다가 해야됨) // 모집이미지 등록여러개 // !!!!!(아직안함)동아리키 받아와야함
    //     System.out.println(circleNoticeDto.getCircle_notice_title());
    //     System.out.println(circleNoticeDto.getCircle_notice_content());

    //     // 모집공고 등록이 먼저 되어야하고 그다음에 번호값을 받아서(Max) 번호키를 이미지번호키에 등록
        
    //     int noticeId = circleService.circleNoticeIdInt();
    //     for(MultipartFile multipartFile : images){
    //         // 빈공간 처리
    //         if(multipartFile.isEmpty()) {
	// 			continue;
	// 		}
    //         // 이게 원본 파일명
    //         String filename = multipartFile.getOriginalFilename();
    //         long randomFilename = System.currentTimeMillis();
    //         String Path = "C:/uploadFiles/";
            

    //         File realtodayPath = new File(Path);

    //         // 디렉토리 생성
    //         if(!realtodayPath.exists()){
    //             realtodayPath.mkdirs();
    //         }

    //         String commafile = filename.substring(filename.lastIndexOf("."));
    //         String fileLink = randomFilename + commafile;

    //         try {
	// 			multipartFile.transferTo(new File(Path + fileLink));
	// 		}catch(Exception e) {
	// 			e.printStackTrace();
	// 		}
    //         // 반복문 돌리면서 insert
    //         CircleNoticeImageDto circleNoticeImageDto = new CircleNoticeImageDto();
    //         circleNoticeImageDto.setCircle_notice_image(fileLink);
    //         circleNoticeImageDto.setCircle_notice_id(noticeId);

    //         circleService.circleNoticeImageInfoInsert(circleNoticeImageDto);
            
    //     }
        

    //     return "redirect:./circleMainPage";
    // }

    @RequestMapping("circleNoticeBoardPage")
    public String circleNoticeBoardPage(){
        
        return "circle/circleNoticeBoardPage";
    }
    // 동아리 리스트를 출력하는 페이지
    @RequestMapping("circleListPage")
    public String circleListPage(){
        
        return "circle/circleListPage";
    }

    @RequestMapping("circleListArticlePage")
    public String circleListArticlePage(){
        // 여기서 가입신청->->
        // 상세동아리정보 => 서클정보전부 담아와야함
        
        // 이미지 리스트 가져오깅

        return "circle/circleListArticlePage";
    }
    
    @RequestMapping("logoutProcess")
    public String logoutProcess(HttpSession session) {
        // 세션 만료
        session.invalidate();
        // 로그아웃 후 리다이렉션할 홈페이지 경로 반환
        return "redirect:../login/loginPage";
    }
    // 내가 가입한 동아리리스트 (무조건 세션이있어야 볼수있는 페이지)
    @RequestMapping("myCircleListPage")
    public String myCircleListPage(){
        
        

        return "circle/myCircleListPage";
    }
    // 동아리 메인페이지라고 봐도 무방
    @RequestMapping("circleFeedPage")
    public String circleFeedPage(){



        return "circle/circleFeedPage";
    }
    // 동아리 게시글 작성 페이지 (대표이미지는 여기에있음)
    @RequestMapping("circleFeedWritePage")
    public String circleFeedWritePage(){



        return "circle/circleFeedWritePage";
    }
    // 동아리 게시글 상세이미지 넣기
    @RequestMapping("circleFeedImageApplyPage")
    public String circleFeedImageApplyPage(){

        return "circle/circleFeedImageApplyPage";
    }

    // 동아리 게시글 상세이미지 등록완료
    @RequestMapping("circleBoardImageApplyProcess")
    public String circleBoardImageApplyProcess(@RequestParam("circle_id") int circle_id,  @RequestParam("sub_image") MultipartFile[] images, HttpSession session){
        UserInfoDto userInfoDto = (UserInfoDto)session.getAttribute("sessionUserInfo");
        int user_id = userInfoDto.getUser_id();

        CircleMemberDto circleMemberDto = circleService.circleMemberInfoByUserIdAndCircleId(user_id, circle_id);
        int circle_member_id = circleMemberDto.getCircle_member_id();

        // member_id를 통해서 방금작성한글 찾아오면됨 => max값이용
        int board_id = circleService.boardIdMaxByCircleMemberId(circle_member_id);

        for(MultipartFile multipartFile : images){
            // 빈공간 처리
            if(multipartFile.isEmpty()) {
                continue;
            }
            // 이게 원본 파일명
            String filename = multipartFile.getOriginalFilename();
            long randomFilename = System.currentTimeMillis();
            String Path = "C:/uploadFiles/";
            
            File realtodayPath = new File(Path);

            // 디렉토리 생성
            if(!realtodayPath.exists()){
                realtodayPath.mkdirs();
            }

            String commafile = filename.substring(filename.lastIndexOf("."));
            String fileLink = randomFilename + commafile;

            try {
                multipartFile.transferTo(new File(Path + fileLink));
            }catch(Exception e) {
                e.printStackTrace();
            }

            CircleBoardImageDto circleBoardImageDto = new CircleBoardImageDto();
            circleBoardImageDto.setCircle_board_id(board_id);
            circleBoardImageDto.setSub_image(fileLink);
            
            circleService.circleboardImageDtoInsert(circleBoardImageDto);
            
        }

        return "redirect:./circleFeedPage?circle_id=" + circle_id;
    }
    
    @RequestMapping("circleVoteWritePage")
    public String circleVoteWritePage(){

        return "circle/circleVoteWritePage";
    }
    @RequestMapping("circleVoteListPage")
    public String circleVoteListPage(){

        return "circle/circleVoteListPage";
    }
    @RequestMapping("circleVoteListArticlePage")
    public String circleVoteListArticlePage(){

        return "circle/circleVoteListArticlePage";
    }

    // 일정관리페이지 그냥 만들어놨음.. UI작업하자,
    @RequestMapping("circleScheduleManagePage")
    public String circleScheduleManagePage(){

        return "circle/circleScheduleManagePage";
    }

    @RequestMapping("circleScheduleApplyPage")
    public String circleScheduleApplyPage(){
        
        return "circle/circleScheduleApplyPage";
    }
    // 마이페이지 같은느낌? 여기서 동아리 관리 페이지 만들어서 들어가면 => 가입신청 받아주는 곳이 있어야함
    @RequestMapping("circleMyPage")
    public String circleMyPage(){
        
        return "circle/circleMyPage";
    }

    @RequestMapping("circleManagerOnlyPage")
    public String circleManagerOnlyPage(){

        return "circle/circleManagerOnlyPage";
    }
    @RequestMapping("circleApprovalJoinPage")
    public String circleApprovalJoinPage(){

        return "circle/circleApprovalJoinPage";
    }
    @RequestMapping("circleFeedArticlePage")
    public String circleFeedArticlePage(){

        return "circle/circleFeedArticlePage";
    }
    @RequestMapping("circlePaymentFail")
    public String circlePaymentFail(){

        return "circle/circlePaymentFail";
    }
    @RequestMapping("circlePaymentSuccess")
    public String circlePaymentSuccess(){

        return "circle/circlePaymentSuccess";
    }
    @RequestMapping("circlePaymentCancel")
    public String circlePaymentCancel(){

        return "circle/circlePaymentCancel";
    }
    @RequestMapping("circleScheduleListPage")
    public String circleScheduleListPage(){

        return "circle/circleScheduleListPage";
    }
    @RequestMapping("circleMyLikeListPage")
    public String circleMyLikeListPage(){

        return "circle/circleMyLikeListPage";
    }
    @RequestMapping("circleMyVoteListPage")
    public String circleMyVoteListPage(){

        return "circle/circleMyVoteListPage";
    }
    
    
    
    
    


    


}
