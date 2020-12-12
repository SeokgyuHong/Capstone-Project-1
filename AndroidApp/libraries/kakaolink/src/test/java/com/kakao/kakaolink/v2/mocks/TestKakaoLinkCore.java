package com.kakao.kakaolink.v2.mocks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kakao.kakaolink.v2.network.KakaoLinkCore;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.IRequest;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 11. 20..
 */

public class TestKakaoLinkCore implements KakaoLinkCore {
    @Override
    public boolean isAvailable(Context context) {
        return true;
    }

    @Override
    public IRequest defaultTemplateRequest(Context context, final String appKey, TemplateParams params) {
        return null;
    }

    @Override
    public IRequest customTemplateRequest(Context context, final String appKey, String templateId, Map<String, String> templateArgs) {
        return null;
    }

    @Override
    public IRequest scrapTemplateRequest(Context context, String appKey, String url) {
        return null;
    }

    @Override
    public IRequest scrapTemplateRequest(Context context, final String appKey, String url, String templateId, Map<String, String> templateArgs) {
        return null;
    }

    @Override
    public Intent kakaoLinkIntent(Context context, String appKey, JSONObject linkResponse) {
        return null;
    }

    @Override
    public Intent kakaoLinkIntent(Context context, String appKey, JSONObject linkResponse, Map<String, String> serverCallbackArgs) throws KakaoException {
        return null;
    }

    @Override
    public Intent kakaoTalkMarketIntent(Context context) {
        return null;
    }

    @Override
    public Uri sharerUri(Context context, TemplateParams params, Map<String, String> serverCallbackArgs) {
        return new Uri.Builder().build();
    }

    @Override
    public Uri sharerUri(Context context, String templateId, Map<String, String> templateArgs, Map<String, String> serverCallbackArgs) {
        return new Uri.Builder().build();
    }

    @Override
    public Uri sharerUri(Context context, String templateId, Map<String, String> templateArgs) {
        return new Uri.Builder().build();
    }

    @Override
    public Uri sharerUri(Context context, String url, String templateId, Map<String, String> templateArgs, Map<String, String> serverCallbackArgs) {
        return new Uri.Builder().build();
    }
}
