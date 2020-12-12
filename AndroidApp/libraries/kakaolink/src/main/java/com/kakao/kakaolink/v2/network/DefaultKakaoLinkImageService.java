package com.kakao.kakaolink.v2.network;

import android.content.Context;

import com.kakao.network.IRequest;
import com.kakao.common.KakaoContextService;

import java.io.File;

/**
 * @author kevin.kang. Created on 2017. 11. 20..
 */

class DefaultKakaoLinkImageService implements KakaoLinkImageService {
    private KakaoContextService contextService;

    DefaultKakaoLinkImageService(final KakaoContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public IRequest imageUploadRequest(final Context context, File imageFile, boolean secureResource) {
        contextService.initialize(context);
        return new LinkImageUploadRequest(contextService.phaseInfo(), contextService.getAppConfiguration(), secureResource, imageFile);
    }

    @Override
    public IRequest imageScrapRequest(final Context context, String url, boolean secureResource) {
        contextService.initialize(context);
        return new LinkImageScrapRequest(contextService.phaseInfo(), contextService.getAppConfiguration(), url, secureResource);
    }

    @Override
    public IRequest imageDeleteRequestWithToken(Context context, String imageToken) {
        contextService.initialize(context);
        return new LinkImageDeleteRequest(contextService.phaseInfo(), contextService.getAppConfiguration(), null, imageToken);
    }
}
