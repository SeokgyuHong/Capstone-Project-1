package com.kakao.kakaolink.v2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.kakao.kakaolink.v2.mocks.TestKakaoLinkImageService;
import com.kakao.kakaolink.v2.mocks.TestKakaoLinkCore;
import com.kakao.kakaolink.v2.network.KakaoLinkCore;
import com.kakao.kakaolink.v2.network.KakaoLinkImageService;
import com.kakao.network.ErrorResult;
import com.kakao.network.NetworkService;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.test.common.KakaoTestCase;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.util.concurrent.RoboExecutorService;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * @author kevin.kang. Created on 2016. 11. 28..
 */

@RunWith(RobolectricTestRunner.class)
public class KakaoLinkServiceTest extends KakaoTestCase {
    private Activity activity;
    private KakaoLinkService service;
    private KakaoLinkCore linkService;
    private ResponseCallback<KakaoLinkResponse> callback;

    @Before
    public void setup() {
        super.setup();
        activity = mock(Activity.class);
        linkService = spy(new TestKakaoLinkCore());
        KakaoLinkImageService imageService = spy(new TestKakaoLinkImageService());
        NetworkService networkService = spy(new TestNetworkService(new Handler(Looper.getMainLooper()), new RoboExecutorService()));
        service = spy(new KakaoLinkService(linkService, imageService, networkService));
        callback = spy(new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
        Robolectric.getBackgroundThreadScheduler().pause();
    }

    /**
     * Tests normal case of kakaolink v2 send.
     */
    @Test
    public void send() {
        doReturn(true).when(linkService).isAvailable(activity);
        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("${iphoneAppParam}", "key1=value1");
        service.sendCustom(activity, "91", templateArgs, callback);
        ShadowApplication.runBackgroundTasks();
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        verify(callback).onSuccess(any(KakaoLinkResponse.class));
    }

    /**
     * Tests if sharer is used to send kakaolink if lower version or no KakaoTalk is installed.
     */
    @Test
    public void sendWithLowerTalkVersion() {
        doReturn(false).when(linkService).isAvailable(activity);

        service.sendCustom(activity, "91", getTemplateArgs(), callback);
        ShadowApplication.runBackgroundTasks();
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        verify(service).openUrlWithCustomTab(any(Context.class), any(Uri.class));
    }

    /**
     * Tests if intent with long uri properly calls error callback. KakaoException should not
     * propagate to UI thread, causing app crash. If exception propagates, neither success / failure
     * callback will be called.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void sendWithLongUri() {
        // throw exception when creating kakaolink intent.
        doThrow(new KakaoException(KakaoException.ErrorType.URI_LENGTH_EXCEEDED, "uri length exceeded"))
                .when(linkService).kakaoLinkIntent(eq(activity), (String) isNull(), any(JSONObject.class), (Map<String, String>) isNull());

        service.sendCustom(activity, "91", getTemplateArgs(), callback);
        ShadowApplication.runBackgroundTasks();
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        // verify exception is caught and error callback is called.
        verify(callback, times(0)).onSuccess(any(KakaoLinkResponse.class));
        verify(callback).onFailure(any(ErrorResult.class));
    }

    private Map<String, String> getTemplateArgs() {
        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("${iphoneAppParam}", "key1=value1");
        return templateArgs;
    }
}
