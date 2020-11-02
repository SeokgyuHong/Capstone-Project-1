package com.kakao.kakaolink.v2.network;

import android.content.Context;

import com.kakao.network.IRequest;
import com.kakao.common.KakaoContextService;

import java.io.File;

/**
 * @author kevin.kang. Created on 2017. 11. 20..
 */

public interface KakaoLinkImageService {
    /**
     * @param context
     * @param imageFile
     * @param secureResource
     * @return
     */
    IRequest imageUploadRequest(final Context context, final File imageFile, final boolean secureResource);

    /**
     * @param context
     * @param url
     * @param secureResource
     * @return
     */
    IRequest imageScrapRequest(final Context context, final String url, final boolean secureResource);

    /**
     * @param context
     * @param imageToken
     * @return
     */
    IRequest imageDeleteRequestWithToken(final Context context, final String imageToken);

    class Factory {
        public static KakaoLinkImageService getInstance() {
            KakaoContextService contextService = KakaoContextService.getInstance();
            return new DefaultKakaoLinkImageService(contextService);
        }
    }
}
