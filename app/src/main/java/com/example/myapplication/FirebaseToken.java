package com.example.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;

public class FirebaseToken extends FirebaseMessagingService {
    private String refreshedToken = null;
    private static final String CHANNEL_ID = "1000" ;
    private String Latitude;
    private  String Longitude;
    private String Phone_number;
    private  String data_array;

    private SharedPreferences sensor_status_pref;
    private SharedPreferences.Editor sensor_status_editor;

//    private static NotificationManagerCompat notificationManager;
//    private static NotificationChannel channel;
//
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
//
//        if (remoteMessage.getNotification() != null) {
//            Log.e("포그라운드", "From: " + remoteMessage.getFrom());
//            Log.e("포그라운드", "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            String messageBody = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
//
//            createNotificationChannel();
//            getHeadup(messageBody +"(포그라운드)", messageTitle);
//
//            //sendNotification(messageBody,messageTitle);
//            //Toast.makeText(MainActivity.class, remoteMessage.getNotification().getTitle(), Toast.LENGTH_SHORT).show();
//        }
//        else if (remoteMessage.getData().size() > 0) {
//            Log.e("백그라운드", "From: " + remoteMessage.getData().get("title"));
//            Log.e("백그라운드", "Message Notification Body: " + remoteMessage.getData().get("title"));
//
//
//            String messageBody = remoteMessage.getData().get("title");
//            String messageTitle = remoteMessage.getData().get("body");
//
//            createNotificationChannel();
//            getHeadup(messageBody+"2", messageTitle);
//           // sendNotification(messageBody, messageTitle);
//        }

        sensor_status_pref = getApplication().getSharedPreferences("Sensor_status", Activity.MODE_PRIVATE);
        int falldown_count = Integer.parseInt(sensor_status_pref.getString("falldown_count", "0"));
        falldown_count++;
        sensor_status_editor = sensor_status_pref.edit();
        sensor_status_editor.putString("falldown_count" ,Integer.toString(falldown_count) );
        sensor_status_editor.commit();

        String messageBody = remoteMessage.getData().get("title");
        String messageTitle = remoteMessage.getData().get("body");
        Latitude = remoteMessage.getData().get("latitude");
        Longitude = remoteMessage.getData().get("longitude");
        Phone_number = remoteMessage.getData().get("phone_number");
        data_array = remoteMessage.getData().get("data_array");
        Log.e("twtwertretertert", String.format("%s + %s", Latitude, Longitude));
        createNotificationChannel();
        getHeadup(messageBody, messageTitle, Latitude, Longitude, Phone_number, data_array);
    }
    private void getHeadup(String title, String body, String Latitude, String Longitude, String Phone_number, String data_array){
        Intent snoozeIntent = new Intent(this,FalldownActivity.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        snoozeIntent.putExtra("latitude", Latitude);
        snoozeIntent.putExtra("longitude", Longitude);
        snoozeIntent.putExtra("phone_number", Phone_number);
        snoozeIntent.putExtra("data_array", data_array);

        snoozeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent snoozePendingIntent =
                PendingIntent.getActivity(this, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setFullScreenIntent(snoozePendingIntent, false)
                .setContentIntent(snoozePendingIntent);
                //.setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        int notificationId = 15;
        notificationManager.notify(notificationId, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManager.class
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void sendNotification(String messageBody, String messageTitle) {
        //////////////////////////// 포그라운드 및 백그라운드 푸시알림 처리 ////////////////////////////
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 2;

        String CHANNEL_ID = "my_channel_02";

        try {
            Notification notification = new Notification.Builder(FirebaseToken.this)
                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
                    .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
                    .setSmallIcon(R.drawable.ic_menu_slideshow)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
//
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.drawable.ic_menu_slideshow);
//            mediaPlayer.start();

            mNotificationManager.notify(notifyID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
