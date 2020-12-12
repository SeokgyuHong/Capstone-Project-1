package com.kakao.network.response;

/**
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public final class BlankApiResponse {
    public static final ResponseStringConverter<Boolean> CONVERTER = new ResponseStringConverter<Boolean>() {
        @Override
        public Boolean convert(String o) throws ResponseBody.ResponseBodyException {
            return true;
        }
    };
}