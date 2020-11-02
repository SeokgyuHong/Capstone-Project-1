package com.kakao.test.common;


import androidx.annotation.NonNull;

import com.kakao.common.KakaoPhase;
import com.kakao.common.PhaseInfo;

public class TestPhaseInfo implements PhaseInfo {
    @NonNull
    @Override
    public KakaoPhase phase() {
        return KakaoPhase.PRODUCTION;
    }

    @Override
    public String appKey() {
        return "sample_app_key";
    }

    @Override
    public String clientSecret() {
        return null;
    }
}
