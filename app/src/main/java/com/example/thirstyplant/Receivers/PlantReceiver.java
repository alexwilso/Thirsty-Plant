package com.example.thirstyplant.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.thirstyplant.R;
import com.example.thirstyplant.activities.WaterSchedule;

public class PlantReceiver extends android.content.BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationID = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("toWater");

        Intent toWater = new Intent(context, WaterSchedule.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toWater, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context, "waterNotify");
        builder.setContentTitle("You have a plant to water")
                .setContentText(message + " needs to be watered")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationID, builder.build());
    }
}
