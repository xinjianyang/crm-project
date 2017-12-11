package com.kaishengit.auth;

import com.kaishengit.entity.Admin;
import com.kaishengit.entity.User;
import com.kaishengit.service.AdminService;
import com.kaishengit.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author NativeBoy
 */
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;
    /**
     * 权限认正方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 登录认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();

        /*User user = userService.findUserByUserName(userName);*/

        Admin admin = adminService.findByUserName(userName);

        if(admin != null){
            return new SimpleAuthenticationInfo(admin,admin.getPassword(),getName());
        }

        return null;
    }
}
