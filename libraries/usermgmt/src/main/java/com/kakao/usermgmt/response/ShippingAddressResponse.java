package com.kakao.usermgmt.response;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.usermgmt.StringSet;
import com.kakao.usermgmt.response.model.ShippingAddress;
import com.kakao.util.OptionalBoolean;

import java.util.List;

/**
 * /v1/user/shipping_address 응담 모델.
 *
 * @author kevin.kang. Created on 2019-03-26..
 */
public class ShippingAddressResponse extends JSONObjectResponse {
    private final Long userId;
    private final OptionalBoolean hasShippingAddresses;
    private final OptionalBoolean shippingAddressesNeedsAgreement;
    private final List<ShippingAddress> shippingAddresses;

    public static final ResponseStringConverter<ShippingAddressResponse> CONVERTER = new ResponseStringConverter<ShippingAddressResponse>() {
        @Override
        public ShippingAddressResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ShippingAddressResponse(o);
    }
};

    private ShippingAddressResponse(String o) {
        super(o);
        userId = getBody().has(StringSet.user_id) ? getBody().getLong(StringSet.user_id) : null;
        hasShippingAddresses = getBody().has(StringSet.has_shipping_addresses) ?
                OptionalBoolean.getOptionalBoolean(getBody().getBoolean(StringSet.has_shipping_addresses)) :
                null;
        shippingAddressesNeedsAgreement = getBody().has(StringSet.shipping_addresses_needs_agreement) ?
                OptionalBoolean.getOptionalBoolean(getBody().getBoolean(StringSet.shipping_addresses_needs_agreement)) :
                null;
        shippingAddresses = getBody().has(StringSet.shipping_addresses) ?
                ShippingAddress.CONVERTER.convertList(getBody().getJSONArray(StringSet.shipping_addresses)) :
                null;
    }

    /**
     * 배송지 정보를 요청한 사용자 아이디(ID)
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 배송지 정보를 소유하고 있는 여부.
     * 해당 값이 false일때는 배송지를 서비스에서 자체 수집하여야 하고, 해당 값이 true 인데 shipping_addresses 값이 내려오지 않는 경우는 사용자가 배송지 제공에 대한 제3자 정보제공동의를 하지 않은 경우이므로 배송지에 대한 동적 동의창을 요청해야함
     */
    public OptionalBoolean hasShippingAddresses() {
        return hasShippingAddresses;
    }

    /**
     * 사용자의 배송지 정보 리스트. 최신 수정순 (단, 기본 배송지는 수정시각과 상관없이 첫번째에 위치)
     */
    public List<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    /**
     * 배송지 정보 조회를 위하여 유저에게 제3자 정보제공동의를 받아야 하는지 여부
     */
    public OptionalBoolean shippingAddressesNeedsAgreement() {
        return shippingAddressesNeedsAgreement;
    }
}
