package com.example.thirstyplant.notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.example.thirstyplant.Receivers.WaterReceiver;

import com.example.thirstyplant.model.Plant;

import org.json.JSONException;

import java.util.Calendar;

public class NotificationHelper extends Activity {
    Plant plant;
    int notificationId = 100;
    int id;

    public NotificationHelper(Plant plant) {
        this.plant = plant;
        createNotificationChannel();
    }


    /**
     * Sets time and date for alarm
     */
    public Calendar setTimeDate(){
        // Splits date into integers
        String[] arrOfString = plant.getNextWaterDate().split("-");
        int year = Integer.parseInt(arrOfString[0]);
        int month = Integer.parseInt(arrOfString[1]);
        int day = Integer.parseInt(arrOfString[2]);

        // Splits time into integers
        String[] timeToInt = plant.getNextWaterTimer().split(":");
        int hour = Integer.parseInt(timeToInt[0]);
        int minute = Integer.parseInt(timeToInt[1]);

        // Sets calender time to time chosen by user
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(year, month -1, day, hour, minute, 0);
//
        return alarmTime;
    }

    /**
     * Creates notification channel for watering notifications
     */
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence charSequence = "Watering Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("WaterAlarm", charSequence, importance);
            channel.setDescription("Alarm for watering");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates alarm at time chosen by user
     */
    public void createAlarm(View view) throws JSONException {
        Intent intent = new Intent(NotificationHelper.this, WaterReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("toWater", "Name: " + plant.getPlantName() + " Location: " + plant.getLocation());
        // Look at flag here
        id = plant.getNotification_id();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationHelper.this,
                id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar alarmTime = setTimeDate();
        long alarmStartTime = alarmTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
    }
}
