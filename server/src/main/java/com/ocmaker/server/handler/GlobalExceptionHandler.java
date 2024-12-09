package com.ocmaker.server.handler;

import com.ocmaker.common.result.Result;
import com.ocmaker.server.exception.*;
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
    public ResponseEntity<?> loginFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(401);
    }

    /**
     * 用来处理请求资源不存在的方法
     * @param ex
     * @return
     */
    @ExceptionHandler(NoSuchSourceException.class)
    public ResponseEntity<?> noSuchOcExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(404);
    }

    /**
     * 用来处理请求资源权限不足的方法，如请求了不属于自己的OC
     * @param ex
     * @return
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> deniedExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(403);
    }

    /**
     * 用来处理文件上传失败的问题
     * @param ex
     * @return
     */
    @ExceptionHandler(FileUploadFailException.class)
    public ResponseEntity<?> fileUploadFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(500);
    }

    /**
     * 用来处理远程文件删除失败的问题
     * @param ex
     * @return
     */
    @ExceptionHandler(FileDeleteFailException.class)
    public ResponseEntity<?> fileDeleteFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(500);
    }

    /**
     * 图像生成失败
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(GenerateFailException.class)
    public ResponseEntity<?> imageGenerateFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(500, ex.getMessage());
    }

    @ExceptionHandler(RegisterFailException.class)
    public ResponseEntity<?> registerFailExceptionResponseEntity(Exception ex) {
        ex.printStackTrace();
        return Result.error(400, ex.getMessage());
    }


}
