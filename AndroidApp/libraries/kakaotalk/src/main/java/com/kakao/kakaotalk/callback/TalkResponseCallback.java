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
package com.kakao.kakaotalk.callback;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.kakaotalk.ApiErrorCode;
import com.kakao.network.ErrorResult;

/**
 * @author leoshin, created at 15. 8. 4..
 */
public abstract class TalkResponseCallback<T> extends ApiResponseCallback<T> {
    /**
     *  카카오계정에 연결한 카카오톡 사용자가 아니여서 요청이 실패한 경우 호출된다.
     */
    public abstract void onNotKakaoTalkUser();

    @Override
    public void onFailureForUiThread(ErrorResult errorResult) {
        int result = errorResult.getErrorCode();
        if (result == ApiErrorCode.NOT_EXIST_KAKAOTALK_USER_CODE) {
            onNotKakaoTalkUser();
        } else {
            super.onFailureForUiThread(errorResult);
        }
    }
}
