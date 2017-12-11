package com.kaishengit.controller.interceptor;


import com.kaishengit.entity.Admin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *判断用户是否登陆,如果没有登录则跳转到登录界面
 * @author User
 * @date 2017/11/8
 */


public class LoginInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();

        //如果访问的路径是登录界面则放行
        if("".equals(url) || "/login".equals(url)){
            return true;
        }

        if(url.startsWith("/static/")){
            return true;
        }

        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("admin");

        if (admin != null){
            return true;
        }

        response.sendRedirect("/login");

        return false;
    }
}
