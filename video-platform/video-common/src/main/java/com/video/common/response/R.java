package com.video.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    /**
     * 成功
     */
    public static <T> R<T> ok() {
        return restResult(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    
    /**
     * 成功，带数据
     */
    public static <T> R<T> ok(T data) {
        return restResult(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    
    /**
     * 成功，带消息
     */
    public static <T> R<T> ok(String message) {
        return restResult(null, ResultCode.SUCCESS.getCode(), message);
    }
    
    /**
     * 成功，带数据和消息
     */
    public static <T> R<T> ok(T data, String message) {
        return restResult(data, ResultCode.SUCCESS.getCode(), message);
    }
    
    /**
     * 失败
     */
    public static <T> R<T> error() {
        return restResult(null, ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage());
    }
    
    /**
     * 失败，带消息
     */
    public static <T> R<T> error(String message) {
        return restResult(null, ResultCode.ERROR.getCode(), message);
    }
    
    /**
     * 失败，带状态码和消息
     */
    public static <T> R<T> error(Integer code, String message) {
        return restResult(null, code, message);
    }
    
    /**
     * 构造结果
     */
    private static <T> R<T> restResult(T data, Integer code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        return r;
    }
} 