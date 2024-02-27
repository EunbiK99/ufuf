package com.cu.ufuf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cu.ufuf.login.interceptor.SessionInterceptor;
import com.cu.ufuf.meeting.interceptor.MeetingInterceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableWebMvc
@EnableScheduling
public class AppConfig implements WebMvcConfigurer{
    
	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 아래 3개 폴더 생성
        // /static/public/js
        // /static/public/css
        // /static/public/image
        
        registry.addResourceHandler("/public/**")
            .addResourceLocations("classpath:/static/public/")
            // .setCachePeriod(60 * 60 * 24 * 365);
        ;  
        
        // 업로드 외부 파일 
        registry.addResourceHandler("/uploadFiles/**")
            .addResourceLocations("file:///C:/uploadFiles/")
            // .setCachePeriod(60 * 60 * 24 * 365);
        ; 
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MeetingInterceptor())
            .addPathPatterns("/meeting/**")
            .excludePathPatterns("/meeting/mainPage/**")
            .excludePathPatterns("/meeting/myPage/**")            
            .excludePathPatterns("/meeting/errorPage/**")            
            .excludePathPatterns("/meeting/api/**")            
            ;
        
        registry.addInterceptor(new SessionInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/login/**")
            .excludePathPatterns("/commons/**")
            .excludePathPatterns("/circle/restApi/**")
            .excludePathPatterns("/circle/circleMainPage**")
            .excludePathPatterns("/room/restApi/**")
            .excludePathPatterns("/room/roomMainPage/**")
            .excludePathPatterns("/room/roomListPage/**")
            .excludePathPatterns("/room/roomDetailPage/**")
            .excludePathPatterns("/mission/map**")
            .excludePathPatterns("/mission/test1**")
            .excludePathPatterns("/mission/restApi/**")
            .excludePathPatterns("/meeting/mainPage/**")
            .excludePathPatterns("/meeting/myPage/**")            
            .excludePathPatterns("/meeting/errorPage/**")    
            .excludePathPatterns("/meeting/api/**")   
            .excludePathPatterns("/public/**")
            .excludePathPatterns("https://ufuf.null-pointer-exception.com")
            ;
    }

}

