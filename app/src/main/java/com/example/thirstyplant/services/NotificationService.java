//package com.example.thirstyplant.services;
//
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.util.Log;
//import android.view.View;
//
//import androidx.annotation.RequiresApi;
//
//import com.example.thirstyplant.Receivers.WaterReceiver;
//import com.example.thirstyplant.activities.WaterSchedule;
//import com.example.thirstyplant.model.Plant;
//
//import org.json.JSONException;
//
//import java.util.Calendar;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class NotificationService extends Service {
//
//    Timer timer;
//    TimerTask timerTask;
//    String TAG = "Timers";
//    int alarmAt;
//    Plant plant;
//    long time;
//    Intent intent;
//
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e(TAG, "onStartCommand");
//        super.onStartCommand(intent, flags, startId);
//
//        startTimer();
//
//        return START_STICKY;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onCreate() {
//        Log.e(TAG, "onCreate");
//        createNotificationChannel();
//        plant = this.getInt
//        long time = plant.timeUntilCare(plant.getNextWaterDate() + " " + plant.getNextWaterTimer());
//
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.e(TAG, "onDestroy");
//        stoptimertask();
//        super.onDestroy();
//
//
//    }
//
//    //we are going to use a handler to be able to run in our TimerTask
//    final Handler handler = new Handler();
//
//
//    public void startTimer() {
//        //set a new Timer
//        timer = new Timer();
//
//        //initialize the TimerTask's job
//        initializeTimerTask();
//
//        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//        timer.schedule(timerTask, time, alarmAt); //
//        //timer.schedule(timerTask, 5000,1000); //
//    }
//
//    public void stoptimertask() {
//        //stop the timer, if it's not already null
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    public void initializeTimerTask() {
//
//        timerTask = new TimerTask() {
//            public void run() {
//
//                //use a handler to run a toast that shows the current timestamp
//                handler.post(new Runnable() {
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    public void run() {
//                        try {
//                            createAlarm();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        };
//    }
//
//    private void createNotificationChannel() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence charSequence = "Watering Notification";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel = new NotificationChannel("WaterAlarm", charSequence, importance);
//            channel.setDescription("Alarm for watering");
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    /**
//     * Creates alarm at time chosen by user
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void createAlarm() throws JSONException {
//        Intent intent = new Intent(this, WaterReceiver.class);
//        intent.putExtra("notificationId", 101);
//        intent.putExtra("toWater", "Name: " + plant.getPlantName() + " Location: " + plant.getLocation());
//        // Look at flag here
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
//                plant.getNotification_id(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        long time = plant.timeUntilCare(plant.getNextWaterDate() + " " + plant.getNextWaterTimer());
//
//        System.out.println("Tiemr will ring at " + plant.timeUntilCare(plant.getNextWaterDate() + " " + plant.getNextWaterTimer()));
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
//    }
//}