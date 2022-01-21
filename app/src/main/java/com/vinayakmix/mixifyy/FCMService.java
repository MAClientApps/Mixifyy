package com.vinayakmix.mixifyy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "FirebaseService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("FCM Token : ", s);
        AdjustEvent adjustEvent = new AdjustEvent(AppConst.adjustEventToken);
        adjustEvent.addCallbackParameter(AppConst.eventValue, s);
        adjustEvent.addCallbackParameter(AppConst.userUUID,
                AppConst.generateMixifyyUserUUID(FCMService.this));
        Adjust.trackEvent(adjustEvent);
        Adjust.setPushToken(s, FCMService.this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        sendNotification(notification, data);
    }

    private void sendNotification(RemoteMessage.Notification notification,
                                  Map<String, String> data) {

        Intent intent = null;
        String title = "", body = "";
        if (data != null) {
            String action = data.get(AppConst.actionId);
            String deeplink = data.get(AppConst.deepLink);
            if (action != null && action.equalsIgnoreCase("1")) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent = new Intent(this, SplashActivity.class);
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
        }

        if (notification != null) {
            title = notification.getTitle();
            body = notification.getBody();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "1")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_launcher_round);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("1", CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}
