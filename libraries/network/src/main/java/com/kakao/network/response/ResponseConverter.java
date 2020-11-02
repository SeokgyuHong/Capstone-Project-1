package com.kakao.network.response;

import org.json.JSONArray;

/**
 * @author kevin.kang. Created on 2017. 12. 5..
 */

public interface ResponseConverter<F, T> {
    F fromArray(JSONArray array, int i) throws ResponseBody.ResponseBodyException;
    T convert(F o) throws ResponseBody.ResponseBodyException;
}
