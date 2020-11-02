package com.kakao.kakaolink.v2.network;

import android.net.Uri;

import com.kakao.network.ServerProtocol;
import com.kakao.network.storage.ImageScrapRequest;
import com.kakao.common.IConfiguration;
import com.kakao.common.PhaseInfo;

/**
 * @author kevin.kang. Created on 2017. 3. 20..
 */

class LinkImageScrapRequest extends ImageScrapRequest {
    LinkImageScrapRequest(final PhaseInfo phaseInfo, IConfiguration configuration, final String imageUrl, final Boolean secureResource) {
        super(phaseInfo, configuration, imageUrl, secureResource);
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder();
        builder.path(ServerProtocol.LINK_IMAGE_SCRAP_PATH);
        return builder;
    }
}
