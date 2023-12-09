package com.pt_plus.Fcm;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pt_plus.Activitys.ActivityHome;
import com.pt_plus.Activitys.ActivitySpashScreen;
import com.pt_plus.R;
import com.pt_plus.Utils.AppLogger;


import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class FirebaseMessageReceiver
        extends FirebaseMessagingService {


    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        String image = "";
        try {
            image = remoteMessage.getData().get("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), image);
    }

    public void showNotification(String title,
                                 String message, String image) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, ActivityHome.class);
        // Assign channel ID
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)

                .setContentTitle(title)
                .setContentText(message);

        if (image != null && !image.isEmpty() && !image.equalsIgnoreCase("null")) {
            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addNextIntent(intent);
//                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                        mBuilder.setContentIntent(resultPendingIntent);

                            notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mBuilder.setLargeIcon(resource);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                            stackBuilder.addNextIntent(intent);
//                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                        mBuilder.setContentIntent(resultPendingIntent);

                            notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {

                        }
                    });
        } else {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntent(intent);
//                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                        mBuilder.setContentIntent(resultPendingIntent);

            notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
        }


    }

}