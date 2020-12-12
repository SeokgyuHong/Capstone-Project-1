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
package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

/**
 * 톡프로필 API {@link com.kakao.kakaotalk.v2.KakaoTalkService#requestProfile(TalkResponseCallback, boolean)}
 * 의 응답 클래스.
 *
 * Response class for {@link com.kakao.kakaotalk.v2.KakaoTalkService#requestProfile(TalkResponseCallback, boolean)} API.
 *
 * @author leoshin, created at 15. 7. 27..
 */
public class TalkProfileResponse extends JSONObjectResponse {
    final private KakaoTalkProfile profile;
    public TalkProfileResponse(String stringData) {
        super(stringData);
        this.profile = new KakaoTalkProfile(getBody());
    }

    public KakaoTalkProfile getProfile() {
        return profile;
    }

    public static final ResponseStringConverter<KakaoTalkProfile> CONVERTER = new ResponseStringConverter<KakaoTalkProfile>() {
        @Override
        public KakaoTalkProfile convert(String o) throws ResponseBody.ResponseBodyException {
            return new TalkProfileResponse(o).getProfile();
        }
    };
}
