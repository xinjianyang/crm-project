package com.kaishengit.test;

import com.kaishengit.util.WeixinPublicUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by User on 2017/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-weixin.xml")
public class WeixinPublicTest {

    @Autowired
    private WeixinPublicUtil weixinPublicUtil;

    @Test
    public void getAccessToken(){
        String token = weixinPublicUtil.getAccessToken();
        System.out.println(token);
    }

    @Test
    public void getWeixinServerIP(){
        weixinPublicUtil.getIpFromServer();
    }
}
