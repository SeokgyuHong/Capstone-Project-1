/*
  Copyright 2014-2018 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.network.response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ResponseBodyArray {
    private final JSONArray jsonArray;
    private int statusCode;

    @Deprecated
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @deprecated Use {@link ResponseBodyArray#ResponseBodyArray(JSONArray)} instead
     */
    @Deprecated
    public ResponseBodyArray(int statusCode, byte[] body) throws ResponseBody.ResponseBodyException {
        this.statusCode = statusCode;
        if (body == null) {
            throw new ResponseBody.ResponseBodyException();
        }

        try {
            this.jsonArray = new JSONArray(new String(body));
        } catch (JSONException e) {
           throw new ResponseBody.ResponseBodyException(e);
        }
    }

    /**
     * @deprecated Use {@link ResponseBodyArray#ResponseBodyArray(JSONArray)} instead
     */
    @Deprecated
    public ResponseBodyArray(int statusCode, JSONArray jsonArray) throws ResponseBody.ResponseBodyException {
        this.statusCode = statusCode;
        if (jsonArray == null) {
            throw new ResponseBody.ResponseBodyException();
        }
        this.jsonArray = jsonArray;
    }

    public ResponseBodyArray(JSONArray jsonArray) throws ResponseBody.ResponseBodyException {
        if (jsonArray == null) {
            throw new ResponseBody.ResponseBodyException("Cannot instantiate ResponseBodyArray with null json array.");
        }
        this.jsonArray = jsonArray;
    }

    public int length() {
        return jsonArray.length();
    }

    public long getLong(int i) throws ResponseBody.ResponseBodyException {
        try {
            Object obj = getOrThrow(i);
            if (obj instanceof Integer) {
                return (Integer) obj;
            }
            else if (obj instanceof Long) {
                return (Long) obj;
            }
            else {
                throw new ResponseBody.ResponseBodyException();
            }
        }
        catch(Exception e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
    }

    public String getString(int i) throws ResponseBody.ResponseBodyException {
        try {
            return (String) getOrThrow(i);
        }
        catch(Exception e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
    }

    public int getInt(int i) throws ResponseBody.ResponseBodyException {
        try {
            return (Integer) getOrThrow(i);
        }
        catch(Exception e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
    }
    
    public Boolean getBoolean(int i) throws ResponseBody.ResponseBodyException {
        try {
            return (Boolean) getOrThrow(i);
        }
        catch(Exception e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
    }

    public ResponseBody getBody(int i) throws ResponseBody.ResponseBodyException {
        try {
            return new ResponseBody((JSONObject) getOrThrow(i));
        }
        catch(ResponseBody.ResponseBodyException e) {
            throw e;
        }
        catch(Exception e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
    }

    private Object getOrThrow(int index) {
        Object v = null;
        try {
            v = jsonArray.get(index);
        } catch (JSONException ignore) {
        }

        if (v == null) {
            throw new NoSuchElementException();
        }
        return v;
    }

    public static <T> List<T> toList(ResponseBodyArray array) throws ResponseBody.ResponseBodyException {
        List<T> list = new ArrayList<T>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.getOrThrow(i);
            if(value instanceof JSONArray) {
                value = toList(new ResponseBodyArray((JSONArray) value));
            }

            else if(value instanceof JSONObject) {
                value = ResponseBody.toMap(new ResponseBody((JSONObject) value));
            }
            list.add((T)value);
        }
        return list;
    }

    @Override
    public String toString() {
        return jsonArray.toString();
    }
}
