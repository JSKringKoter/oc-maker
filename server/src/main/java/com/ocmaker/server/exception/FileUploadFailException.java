package com.ocmaker.server.exception;

public class FileUploadFailException extends RuntimeException{
    public FileUploadFailException(String msg) {
        super(msg);
    }
}
