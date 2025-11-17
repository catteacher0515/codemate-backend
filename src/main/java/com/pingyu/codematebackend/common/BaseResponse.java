package com.pingyu.codematebackend.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用返回类 (Unified Response Body)
 * [已重构 - 策略 B]
 * 1. 采用静态工厂方法 (不再需要 ResultUtils)
 * 2. 错误处理基于 ErrorCode 枚举
 * * @param <T> 响应数据的类型
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码 (来自 ErrorCode.getCode())
     */
    private int code;

    /**
     * 响应数据 (泛型)
     */
    private T data;

    /**
     * 响应消息 (来自 ErrorCode.getMessage())
     */
    private String message;

    /**
     * 构造函数私有化，强制使用静态工厂方法
     */
    private BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    // --- 静态工厂方法 (成功) ---

    /**
     * 创建成功的响应 (携带数据)
     * @param data 成功时携带的数据
     * @param <T>  数据类型
     * @return 成功的 BaseResponse 实例
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    /**
     * 创建成功的响应 (不携带数据, e.g., 删除成功)
     * @return 成功的 BaseResponse 实例
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), null, ErrorCode.SUCCESS.getMessage());
    }

    // --- 静态工厂方法 (失败) ---

    /**
     * 创建失败的响应 (根据 ErrorCode 枚举)
     * @param errorCode 错误码枚举实例
     * @return 失败的 BaseResponse 实例
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    /**
     * 创建失败的响应 (根据 ErrorCode 和自定义错误消息)
     * @param errorCode 错误码枚举实例
     * @param message   自定义错误消息 (覆盖 ErrorCode 的默认 message)
     * @return 失败的 BaseResponse 实例
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

    /**
     * 创建失败的响应 (根据 自定义 code 和 自定义错误消息)
     * (这个方法用于兼容旧的 ResultUtils.error(code, message)，但不推荐常规使用)
     * @param code    自定义错误码
     * @param message 自定义错误消息
     * @return 失败的 BaseResponse 实例
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }
}