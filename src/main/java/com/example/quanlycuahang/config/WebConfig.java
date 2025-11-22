package com.example.quanlycuahang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/");
    }

    @Bean //  Đánh dấu phương thức này sẽ tạo ra một đối tượng được quản lý bởi Spring Context
    public RestTemplate restTemplate() {
        // Bạn có thể tùy chỉnh RestTemplate ở đây (ví dụ: timeouts, interceptors)
        return new RestTemplate();
    }


}