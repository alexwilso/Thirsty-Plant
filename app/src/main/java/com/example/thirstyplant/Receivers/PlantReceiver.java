package com.example.thirstyplant.Receivers;

import android.app.Notification;
import android.app.NotificationChannel;
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
import com.example.thirstyplant.activities.Home;
import com.example.thirstyplant.activities.WaterSchedule;

public class PlantReceiver extends android.content.BroadcastReceiver {
    private static final String channelId = "waterAlarm";

    public void onReceive(Context context, Intent intent) {
        // Plant name and id for message
        int notificationID = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("toWater");

        // Takes user to home when notification is tapped
        Intent toHome = new Intent(context, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toHome, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence charSequence = "Water ";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, charSequence, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Sets details of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.plant)
                .setContentTitle("Time to Water!")
                .setContentText("Your " + message + " needs to be watered")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // calls notification
        notificationManager.notify(notificationID, builder.build());

    }
}
