package com.kaishengit.auth;

import com.kaishengit.entity.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * shiro的工具类
 * @author NativeBoy
 */
public class ShiroUtil {

    /**
     * 获取当前登陆对象
     * @return
     */
    public static Admin getCurrentAdmin(){

        Subject subject = SecurityUtils.getSubject();
        return (Admin) subject.getPrincipal();
    }
}
