package com.ocmaker.server.exception;

public class NoSuchSourceException extends RuntimeException{
    public NoSuchSourceException(String msg) {
        super(msg);
    }
}