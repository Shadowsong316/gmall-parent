package com.atguigu.gmall.admin.config;

import com.atguigu.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 处理系统所有的异常
 */
@Slf4j
@RestControllerAdvice//这是一个统一异常处理类
public class GmallGlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public Object arithmeticException(ArithmeticException e){
        log.error("全局异常处理类感知到异常");
        return new CommonResult().failed().validateFailed(e.getMessage());
    }
}
