package com.cu.ufuf.login.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 세션에 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (request.getSession().getAttribute("sessionUserInfo") == null) {
            response.sendRedirect(request.getContextPath() + "/login/loginRequired");
            return false; // 로그인 페이지로 리다이렉트되었으므로 요청을 중단함
        }

        return true; // 요청을 계속 진행함
    }

    // 이후의 메서드는 필요에 따라 오버라이드할 수 있음
}

