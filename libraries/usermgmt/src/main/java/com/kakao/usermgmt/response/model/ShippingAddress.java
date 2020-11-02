package com.kakao.usermgmt.response.model;

import androidx.annotation.NonNull;

import com.kakao.network.response.JSONObjectConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.usermgmt.StringSet;
import com.kakao.util.OptionalBoolean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 배송지 정보
 *
 * @author kevin.kang. Created on 2019-03-26..
 */
public class ShippingAddress {
    private final Long id;
    private final String name;
    private final OptionalBoolean isDefault;
    private final Integer updatedAt;
    private final String type;

    private final String baseAddress;
    private final String detailAddress;
    private final String receiverName;
    private final String receiverPhoneNumber1;
    private final String receiverPhoneNumber2;
    private final String zoneNumber;
    private final String zipCode;

    public static final JSONObjectConverter<ShippingAddress> CONVERTER = new JSONObjectConverter<ShippingAddress>() {
        @Override
        public ShippingAddress convert(JSONObject data) {
            return new ShippingAddress(data);
        }
    };

    private ShippingAddress(@NonNull JSONObject data) {
        try {
            id = data.has(StringSet.id) ? data.getLong(StringSet.id) : null;
            isDefault = data.has(StringSet.is_default) ?
                    OptionalBoolean.getOptionalBoolean(data.getBoolean(StringSet.is_default)) : null;
            updatedAt = data.has(StringSet.updated_at) ? data.getInt(StringSet.updated_at) : null;
        } catch (JSONException e) {
            throw new ResponseBody.ResponseBodyException(e);
        }
        name = data.optString(StringSet.name, null);
        type = data.optString(StringSet.type, null);
        baseAddress = data.optString(StringSet.base_address);
        detailAddress = data.optString(StringSet.detail_address);
        receiverName = data.optString(StringSet.receiver_name);
        receiverPhoneNumber1 = data.optString(StringSet.receiver_phone_number1);
        receiverPhoneNumber2 = data.optString(StringSet.receiver_phone_number2);
        zoneNumber = data.optString(StringSet.zone_number);
        zipCode = data.optString(StringSet.zip_code);
    }

    /**
     * 배송지 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 배송지
     */
    public String getName() {
        return name;
    }

    /**
     * 기본 배송지 여부
     */
    public OptionalBoolean isDefault() {
        return isDefault;
    }

    /**
     * 수정시각의 timestamp
     */
    public Integer getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 배송지 타입.구주소(지번,번지 주소) 또는 신주소(도로명 주소)
     *
     * @return "OLD" or "NEW"
     */
    public String getType() {
        return type;
    }

    /**
     * 우편번호 검색시 채워지는 기본 주소
     */
    public String getBaseAddress() {
        return baseAddress;
    }

    /**
     * 기본 주소에 추가하는 상세 주소
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * (Optional) 수령인 이름
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * (Optional) 수령인 연락처
     */
    public String getReceiverPhoneNumber1() {
        return receiverPhoneNumber1;
    }

    /**
     * (Optional) 수령인 추가 연락처
     */
    public String getReceiverPhoneNumber2() {
        return receiverPhoneNumber2;
    }

    /**
     * (Optional) 신주소 우편번호. 신주소인 경우에 반드시 존재함.
     */
    public String getZoneNumber() {
        return zoneNumber;
    }

    /**
     * (Optional) 구주소 우편번호. 우편번호를 소유하지 않는 구주소도 존재하여, 구주소인 경우도 해당값이 없을 수 있음.
     */
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject().put(StringSet.id, id).put(StringSet.name, name)
                    .put(StringSet.is_default, isDefault == null ? null : isDefault.getBoolean())
                    .put(StringSet.updated_at, updatedAt).put(StringSet.type, type)
                    .put(StringSet.base_address, baseAddress)
                    .put(StringSet.detail_address, detailAddress)
                    .put(StringSet.receiver_name, receiverName)
                    .put(StringSet.receiver_phone_number1, receiverPhoneNumber1)
                    .put(StringSet.receiver_phone_number2, receiverPhoneNumber2)
                    .put(StringSet.zone_number, zoneNumber)
                    .put(StringSet.zip_code, zipCode).toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
