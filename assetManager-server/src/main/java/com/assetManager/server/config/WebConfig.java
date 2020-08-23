package com.assetManager.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * 개발 환경에서 CORS 해결을 위한 코드. <br />
     * 운영 환경에서는 주석처리 할 것!
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO 개발환경에서만!
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:3000")
                .allowCredentials(true);
    }
}
