package com.winton.ytpaas.common.configuration;

import com.winton.ytpaas.common.configuration.jwt.AuthenticationInterceptor;
import com.winton.ytpaas.common.configuration.log.SystemLogInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有api接口都需要token验证
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/api/**");

        // 日志拦截器
        registry.addInterceptor(systemLogInterceptor())
                .addPathPatterns("/**");
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    @Bean
    public SystemLogInterceptor systemLogInterceptor() {
        return new SystemLogInterceptor();
    }
}