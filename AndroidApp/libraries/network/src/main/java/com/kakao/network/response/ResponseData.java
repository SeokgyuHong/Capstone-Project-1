/**
 * Copyright 2014-2015 Kakao Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.network.response;

/**
 * @author leoshin, created at 15. 8. 3..
 */
public class ResponseData {
    private final int httpStatusCode;
    private final byte[] data;

    public ResponseData(int httpStatusCode, byte[] data) {
        this.httpStatusCode = httpStatusCode;
        this.data = data;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public byte[] getData() {
        return data;
    }

    public String getStringData() {
        if (data == null) {
            return null;
        }
        return new String(data);
    }
}
