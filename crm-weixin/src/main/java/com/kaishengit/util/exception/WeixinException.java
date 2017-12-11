package com.kaishengit.util.exception;

/**
 * Created by User on 2017/11/21.
 */
public class WeixinException extends RuntimeException {

    public WeixinException (){}

    public WeixinException (String message){
        super(message);
    }
}
