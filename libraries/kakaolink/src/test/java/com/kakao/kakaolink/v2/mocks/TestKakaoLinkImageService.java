package com.kakao.kakaolink.v2.mocks;

import android.content.Context;

import com.kakao.kakaolink.v2.network.KakaoLinkImageService;
import com.kakao.network.IRequest;

import java.io.File;

/**
 * @author kevin.kang. Created on 2017. 11. 20..
 */

public class TestKakaoLinkImageService implements KakaoLinkImageService {
    @Override
    public IRequest imageUploadRequest(Context context, File imageFile, boolean secureResource) {
        return null;
    }

    @Override
    public IRequest imageScrapRequest(Context context, String url, boolean secureResource) {
        return null;
    }

    @Override
    public IRequest imageDeleteRequestWithToken(Context context, String imageToken) {
        return null;
    }
}
