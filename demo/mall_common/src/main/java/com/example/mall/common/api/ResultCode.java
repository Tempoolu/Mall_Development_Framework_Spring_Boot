package com.example.mall.common.api;

public enum ResultCode implements IErrorCode{

    SUCCESS(200, "Operation successful"),
    FAILED(500, "Operation failed"),
    VALIDATE_FAILED(404, "Parameter verification failed"),
    UNAUTHORIZED(401, "Not logged or token expired"),
    FORBIDDEN(403, "Permission denied");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
