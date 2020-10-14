package com.wilson.thirstyplant.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.wilson.thirstyplant.model.Plant;
import com.wilson.thirstyplant.receivers.WaterReceiver;
import org.json.JSONException;
import java.util.Calendar;

public class WaterNotifications {


    public static final String NOTIFICATION_ID = "notificationId";
    public static final String TO_WATER = "toWater";
    int notificationId = 100;
    Plant plant;
    Context context;
    AlarmManager alarmManager;

    public WaterNotifications(Plant plant, Context context, AlarmManager alarmManager) {
        this.plant = plant;
        this.context = context;
        this.alarmManager = alarmManager;
    }

    /**
     * Sets time and date for alarm
     * */
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

        return alarmTime;
    }

    /**
     * Creates alarm at time chosen by user
     */
    public void createAlarm() {
        Intent intent = new Intent(context, WaterReceiver.class);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TO_WATER, "Name: " + plant.getPlantName() + " Location: " + plant.getLocation());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                plant.getNotification_id(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alarmTime = setTimeDate();
        long alarmStartTime = alarmTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
    }

    /**
     * Deletes watering alarm
     */
    public void deleteAlarm(){
        Intent waterAlarm = new Intent(context, WaterReceiver.class);
        waterAlarm.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                plant.getNotification_id(), waterAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }
}
