package com.example.mall.common.exception;

import com.example.mall.common.api.IErrorCode;

// 这里用的不是真的assert，而是抛出错误
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
