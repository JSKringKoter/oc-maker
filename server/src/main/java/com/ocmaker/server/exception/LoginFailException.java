package com.ocmaker.server.exception;


public class LoginFailException extends RuntimeException{
    public LoginFailException(String msg) {
        super(msg);
    }
}
