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
package com.kakao.kakaostory.response;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

/**
 * @author leoshin, created at 15. 7. 31..
 */
public class StoryPostResponse extends JSONObjectResponse {
    private final MyStoryInfo myStoryInfo;

    public StoryPostResponse(String stringData) throws ResponseBody.ResponseBodyException {
        super(stringData);
        this.myStoryInfo = new MyStoryInfo(stringData);
    }

    public MyStoryInfo getMyStoryInfo() {
        return myStoryInfo;
    }

    public static final ResponseStringConverter<MyStoryInfo> CONVERTER = new ResponseStringConverter<MyStoryInfo>() {
        @Override
        public MyStoryInfo convert(String o) {
            return new StoryPostResponse(o).getMyStoryInfo();
        }
    };
}
