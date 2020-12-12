package com.kakao.network.response;

/**
 * @author kevin.kang. Created on 2018. 1. 18..
 */

public interface CustomErrorConverter<T> {
    T convert(int httpStatusCode, String errorResponse);
}
