package com.example.mall.common.exception;

import com.example.mall.common.api.IErrorCode;

// ApiException -> RuntimeException -> Exception -> Throwable(有message, cause等）
public class ApiException extends RuntimeException{
    private IErrorCode errorCode;
    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) { super(message);}

    public ApiException(Throwable cause) { super(cause);}

    public ApiException(String message, Throwable cause) {super(message, cause);}

    public IErrorCode getErrorCode() {return errorCode;}
}
