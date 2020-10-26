package com.tom.config;
/*
*
*
*
* 拦截器
 */
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object currentUser = request.getSession().getAttribute("currentUser");

        if(currentUser == null) {
            request.setAttribute("msg", "没有权限,请去往登录!");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
