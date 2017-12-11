package com.kaishengit.util;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kaishengit.util.exception.WeixinException;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * @author User
 */
@Component
public class WeixinPublicUtil {



    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.appsecret}")
    private String appSecret;

    private static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    private static final String GET_WEIXIN_SERVERIP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s";

    private LoadingCache<String,String> accessTokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(7200, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {

                    String url = String.format(GET_ACCESSTOKEN_URL,appId,appSecret);

                    String result = sendHttpRequestGet(url);

                    Map<String,Object> map = JSON.parseObject(result, HashMap.class);
                    System.out.println(map);
                    if(map.get("access_token") != null){
                        return (String) map.get("access_token");
                    }

                    throw new WeixinException("获取token异常");
                }
            });


    public void getIpFromServer(){
        String url = String.format(GET_WEIXIN_SERVERIP_URL,getAccessToken());

        String result = sendHttpRequestGet(url);
        Map<String,Object> map = JSON.parseObject(result,HashMap.class);

        if(map.get("ip_list") != null){
            System.out.println(map);
        }else {
            throw new WeixinException("获取微信serverIP异常");
        }

    }

    /**
     * 获取access_token
     * @return
     */
    public String getAccessToken(){
        try {
            return accessTokenCache.get("");
        } catch (ExecutionException e) {
            throw new RuntimeException("获取token异常",e);
        }
    }

    /**
     * 发出HTTP get请求
     * @param url 请求路径
     * @return 响应结果
     */
    private String sendHttpRequestGet(String url){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("http请求异常",e);
        }
    }

    /**
     * 发出HTTP post请求
     * @param url 请求路径
     * @return 响应结果
     */
    private String sendHttpRequestPost(String url,String json){

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("Http请求异常",e);
        }
    }

}
