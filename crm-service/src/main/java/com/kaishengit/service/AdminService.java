package com.kaishengit.service;

import com.kaishengit.entity.Admin;
import com.kaishengit.service.exception.ValidateException;


/**
 * Created by User on 2017/11/6.
 */
public interface AdminService {

    Admin findByPasswordAndUserName(String userName, String password) throws ValidateException;


    Admin findByUserName(String userName);
}
