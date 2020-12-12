package com.kakao.usermgmt.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.usermgmt.StringSet;

/**
 * @author kevin.kang. Created on 2019-03-26..
 */
public class ShippingAddressesRequest extends AuthorizedApiRequest {
    private final Long addressId;
    private final Integer fromUpdatedAt;
    private final Integer pageSize;

    public ShippingAddressesRequest(final Long addressId, final Integer fromUpdatedAt, final Integer pageSize) {
        this.addressId = addressId;
        this.fromUpdatedAt = fromUpdatedAt;
        this.pageSize = pageSize;
    }

    @Override
    public String getMethod() {
        return GET;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().path(ServerProtocol.USER_SHIPPING_ADDRESS_PATH);
        if (addressId != null) builder.appendQueryParameter(StringSet.address_id, addressId.toString());
        if (fromUpdatedAt != null) builder.appendQueryParameter(StringSet.from_updated_at, fromUpdatedAt.toString());
        if (pageSize != null) builder.appendQueryParameter(StringSet.page_size, pageSize.toString());
        return builder;
    }
}
