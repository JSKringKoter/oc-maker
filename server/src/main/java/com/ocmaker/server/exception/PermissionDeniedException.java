package com.ocmaker.server.exception;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
