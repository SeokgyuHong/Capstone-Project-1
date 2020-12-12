package com.kakao.usermgmt.response.model;

import com.kakao.network.response.JSONObjectConverter;
import com.kakao.usermgmt.StringSet;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 유저의 개별 동의항목 동의 내역.
 *
 * @author kevin.kang. Created on 2019-03-19..
 */
public class ServiceTerms {
    private final String tag;
    private final String agreedAt;

    private ServiceTerms(JSONObject body) {
        tag = body.optString(StringSet.tag, null);
        agreedAt = body.optString(StringSet.agreed_at, null);
    }

    public static final JSONObjectConverter<ServiceTerms> CONVERTER = new JSONObjectConverter<ServiceTerms>() {
        @Override
        public ServiceTerms convert(JSONObject data) {
            return new ServiceTerms(data);
        }
    };

    /**
     * 동의한 약관의 tag. 3rd 에서 설정한 값
     */
    public String getTag() {
        return tag;
    }

    /**
     * 동의한 시간. 약관이 여러번 뜨는 구조라면, 마지막으로 동의한 시간.
     * RFC3339 internet date/time format (yyyy-mm-dd'T'HH:mm:ss'Z', yyyy-mm-dd'T'HH:mm:ss'+'HH:mm, yyyy-mm-dd'T'HH:mm:ss'-'HH:mm 가능)
     *
     * @return last time this tag was agreed at in UTC time
     */
    public String getAgreedAt() {
        return agreedAt;
    }

    /**
     * 약관에 마지막으로 동의한 시간을 Date 형태로 반환한다.
     *
     * @return Date instance in local time zone.
     */
    public Date getAgreedAtDate() {
        if (agreedAt == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(agreedAt);
        } catch (ParseException e) {
            Logger.w("Failed to parse ServiceTerms agreedAt field: ", agreedAt);
            return null;
        }
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put(StringSet.tag, tag).put(StringSet.agreed_at, agreedAt).toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
