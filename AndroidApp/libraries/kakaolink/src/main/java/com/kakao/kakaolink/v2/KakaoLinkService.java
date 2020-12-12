/*
  Copyright 2016-2019 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.kakaolink.v2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsService;
import androidx.browser.customtabs.CustomTabsServiceConnection;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.v2.network.KakaoLinkCore;
import com.kakao.kakaolink.v2.network.KakaoLinkImageService;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.IRequest;
import com.kakao.network.NetworkService;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.network.storage.ImageDeleteResponse;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.helper.AbstractFuture;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Class for sending KakaoTalk messages using KakaoLink v2 API.
 * <p>
 * 카카오링크 v2를 사용하기 위한 클래스.
 *
 * @author kevin.kang
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkService {

    private static KakaoLinkService instance =
            new KakaoLinkService(
                    KakaoLinkCore.Factory.getInstance(),
                    KakaoLinkImageService.Factory.getInstance(),
                    NetworkService.Factory.getInstance()
            );

    private KakaoLinkCore linkCore;
    private KakaoLinkImageService imageService;
    private NetworkService networkService;

    KakaoLinkService(KakaoLinkCore linkCore, KakaoLinkImageService imageService, NetworkService networkService) {
        this.linkCore = linkCore;
        this.imageService = imageService;
        this.networkService = networkService;
    }

    /**
     * Returns a singleton instance for KakaoLink v2 API.
     * 이 클래스의 싱글턴 인스턴스를 리턴한다.
     *
     * @return a singleton instance of KakaoLinkService class
     */
    public static KakaoLinkService getInstance() {
        return instance;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isKakaoLinkV2Available(final Context context) {
        return linkCore.isAvailable(context);
    }

    /**
     * Send KakaoLink v2 message with custom templates. Template id and arguments should be provided.
     * <p>
     * 커스텀 템플릿을 사용하여 카카오링크 v2 메시지를 보냄.
     *
     * @param context      Context to start an activity for KakaoLink
     * @param templateId   id of the custom template created in developer website
     * @param templateArgs template arguments to fill in the custom template
     * @param callback     success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    @SuppressWarnings("WeakerAccess")
    public void sendCustom(final Context context,
                           final String templateId,
                           final Map<String, String> templateArgs,
                           final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.customTemplateRequest(context, null, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, templateId, templateArgs, null);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, null, callback);
    }

    /**
     * Send KakaoLink v2 message with custom templates. Template id and arguments should be provided.
     * <p>
     * 커스텀 템플릿을 사용하여 카카오링크 v2 메시지를 보냄. 서버 콜백에 넘길 값들을 지정할 수 있다.
     *
     * @param context            Context to start an activity for KakaoLink
     * @param templateId         id of the custom template created in developer website
     * @param templateArgs       template arguments to fill in the custom template
     * @param serverCallbackArgs arguments for server callback
     * @param callback           success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     * @since 1.13.0
     */
    public void sendCustom(final Context context,
                           final String templateId,
                           final Map<String, String> templateArgs,
                           final Map<String, String> serverCallbackArgs,
                           final ResponseCallback<KakaoLinkResponse> callback) {
        // App key, key hash, KA header 등을 준비한다.
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.customTemplateRequest(context, null, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, templateId, templateArgs, serverCallbackArgs);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, serverCallbackArgs, callback);
    }

    /**
     * Send KakaoLink v2 message with default templates.
     * <p>
     * 디폴트 템플릿을 사용하여 카카오링크 v2 메시지를 보낸다. 별도의 템플릿 설정이 필요 없으며 아래의 템플릿 타입들을 지원한다.
     * <p>
     * - {@link com.kakao.message.template.FeedTemplate}
     * - {@link com.kakao.message.template.ListTemplate}
     * - {@link com.kakao.message.template.LocationTemplate}
     * - {@link com.kakao.message.template.CommerceTemplate}
     * - {@link com.kakao.message.template.TextTemplate}
     *
     * @param context  Context to start an activity for KakaoLink
     * @param params   TemplateParams object containing template arguments bulit with its builder
     * @param callback success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    @SuppressWarnings("unused")
    public void sendDefault(final Context context,
                            final TemplateParams params,
                            final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.defaultTemplateRequest(context, null, params);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, params, null);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, null, callback);
    }

    /**
     * Send KakaoLink v2 message with default templates.
     * <p>
     * 디폴트 템플릿을 사용하여 카카오링크 v2 메시지를 보낸다. 별도의 템플릿 설정이 필요 없으며 아래의 템플릿 타입들을 지원한다.
     * <p>
     * - {@link com.kakao.message.template.FeedTemplate}
     * - {@link com.kakao.message.template.ListTemplate}
     * - {@link com.kakao.message.template.LocationTemplate}
     * - {@link com.kakao.message.template.CommerceTemplate}
     * - {@link com.kakao.message.template.TextTemplate}
     *
     * @param context            Context to start an activity for KakaoLink
     * @param params             TemplateParams object containing template arguments bulit with its builder
     * @param serverCallbackArgs arguments to be delivered to server callback
     * @param callback           success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     * @since 1.13.0
     */
    public void sendDefault(final Context context,
                            final TemplateParams params,
                            final Map<String, String> serverCallbackArgs,
                            final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.defaultTemplateRequest(context, null, params);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, params, serverCallbackArgs);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, serverCallbackArgs, callback);
    }

    /**
     * Send a URL scrap message with custom template and template arguments.
     *
     * @param context      Context to start an activity for KakaoLink
     * @param url          URL to be scrapped
     * @param templateId   id of the custom template created in developer website
     * @param templateArgs template arguments to fill in the custom template
     * @param callback     success/failure callback that will be passed detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    @SuppressWarnings("unused")
    public void sendScrap(final Context context,
                          final String url,
                          final String templateId,
                          final Map<String, String> templateArgs,
                          final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.scrapTemplateRequest(context, null, url, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, url, templateId, templateArgs, null);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, null, callback);
    }

    /**
     * Send a URL scrap message with custom template and template arguments.
     *
     * @param context            Context to start an activity for KakaoLink
     * @param url                URL to be scrapped
     * @param templateId         id of the custom template created in developer website
     * @param templateArgs       template arguments to fill in the custom template
     * @param serverCallbackArgs arguments to be delivered to server callback
     * @param callback           success/failure callback that will be passed detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     * @since 1.13.0
     */
    @SuppressWarnings("WeakerAccess")
    public void sendScrap(final Context context,
                          final String url,
                          final String templateId,
                          final Map<String, String> templateArgs,
                          final Map<String, String> serverCallbackArgs,
                          final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return linkCore.scrapTemplateRequest(context, null, url, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() {
                return linkCore.sharerUri(context, url, templateId, templateArgs, serverCallbackArgs);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, serverCallbackArgs, callback);
    }

    /**
     * Send a URL scrap message with default scrap template.
     * <p>
     * 별도의 템플릿 설정 없이 URL 스크랩 메시지를 보낸다.
     *
     * @param context  Context to start an activity for KakaoLink
     * @param url      URL to be scrapped
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    @SuppressWarnings("unused")
    public void sendScrap(final Context context,
                          final String url,
                          final ResponseCallback<KakaoLinkResponse> callback) {
        sendScrap(context, url, null, callback);
    }

    /**
     * Send a URL scrap message with default scrap template.
     * <p>
     * 별도의 템플릿 설정 없이 카카오링크 v2 스크랩 메시지를 보낸다. 서버 콜백에 넘길 값들을 지정할 수 있다.
     *
     * @param context            Context to start an activity for KakaoLink
     * @param url                URL to be scrapped
     * @param callback           success/failure callback that will be passed detailed warnings or error messages
     * @param serverCallbackArgs arguments to be delivered to server callback
     * @throws IllegalStateException if kakao app key or android key hash is not set
     * @since 1.13.0
     */
    public void sendScrap(final Context context,
                          final String url,
                          final Map<String, String> serverCallbackArgs,
                          final ResponseCallback<KakaoLinkResponse> callback) {
        sendScrap(context, url, null, null, serverCallbackArgs, callback);
    }

    /**
     * Upload image to Kakao storage server to be used in KakaoLink message.
     * <p>
     * 자체적인 이미지 서버를 거치지 않고 로컬 디바이스에 있는 이미지를 카카오링크에 사용하고 싶은 경우 카카오 이미지 서버에
     * 업로드 하는 기능.
     *
     * @param context        Context to start an activity for KakaoLink
     * @param secureResource true if https is needed for image url, false if http is sufficient
     * @param imageFile      Image file
     * @param callback       success/failure callback that will be passed detailed warnings or error messages
     */
    public void uploadImage(final Context context, final Boolean secureResource, final File imageFile, final ResponseCallback<ImageUploadResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return imageService.imageUploadRequest(context, imageFile, secureResource);
            }
        };
        sendLinkImageRequest(future, ImageUploadResponse.CONVERTER, callback);
    }

    /**
     * Upload image with the given URL to Kakao storage server.
     *
     * @param context        Context to start an activity for KakaoLink
     * @param secureResource true if https is needed for image url, false if http is sufficient
     * @param imageUrl       URL of image to be scrapped
     * @param callback       success/failure callback that will be passed detailed warnings or error messages
     */
    public void scrapImage(final Context context, final Boolean secureResource, final String imageUrl, final ResponseCallback<ImageUploadResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return imageService.imageScrapRequest(context, imageUrl, secureResource);
            }
        };
        sendLinkImageRequest(future, ImageUploadResponse.CONVERTER, callback);
    }

    /**
     * Delete the image with the given token from Kakao storage server.
     *
     * @param context    Context to start an activity for KakaoLink
     * @param imageToken Token of image to be deleted
     * @param callback   success/failure callback that will be passed detailed warnings or error messages
     */
    @SuppressWarnings("unused")
    public void deleteImageWithToken(final Context context, final String imageToken, final ResponseCallback<ImageDeleteResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() {
                return imageService.imageDeleteRequestWithToken(context, imageToken);
            }
        };
        sendLinkImageRequest(future, ImageDeleteResponse.CONVERTER, callback);
    }

    private void sendKakaoLinkRequest(final Context context, final Future<IRequest> requestFuture,
                                      final Future<Uri> uriFuture, final Map<String, String> serverCallbackArgs,
                                      final ResponseCallback<KakaoLinkResponse> callback) {
        try {
            networkService.request(requestFuture.get(), JSON_OBJECT_CONVERTER, new ResponseCallback<JSONObject>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    callback.onFailure(errorResult);
                }

                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if (isKakaoLinkV2Available(context)) {
                            Intent intent = linkCore.kakaoLinkIntent(context, null, result, serverCallbackArgs);
                            context.startActivity(intent);
                        } else {
                            openUrlWithCustomTab(context, uriFuture.get());
                        }
                        if (callback != null) {
                            callback.onSuccess(new KakaoLinkResponse(result));
                        }
                    } catch (Exception e) {
                        if (callback != null) {
                            callback.onFailure(new ErrorResult(e));
                        }
                    }
                }

                @Override
                public void onDidStart() {
                    super.onDidStart();
                    callback.onDidStart();
                }

                @Override
                public void onDidEnd() {
                    super.onDidEnd();
                    callback.onDidEnd();
                }
            });
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }

    private <T> void sendLinkImageRequest(final Future<IRequest> requestFuture, final ResponseStringConverter<T> converter, final ResponseCallback<T> callback) {
        try {
            networkService.request(requestFuture.get(), converter, callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }

    @SuppressWarnings("unused")
    private ErrorResult getKakaoTalkNotInstalledErrorResult(final Context context) {
        return new ErrorResult(new KakaoException(KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED, context.getString(R.string.com_kakao_alert_install_kakaotalk)));
    }

    /**
     * Returns an intent to start KakaoTalk install page of Google play store.
     *
     * @param context Context to get app information (package name, app key, key hash, and KA header) from.
     * @return Intent to start KakaoTalk install page of Google Play Store.
     */
    @SuppressWarnings("unused")
    public Intent getKakaoTalkInstallIntent(final Context context) {
        return linkCore.kakaoTalkMarketIntent(context);
    }

    private static final ResponseStringConverter<JSONObject> JSON_OBJECT_CONVERTER = new ResponseStringConverter<JSONObject>() {
        @Override
        public JSONObject convert(String o) {
            try {
                return new JSONObject(o);
            } catch (JSONException e) {
                Logger.e(e.toString());
                return null;
            }
        }
    };

    void openUrlWithCustomTab(final Context context, final Uri uri) throws KakaoException {
        final String packageName = resolveCustomTabsPackageName(context, uri);
        if (packageName == null) {
            throw new KakaoException(KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED, context.getString(R.string.com_kakao_alert_install_kakaotalk));
        }
        CustomTabsClient.bindCustomTabsService(context, packageName, new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.enableUrlBarHiding();
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setData(uri);
                customTabsIntent.intent.setPackage(packageName);
                context.startActivity(customTabsIntent.intent);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    String resolveCustomTabsPackageName(final Context context, final Uri uri) {
        String packageName = null;
        String availableChrome = null;

        // get ResolveInfo for default browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // get ResolveInfos for browsers that support custom tabs protocol
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        List<ResolveInfo> serviceInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        for (ResolveInfo info : serviceInfos) {
            // check if chrome is available on this device
            if (availableChrome == null && isPackageNameChrome(info.serviceInfo.packageName)) {
                availableChrome = info.serviceInfo.packageName;
            }
            // check if the browser being looped is the default browser
            if (info.serviceInfo.packageName.equals(resolveInfo.activityInfo.packageName)) {
                packageName = resolveInfo.activityInfo.packageName;
                break;
            }
        }

        // if the default browser does not support custom tabs protocol, use chrome if available.
        if (packageName == null && availableChrome != null) {
            packageName = availableChrome;
        }
        Logger.d("selected browser for kakaolink is %s", packageName);
        return packageName;
    }

    private boolean isPackageNameChrome(final String packageName) {
        return chromePackageNames.contains(packageName);
    }

    private List<String> chromePackageNames = Arrays.asList(
            "com.android.chrome",
            "com.chrome.beta",
            "com.chrome.dev");
}
