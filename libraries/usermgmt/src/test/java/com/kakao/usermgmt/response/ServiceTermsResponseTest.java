package com.kakao.usermgmt.response;

import com.kakao.test.common.KakaoTestCase;
import com.kakao.usermgmt.StringSet;
import com.kakao.usermgmt.response.model.ServiceTerms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2019-03-19..
 */
public class ServiceTermsResponseTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void nullTerms() throws JSONException {
        JSONObject object = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L);
        ServiceTermsResponse response = ServiceTermsResponse.CONVERTER.convert(object.toString());
        assertEquals(1376016924426879191L, response.getUserId().longValue());
        assertNull(response.getAllowedTerms());
    }

    @Test
    public void emptyTerms() throws JSONException {
        JSONObject object = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L)
                .put(StringSet.allowed_service_terms, new JSONArray());

        ServiceTermsResponse response = ServiceTermsResponse.CONVERTER.convert(object.toString());
        List<ServiceTerms> terms = response.getAllowedTerms();
        assertEquals(0, terms.size());
    }

    @Test
    public void twoTerms() throws JSONException {
        JSONObject object = new JSONObject()
                .put(StringSet.user_id, 1376016924426879191L)
                .put(StringSet.allowed_service_terms,
                        new JSONArray()
                                .put(new JSONObject()
                                        .put(StringSet.tag, "privacy_20190201")
                                        .put(StringSet.agreed_at, "2019-02-18T10:29:05Z"))
                                .put(new JSONObject()
                                        .put(StringSet.tag, "service_20190201")
                                        .put(StringSet.agreed_at, "2019-02-18T10:29:05Z"))
                );
        ServiceTermsResponse response = ServiceTermsResponse.CONVERTER.convert(object.toString());
        List<ServiceTerms> terms = response.getAllowedTerms();
        assertEquals(2, terms.size());

        ServiceTerms firstTerm = terms.get(0);
        assertEquals("privacy_20190201", firstTerm.getTag());
        assertEquals("2019-02-18T10:29:05Z", firstTerm.getAgreedAt());
        assertNotNull(firstTerm.getAgreedAtDate());

        ServiceTerms secondTerm = terms.get(1);
        assertEquals("service_20190201", secondTerm.getTag());
        assertEquals("2019-02-18T10:29:05Z", secondTerm.getAgreedAt());
        assertNotNull(secondTerm.getAgreedAtDate());
    }
}
