package com.kaishengit.service.exception;

/**
 *账号登录异常
 * @author 新见
 * @date 2017/11/7
 */
public class ValidateException extends RuntimeException {

    public ValidateException(){

    }

    public ValidateException(String message){
        super(message);

    }

    public ValidateException(String message,Throwable th){
        super(message,th);

    }


    public ValidateException(Throwable th){
        super(th);
    }


}
