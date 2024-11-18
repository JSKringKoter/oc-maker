package com.ocmaker.server.handler;

import com.ocmaker.common.result.Result;
import com.ocmaker.server.exception.LoginFailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<LoginFailException> ex(Exception ex) {
        ex.printStackTrace();
        return Result.error(401);
    }
}
