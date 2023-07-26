package com.example.gulimall.product.exception;

import com.example.common.exception.BizCodeEnume;
import com.example.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
集中处理所有异常
 */
/*
以下二者可以用RestControllerAdvice代替
@ResponseBody
@ControllerAdvice(basePackages = "com.example.gulimall.product.controller")
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
public R handleValidException(MethodArgumentNotValidException e){
    log.error("数据校验出现问题{},异常类型:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> errorMap=new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(),BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data",errorMap);
    }
    //如果不能精确匹配，来到下面的统一处理，可以处理任意类型的异常
    @ExceptionHandler(value=Throwable.class)
    public R handleException(Throwable throwable){
        log.error("错误",throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
