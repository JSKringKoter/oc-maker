package com.ocmaker.server.exception;

public class RegisterFailException extends RuntimeException{
    public RegisterFailException() {
        super();
    }
    public RegisterFailException(String msg) {
        super(msg);
    }
}
