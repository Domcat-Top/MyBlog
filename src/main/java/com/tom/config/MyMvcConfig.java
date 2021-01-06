package com.tom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/swagger-ui.html")
                .addPathPatterns("/admin", "/admin/**")
                // 下面这两行，后面加的，穆奇就是拦截所有请求，以后我的博客不公开了
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/checkLogin", "/css/**", "/img/**", "/editor.md-master/**", "/lib/**");
                // .excludePathPatterns("/", "/css/**", "/img/**", "/editor.md-master/**", "/lib/**");
    }
}


