package com.kakao.network.response;

import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin.kang. Created on 2017. 12. 6..
 */

public abstract class ResponseStringConverter<T> implements ResponseConverter<String, T> {
    @Override
    public String fromArray(JSONArray array, int i) {
        try {
            return array.getString(i);
        } catch (JSONException e) {
            return null;
        }
    }

    public List<T> convertList(String data) {
        List<T> result = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            return convertList(array);
        } catch (JSONException e) {
            Logger.w(e.toString());
        }
        return result;
    }

    public List<T> convertList(JSONArray array) {
        List<T> result = new ArrayList<>();
        if (array == null) return result;
        for (int i = 0; i < array.length(); i++) {
            String element = fromArray(array, i);
            result.add(convert(element));
        }
        return result;
    }

    public static final ResponseStringConverter<String> IDENTITY_CONVERTER = new ResponseStringConverter<String>() {
        @Override
        public String convert(String data) {
            return data;
        }
    };

    @SuppressWarnings("unused")
    public static final ResponseStringConverter<Long> LONG_CONVERTER = new ResponseStringConverter<Long>() {
        @Override
        public Long convert(String o) throws ResponseBody.ResponseBodyException {
            return Long.valueOf(o);
        }
    };
}
