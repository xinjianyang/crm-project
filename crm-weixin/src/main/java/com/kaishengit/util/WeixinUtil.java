package com.kaishengit.util;


import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kaishengit.util.exception.WeixinException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 2017/11/21.
 */

@Component
public class WeixinUtil {

    public static final String ACCESSTOKEN_TYPE_NORMAL = "normal";

    public  static final String ACCESSTOKEN_TYPE_CONTACTS = "contacts";


    @Value("${weixin.secret}")
    private String secret;
    @Value("${weixin.corpId}")
    private String corpId;
    @Value("${weixin.tongxun.secret}")
    private String contactSecret;
    @Value("${weixin.agentId}")
    private Integer agentId;
    /**
     * 获取token的url路径
     */
    private static final String GET_ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    /**
     * 获取创建部门的access token的url
     */
    private static final String POST_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";
    /**
     * 获取删除部门的access token的url
     */
    private static final String GET_DELETE_DEPT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";
    /**
     * 创建成员的token 的url
     */
    private static final String POST_ADDUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";
    /**
     * 删除成员的token 的url
     */
    private static final String GET_DELETE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s";

    /**
     * 发送消息的url
     */
    private static final String POST_SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";



    private LoadingCache<String,String> accessTokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String type) throws Exception {

                    //判断获取的是普通的access Token 还是通讯录的accesstoken
                    String url;
                    if(ACCESSTOKEN_TYPE_CONTACTS.equals(type)){
                        url = String.format(GET_ACCESS_TOKEN_URL,corpId,contactSecret);
                    }else{
                        url = String.format(GET_ACCESS_TOKEN_URL,corpId,secret);
                    }

                    String resultJson = sendHttpRequestGet(url);

                    Map<String,Object> map = JSON.parseObject(resultJson, HashMap.class);
                    if(map.get("errcode").equals(0)){

                        return (String) map.get("access_token");
                    }
                    throw new WeixinException(resultJson);
                }
            });

    /**
     * 给用户发消息
     * @param userIdList
     * @param message
     */
    public void sendMessageToUser(List<Integer> userIdList, String message){

        String url = String.format(POST_SEND_MESSAGE_URL,getAccessToken(WeixinUtil.ACCESSTOKEN_TYPE_NORMAL));

/*        StringBuilder sb = new StringBuilder();
        for(Integer id : userIdList){
            sb.append(id).append("|");
        }

        String touer = sb.toString();

        touer = touer.substring(0,touer.lastIndexOf("|"));*/

        Map<String,Object> map = new HashMap<>();
        map.put("touser","YangXinJian");
        map.put("msgtype","text");
        map.put("agentid",agentId);


        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put("content",message);

        map.put("text",contentMap);

        String result = sendHttpRequestPost(url, JSON.toJSONString(map));

        Map<String,Object> json = JSON.parseObject(result,HashMap.class);
        if(!json.get("errcode").equals(0)){
            System.out.println(json);
            throw new WeixinException("发送消息异常");
        }
    }

    /**
     *
     * @param type accessToken 的类型
     * @return
     */
    public String getAccessToken(String type){
        try {
            return accessTokenCache.get(type);
        } catch (ExecutionException e) {
            throw new RuntimeException("获取access Token异常",e);
        }
    }

    /**
     *
     * @param userId 新建用户的id
     * @param name 名字
     * @param mobile 手机号码
     * @param idList 所属部门的id集合
     */
    public void addUser(Integer userId, String name, String mobile, List<Integer> idList){
        String url = String.format(POST_ADDUSER_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS));

        Map<String,Object> map = new HashMap<>();
        map.put("userid",userId);
        map.put("name",name);
        map.put("mobile",mobile);
        map.put("department",idList);

        String result = sendHttpRequestPost(url,JSON.toJSONString(map));

        Map<String,Object> resultMap = JSON.parseObject(result);
        System.out.println(result);
        if(!resultMap.get("errcode").equals(0)){
            throw new WeixinException("添加成员异常");
        }
    }

    /**
     * 删除成员
     * @param userId 成员id
     */
    public void deleteUser(Integer userId){
        String url = String.format(GET_DELETE_USER_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS),userId);

        String result = sendHttpRequestGet(url);
        Map<String,Object> map = JSON.parseObject(result);
        System.out.println(result);
        if(!map.get("errcode").equals(0)){
            throw new WeixinException("删除成员异常");
        }

    }

    /**
     * 创建部门
     * @param id 部门id
     * @param parentId 父部门id
     * @param name 部门名称
     */
    public void createDept(Integer id, Integer parentId, String name){

        String url = String.format(POST_DEPT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS));

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("parentid",parentId);
        map.put("name",name);
        String resultJson = sendHttpRequestPost(url,JSON.toJSONString(map));

        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if(!resultMap.get("errcode").equals(0)){
            throw new WeixinException("创建部门异常");
        }
    }

    /**
     * 根据主键id删除部门
     * @param id
     */
    public void delDept(Integer id){
        String url = String.format(GET_DELETE_DEPT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS),id);

        String result = sendHttpRequestGet(url);
        Map<String,Object> map = JSON.parseObject(result);
        if(!map.get("errcode").equals(0)){
            System.out.println(result);
            throw new WeixinException("删除部门异常");
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
