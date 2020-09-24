package com.example.thirstyplant.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.thirstyplant.R;
import com.example.thirstyplant.activities.Home;

public class FertilizeReceiver extends BroadcastReceiver {
    private static final String channelId = "FertilizeAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Plant name and id for notification
        int notificationID = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("toFertilize");

        // Takes user to home when notification is tapped
        Intent toHome = new Intent(context, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toHome, 0);

        // Sets details of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.plant)
                .setContentTitle(message)
                .setContentText("Your plant needs to be fertilized")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // calls notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationID, builder.build());
    }
}
