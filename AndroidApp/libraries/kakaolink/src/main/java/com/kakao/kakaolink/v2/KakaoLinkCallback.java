package com.kakao.kakaolink.v2;

import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

/**
 * @author kevin.kang. Created on 2018. 2. 2..
 */

public abstract class KakaoLinkCallback extends ResponseCallback<KakaoLinkResponse> {

    @Override
    public void onFailure(ErrorResult errorResult) {

    }

    @Override
    public void onSuccess(KakaoLinkResponse result) {

    }

    @Override
    public final void onDidStart() {
        super.onDidStart();
    }

    @Override
    public final void onDidEnd() {
        super.onDidEnd();
    }

    @Override
    public void onFailureForUiThread(ErrorResult errorResult) {
        super.onFailureForUiThread(errorResult);
    }

    @Override
    public void onSuccessForUiThread(KakaoLinkResponse result) {
        super.onSuccessForUiThread(result);
    }
}
