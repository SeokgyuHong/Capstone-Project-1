package com.kakao.usermgmt.callback;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import java.util.List;

/**
 * @author kevin.kang. Created on 2018. 4. 4..
 */
public abstract class MeV2ResponseCallback extends ApiResponseCallback<MeV2Response> {
    /**
     * {@link com.kakao.usermgmt.UserManagement#me(List, MeV2ResponseCallback)} can be called
     * even if user has not signed up. Therefore, this method is never called and is declared final
     * here to prevent developers from overriding it.
     */
    @Override
    public final void onNotSignedUp() {
    }
}
