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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class ResponseBody {
    private JSONObject json = null;
    private int statusCode;

    @Deprecated
    public ResponseBody(int statusCode, byte[] body) throws ResponseBodyException {
        this.statusCode = statusCode;
        if (body == null) {
            throw new ResponseBodyException("Response body is null.");
        }

        if (body.length != 0) {
            try {
                this.json = new JSONObject(new String(body));
            } catch (JSONException e) {
                throw new ResponseBodyException(e);
            }
        }
    }

    @Deprecated
    public ResponseBody(int statusCode, JSONObject body) throws ResponseBodyException {
        this.statusCode = statusCode;
        if (body == null) {
            throw new ResponseBodyException();
        }
        this.json = body;
    }

    public ResponseBody(JSONObject jsonObject) throws ResponseBodyException {
        if (jsonObject == null) {
            throw new ResponseBodyException();
        }
        this.json = jsonObject;
    }

    public ResponseBody(String stringData) throws ResponseBodyException {
        try {
            this.json = new JSONObject(stringData);
        } catch (JSONException e) {
            throw new ResponseBodyException(e);
        }
    }

    private Object getOrThrow(String key) {
        Object v = null;
        try {
            v = json.get(key);
        } catch (JSONException ignored) {
        }

        if (v == null) {
            throw new NoSuchElementException(key);
        }

        if (v == JSONObject.NULL) {
            return null;
        }
        return v;
    }

    public long getLong(String key) throws ResponseBodyException {
        try {
            Object obj = getOrThrow(key);
            if (obj instanceof Integer) {
                return (Integer) obj;
            } else if (obj instanceof Long) {
                return (Long) obj;
            } else {
                throw new ResponseBodyException();
            }
        } catch (ResponseBodyException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public int getInt(String key) throws ResponseBodyException {
        try {
            return (Integer) getOrThrow(key);
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public boolean has(String key) {
        return json.has(key);
    }

    public int optInt(String key, int def) {
        if (has(key)) {
            try {
                return getInt(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public String getString(String key) throws ResponseBodyException {
        try {
            return (String) getOrThrow(key);
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public String optString(String key, String def) {
        if (has(key)) {
            try {
                return getString(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public boolean getBoolean(String key) throws ResponseBodyException {
        try {
            return (Boolean) getOrThrow(key);
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    @Deprecated
    public ResponseBodyArray getArray(String key) throws ResponseBodyException {
        try {
            return new ResponseBodyArray((JSONArray) getOrThrow(key));
        } catch (ResponseBodyException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    @Deprecated
    public ResponseBodyArray optArray(String key, ResponseBodyArray def) {
        if (has(key)) {
            try {
                return getArray(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public JSONArray getJSONArray(String key) throws ResponseBodyException {
        try {
            return (JSONArray) getOrThrow(key);
        } catch (ResponseBodyException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public JSONArray optJSONArray(String key, JSONArray def) {
        if (has(key)) {
            try {
                return getJSONArray(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public ResponseBody getBody(String key) throws ResponseBodyException {
        try {
            return new ResponseBody((JSONObject) getOrThrow(key));
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public ResponseBody optBody(String key, ResponseBody def) {
        if (has(key)) {
            try {
                return getBody(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public JSONObject getJson() {
        return json;
    }

    public boolean optBoolean(String key, boolean def) {
        if (has(key)) {
            try {
                return getBoolean(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public long optLong(String key, long def) {
        if (has(key)) {
            try {
                return getLong(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    public JSONObject getJSONObject(String key) {
        try {
            return (JSONObject) getOrThrow(key);
        } catch (ResponseBodyException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseBodyException(e);
        }
    }

    public JSONObject optJSONObject(String key, JSONObject def) {
        if (has(key)) {
            try {
                return getJSONObject(key);
            } catch (ResponseBodyException ignored) {
            }
        }
        return def;
    }

    @Override
    public String toString() {
        return json.toString();
    }

    private Iterator<String> getKeys() {
        return json == null ? null : json.keys();
    }

    public static <T> Map<String, T> toMap(ResponseBody body) throws ResponseBodyException {
        Map<String, T> map = new HashMap<String, T>();

        Iterator<String> keysItr = body.getKeys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = body.getOrThrow(key);

            if (value instanceof JSONArray) {
                value = ResponseBodyArray.toList(new ResponseBodyArray((JSONArray) value));
            } else if (value instanceof JSONObject) {
                value = toMap(new ResponseBody((JSONObject) value));
            }
            map.put(key, (T) value);
        }
        return map;
    }

    public static class ResponseBodyException extends RuntimeException {
        private static final long serialVersionUID = 8171429617556607125L;

        public ResponseBodyException() {
        }

        public ResponseBodyException(String errMsg) {
            super(errMsg);
        }

        public ResponseBodyException(Exception e) {
            super(e);
        }
    }
}
