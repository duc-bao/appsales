package com.manager.myapplication.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.manager.myapplication.R;
import com.manager.myapplication.activity.MainActivity;

// Tạo thông  báo từ firebase
public class FirebaseMessagerReccevier  extends FirebaseMessagingService {
    private RemoteViews customview(String title, String body){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title_noti, title);
        remoteViews.setTextViewText(R.id.body_noti, body);
        remoteViews.setImageViewResource(R.id.imgnoti, R.drawable.baseline_app_shortcut_24);
        return remoteViews;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
       if (message.getNotification() != null){
           showNotification(message.getNotification().getTitle(), message.getNotification().getBody());
       }
    }

    private void showNotification(String title, String body) {
        String channelId=  "noti";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.baseline_app_shortcut_24).setAutoCancel(true).setVibrate(new long[]{1000,1000,1000})
                .setOnlyAlertOnce(true).setContentIntent(pendingIntent);
        builder = builder.setContent(customview(title,body));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "WEB_APP", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }
}
