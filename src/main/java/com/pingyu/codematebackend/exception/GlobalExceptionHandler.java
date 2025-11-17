package com.pingyu.codematebackend.exception; // (请确保包名正确)

import com.pingyu.codematebackend.common.BaseResponse;
import com.pingyu.codematebackend.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器 ("最高法院")
 * [策略 B 的 SOP 实现]
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 法庭 1：专门审理“业务异常” (BusinessException)
     * (处理 Service 层所有“主动”抛出的错误)
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.warn("Business Exception: code={}, message={}", e.getCode(), e.getMessage());
        // 使用 BaseResponse.error(code, message) 来返回
        return BaseResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 法庭 2：兜底审理所有“运行时异常” (RuntimeException)
     * (处理所有“被动”崩溃，如 NullPointerException)
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("Runtime Exception (System Error)", e); // 必须记录完整堆栈 e
        // 统一返回“系统错误”，隐藏内部细节
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR);
    }
}