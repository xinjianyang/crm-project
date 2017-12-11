package com.kaishengit.test;

import com.kaishengit.util.WeixinUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/11/21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-weixin.xml")
public class WeixinTest {

    @Autowired
    private WeixinUtil weixinUtil;
    @Test
    public void getAccessToken(){

        String accessToken = weixinUtil.getAccessToken(WeixinUtil.ACCESSTOKEN_TYPE_CONTACTS);

        System.out.println(accessToken);
    }

    @Test
    public void createDept(){
        weixinUtil.createDept(100,10,"坑比之一__英雄联盟");
    }

    @Test
    public void delDept(){
        weixinUtil.delDept(100);
    }

    @Test
    public void addUser(){

        List<Integer> list = new ArrayList<>();
        list.add(1);

        weixinUtil.addUser(1005,"杨新建","17682317256",list);
    }

    @Test
    public void delUser(){
        weixinUtil.deleteUser(1004);
    }

    @Test
    public void sendMessageToUser(){
        weixinUtil.sendMessageToUser(null,"hello, wellcome to jiaozuo ");
    }
}
