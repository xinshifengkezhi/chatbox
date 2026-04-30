package com.luoxue.account_data.controller.utrrls;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectException {

    // 处理重复键异常（如账号已存在）
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  // 返回 409 状态码
    public void handleDuplicateKey(DuplicateKeyException e) {
        e.printStackTrace();
    }

    // 处理其他未预期的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 返回 500
    public void doException(Exception e) {
        e.printStackTrace();
    }
}
