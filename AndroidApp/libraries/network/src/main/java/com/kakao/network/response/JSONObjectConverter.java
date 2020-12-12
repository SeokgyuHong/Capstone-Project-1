package com.kakao.network.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin.kang. Created on 2018. 1. 10..
 */

public abstract class JSONObjectConverter<T> implements ResponseConverter<JSONObject, T> {
    @Override
    public JSONObject fromArray(JSONArray array, int i) {
        try {
            return array.getJSONObject(i);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public abstract T convert(JSONObject data);

    public List<T> convertList(JSONArray array) {
        List<T> result = new ArrayList<>();
        if (array == null) return result;
        for (int i = 0; i < array.length(); i++) {
            JSONObject element = fromArray(array, i);
            result.add(convert(element));
        }
        return result;
    }
}
