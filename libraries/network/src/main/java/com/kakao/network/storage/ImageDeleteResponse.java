package com.kakao.network.storage;

import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

/**
 * @author kevin.kang. Created on 2017. 3. 20..
 */

public class ImageDeleteResponse {
    ImageDeleteResponse() {
    }
    public static final ResponseStringConverter<ImageDeleteResponse>  CONVERTER = new ResponseStringConverter<ImageDeleteResponse>() {
        @Override
        public ImageDeleteResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ImageDeleteResponse();
        }
    };
}
