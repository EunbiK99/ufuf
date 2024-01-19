package com.cu.ufuf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
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

}

