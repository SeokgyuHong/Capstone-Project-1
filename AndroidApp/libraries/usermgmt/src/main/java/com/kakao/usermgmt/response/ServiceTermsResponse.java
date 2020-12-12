package com.kakao.usermgmt.response;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.usermgmt.StringSet;
import com.kakao.usermgmt.response.model.ServiceTerms;

import java.util.List;

/**
 * 3rd 의 동의항목 내역 응답 모델.
 *
 * @author kevin.kang. Created on 2019-03-19..
 */
public class ServiceTermsResponse extends JSONObjectResponse {
    private final Long userId;
    private final List<ServiceTerms> allowedTerms;

    private ServiceTermsResponse(String stringData) {
        super(stringData);
        userId = getBody().has(StringSet.user_id) ? getBody().getLong(StringSet.user_id) : null;
        allowedTerms = getBody().has(StringSet.allowed_service_terms) ?
                ServiceTerms.CONVERTER.convertList(getBody().getJSONArray(StringSet.allowed_service_terms)) : null;
    }

    public static final ResponseStringConverter<ServiceTermsResponse> CONVERTER = new ResponseStringConverter<ServiceTermsResponse>() {
        @Override
        public ServiceTermsResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ServiceTermsResponse(o);
        }
    };

    /**
     * app user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 사용자가 동의한 3rd의 약관 항목들
     *
     * @return third party's service terms that this user agreed to
     */
    public List<ServiceTerms> getAllowedTerms() {
        return allowedTerms;
    }
}
