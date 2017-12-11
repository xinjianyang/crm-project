package com.kaishengit.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.User;
import com.kaishengit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by User on 2017/11/9.
 */

@Controller
public class WithDeptController {

    @Autowired
    private UserService userService;

    @GetMapping("/finduser/bydept")
    @ResponseBody
    public PageInfo<User> findUserByDeptId(@RequestParam(required = false, name = "p", defaultValue= "1")int pageNo, String deptId){

        return userService.findUserListByDeptId(pageNo, Integer.parseInt(deptId));
    }
}
