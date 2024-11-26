package com.ocmaker.server.exception;

public class FileDeleteFailException extends RuntimeException{
    public FileDeleteFailException(String msg) {
        super(msg);
    }
}
