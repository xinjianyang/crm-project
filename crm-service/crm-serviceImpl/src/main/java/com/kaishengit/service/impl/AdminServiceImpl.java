package com.kaishengit.service.impl;

import com.google.common.collect.Maps;
import com.kaishengit.entity.Admin;


import com.kaishengit.entity.AdminExample;
import com.kaishengit.mapper.AdminMapper;
import com.kaishengit.service.AdminService;
import com.kaishengit.service.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/11/6.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Admin findByPasswordAndUserName(String userName, String password) throws ValidateException{

        Map<String, Object> params = Maps.newHashMap();

        params.put("userName",userName);
        params.put("password",password);

        Admin admin = adminMapper.selctByParams(params);

        if(admin != null){
            return admin;
        }
        logger.info("登陆成功", admin,admin.getUsername());
        throw new ValidateException("账号或密码不正确");


    }

    @Override
    public Admin findByUserName(String userName) {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andUsernameEqualTo(userName);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        if(adminList != null && !adminList.isEmpty()){
            return adminList.get(0);
        }
        return null;
    }
}
