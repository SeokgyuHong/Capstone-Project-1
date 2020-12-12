/*
  Copyright 2014-2017 Kakao Corp.

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
package com.kakao.usermgmt.response;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.usermgmt.StringSet;

/**
 * @author leoshin, created at 15. 8. 6..
 */
public class UserResponse extends JSONObjectResponse {
    private final long userId;

    UserResponse(String stringData) throws ResponseBody.ResponseBodyException {
        super(stringData);
        this.userId = getBody().getLong(StringSet.id);
        if (userId <= 0) {
            throw new ResponseBody.ResponseBodyException("User is called but the result user is null.");
        }
    }

    public long getUserId() {
        return userId;
    }

    public static final ResponseStringConverter<Long> CONVERTER =  new ResponseStringConverter<Long>() {

        @Override
        public Long convert(String o) throws ResponseBody.ResponseBodyException {
            return new UserResponse(o).getUserId();
        }
    };
}
