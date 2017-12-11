package com.kaishengit.web;

import com.sun.org.apache.regexp.internal.RE;

/**
 * Ajax请求的返回结果
 * Created by User on 2017/11/8.
 */
public class ResultJson {

    private String state;
    private String message;
    private Object object;


    public static ResultJson success(){
        ResultJson resultJson = new ResultJson();
        resultJson.setState("success");
        return resultJson;
    }

    public static ResultJson successWithData(Object object){
        ResultJson resultJson = new ResultJson();
        resultJson.setState("success");
        resultJson.setObject(object);
        return resultJson;

    }

    public static ResultJson error(String message){
        ResultJson resultJson = new ResultJson();
        resultJson.setState("error");
        resultJson.setMessage(message);
        return resultJson;
    }

    public ResultJson(){

    }

    public ResultJson(String state){
        this.state = state;
    }

    public ResultJson(String state, String message){
        this.state = state;
        this.message = message;
    }

    public ResultJson(String state, Object object){
        this.state = state;
        this.object = object;
    }

    public ResultJson(String state, String message, Object object){
        this.state = state;
        this.message = message;
        this.object = object;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
