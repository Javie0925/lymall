package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice  // 拦截加了controller注解的类
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)    //拦截不同异常实现异常分类处理
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        return ResponseEntity.status(e.getExceptionEnmu().getCode()).body(
                new ExceptionResult(e.getExceptionEnmu()));
    }
}
