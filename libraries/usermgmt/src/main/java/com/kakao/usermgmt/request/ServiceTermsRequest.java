package com.kakao.usermgmt.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.network.ServerProtocol;

/**
 * @author kevin.kang. Created on 2019-03-19..
 */
public class ServiceTermsRequest extends AuthorizedApiRequest {
    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        return super.getUriBuilder().path(ServerProtocol.USER_SERVICE_TERMS_PATH);
    }
}
