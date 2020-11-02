package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseToken extends FirebaseMessagingService {
    private String refreshedToken = null;
    /* 토큰이 새로 만들어질때나 refresh 될때  */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //Log.e("NEW_TOKEN", refreshedToken);

        /* DB서버로 새토큰을 업데이트시킬수 있는 부분 */
    }

    /* 메세지를 새롭게 받을때 */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("test", "From: " + remoteMessage.getFrom());

        /* 새메세지를 알림기능을 적용하는 부분 */

        if (remoteMessage.getNotification() != null) {
            Log.e("포그라운드", "From: " + remoteMessage.getFrom());
            Log.e("포그라운드", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();
            //Toast.makeText(MainActivity.class, remoteMessage.getNotification().getTitle(), Toast.LENGTH_SHORT).show();
        }
        else if (remoteMessage.getData().size() > 0) {
            Log.e("백그라운드", "From: " + remoteMessage.getFrom());
            Log.e("백그라운드", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();
        }
    }

//    private void sendNotification(String messageBody, String messageTitle) {
//        //////////////////////////// 포그라운드 및 백그라운드 푸시알림 처리 ////////////////////////////
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
//
//        mChannel.setDescription(description);
//        mChannel.enableLights(true);
//        mNotificationManager.createNotificationChannel(mChannel);
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int notifyID = 2;
//
//        String CHANNEL_ID = "my_channel_02";
//
//        try {
//            Notification notification = new Notification.Builder(MyFBMessageService.this)
//                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
//                    .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
//                    .setSmallIcon(R.drawable.icon)
//                    .setChannelId(CHANNEL_ID)
//                    .setContentIntent(pendingIntent)
//                    .build();
//
//            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
//            mediaPlayer.start();
//
//            mNotificationManager.notify(notifyID, notification);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
