package com.kakao.usermgmt.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.usermgmt.StringSet;

import org.json.JSONArray;

import java.util.List;

/**
 * @author kevin.kang. Created on 2018. 4. 4..
 */
public class MeV2Request extends AuthorizedApiRequest {
    private final List<String> propertyKeyList;
    private final boolean secureResource;

    public MeV2Request(List<String> propertyKeyList, boolean secureResource) {
        this.propertyKeyList = propertyKeyList;
        this.secureResource = secureResource;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.USER_ME_V2_PATH)
                .appendQueryParameter(StringSet.secure_resource, String.valueOf(secureResource));
        if (propertyKeyList != null && propertyKeyList.size() > 0) {
            builder.appendQueryParameter(StringSet.property_keys, new JSONArray(propertyKeyList).toString());
        }
        return builder;
    }
}
