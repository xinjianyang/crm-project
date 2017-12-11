package com.kaishengit.service.exception;

/**
 * Created by User on 2017/11/8.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(){

    }

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Throwable th){
        super(message, th);
    }


    public ServiceException(Throwable throwable){
        super(throwable);
    }
}
