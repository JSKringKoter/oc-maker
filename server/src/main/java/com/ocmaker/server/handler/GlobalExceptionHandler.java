package com.ocmaker.server.handler;

import com.ocmaker.common.result.Result;
import com.ocmaker.server.exception.FileUploadFailException;
import com.ocmaker.server.exception.LoginFailException;
import com.ocmaker.server.exception.NoSuchSourceException;
import com.ocmaker.server.exception.PermissionDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 用来处理登陆异常的方法
     * @param ex
     * @return
     */
    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<LoginFailException> loginFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(401);
    }

    /**
     * 用来处理请求资源不存在的方法
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchSourceException.class)
    public ResponseEntity<NoSuchSourceException> noSuchOcExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(404);
    }

    /**
     * 用来处理请求资源权限不足的方法，如请求了不属于自己的OC
     * @param ex
     * @return
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<PermissionDeniedException> deniedExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(403);
    }

    @ExceptionHandler(FileUploadFailException.class)
    public ResponseEntity<FileUploadFailException> fileUploadFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();;
        return Result.error(500);
    }

}
