package com.kakao.sdk.sample.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.push.PushService;
import com.kakao.push.PushToken;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.RootLoginActivity;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2017. 1. 26..
 */

public class SampleFirebaseMessagingService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 1;
    public static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendDataMessage(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        try {
            if (Session.getCurrentSession().isOpened()) {
                PushService.getInstance().registerPushToken(
                        KakaoSDK.getAdapter().getPushConfig().getTokenRegisterCallback(),
                        s,
                        KakaoSDK.getAdapter().getPushConfig().getDeviceUUID(),
                        Utility.getAppVersion(this)
                );
            } else {
                PushToken.saveFcmTokenToCache(FirebaseInstanceId.getInstance().getToken());
            }
        } catch (IllegalStateException e) {
            Logger.e("Session is not initialized. You should call KakaoSDK.init() first.");
        } catch (NullPointerException e) {
            Logger.e("There is something wrong with your KakaoAdapter settings. Check again if it is properly set.");
        }
    }

    private void sendDataMessage(final RemoteMessage message) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(this, RootLoginActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "kakao_push_channel")
                .setSmallIcon(R.drawable.push_noti_icon)
                .setContentTitle(getApplicationContext().getString(R.string.push_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getData().get("content")))
                .setContentText(message.getData().get("content"));

        mBuilder.setContentIntent(contentIntent);
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID + count++, mBuilder.build());
        }
    }
}