package com.wilson.thirstyplant.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.activities.Home;


import static com.wilson.thirstyplant.services.ServiceNotification.Channel_ID;


public class ForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(ForegroundService.this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, Channel_ID)
                .setContentTitle("Thirsty Plant service started")
                .setContentText("You will continue to receive notifications for this application")
                .setSmallIcon(R.drawable.plant)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

