package com.kakao.network.response;

import com.kakao.network.exception.ResponseStatusError;

/**
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public class ApiResponseStatusError extends ResponseStatusError {
    private static final long serialVersionUID = 3702596857996303483L;
    private final int errorCode;
    private final String errorMsg;
    private final int httpStatusCode;
    private ResponseBody errorResponse;

    public ApiResponseStatusError(int errorCode, String errorMsg, int httpStatusCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponseStatusError(int errorCode, String errorMsg, int httpStatusCode, ResponseBody errorResponse) {
        this(errorCode, errorMsg, httpStatusCode);
        this.errorResponse = errorResponse;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public ResponseBody getErrorResponse() {
        return errorResponse;
    }
}