package com.winton.ytpaas.common.configuration.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.winton.ytpaas.system.entity.Sys_User;
import com.winton.ytpaas.system.service.Sys_UserService;

import java.lang.reflect.Method;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    Sys_UserService sysUserService;
    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
        
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

         // 执行认证
         if (token == null) {
            throw new RuntimeException("无token，请重新登录系统");
        }
        // 获取 token 中的 user id
        Sys_User user = tokenService.verifyToken(token);
        if(user == null || user.getYHBM() == null) {
            throw new RuntimeException("token已过期，请重新登录系统");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}