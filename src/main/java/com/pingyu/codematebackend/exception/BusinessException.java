package com.pingyu.codematebackend.exception; // (请确保包名正确)

import com.pingyu.codematebackend.common.ErrorCode;

/**
 * 自定义业务异常
 * (用于在 Service 层“主动”抛出，替代“主动”返回 error)
 */
public class BusinessException extends RuntimeException {

    private final int code;

    /**
     * 构造函数 (核心)
     * @param errorCode 我们定义的错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 把“消息”传给父类 RuntimeException
        this.code = errorCode.getCode();
    }

    /**
     * 构造函数 (允许自定义消息)
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message); // 使用自定义消息
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}