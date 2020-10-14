package com.wilson.thirstyplant.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.adaptors.FertilizeAdaptor;
import com.wilson.thirstyplant.adaptors.WaterAdaptor;
import com.wilson.thirstyplant.io.DatabaseHelper;
import com.wilson.thirstyplant.model.Plant;
import com.wilson.thirstyplant.notifications.WaterNotifications;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private Button settingsButton, addPlantButton, myPlants;
    DatabaseHelper databaseHelper;
    List<Plant> toWaterPlants;
    List<Plant> toFertilizePlants;
    TextView waterComplete;
    TextView fertilizeComplete;
    AlarmManager alarmManager;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // References to controls on layout
        settingsButton = findViewById(R.id.settingsButton);
        addPlantButton = findViewById(R.id.addPlant);
        myPlants = findViewById(R.id.myPlants);
        waterComplete = findViewById(R.id.waterComplete);
        fertilizeComplete = findViewById(R.id.fertilizeComplete);
        databaseHelper = new DatabaseHelper(Home.this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        context = getApplicationContext();
        // Sets and fills recycle view with plants to be watered
        toWaterPlants = new ArrayList<>();
        needWater(toWaterPlants);
        if (toWaterPlants.isEmpty()){
            waterComplete.setVisibility(View.VISIBLE);
        }
        else {
            waterComplete.setVisibility(View.INVISIBLE);
        }
        RecyclerView waterView = findViewById(R.id.ToWater);
        WaterAdaptor waterAdaptor = new WaterAdaptor(Home.this, toWaterPlants, alarmManager, context);
        waterView.setLayoutManager(new GridLayoutManager(Home.this, 3));
        waterView.setAdapter(waterAdaptor);

        // Sets and fills recycle view with plants to be fertilized
        toFertilizePlants = new ArrayList<>();
        needFertilizer(toFertilizePlants);
        if (toFertilizePlants.isEmpty()){
            fertilizeComplete.setVisibility(View.VISIBLE);
        }
        else {
            fertilizeComplete.setVisibility(View.INVISIBLE);
        }
        RecyclerView fertilizeView = findViewById(R.id.ToFertilize);
        FertilizeAdaptor fertilizeAdaptor = new FertilizeAdaptor(Home.this, toFertilizePlants, alarmManager, context);
        fertilizeView.setLayoutManager(new GridLayoutManager(Home.this, 3));
        fertilizeView.setAdapter(fertilizeAdaptor);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSettings();
            }
        });
        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlant();
            }
        });
        myPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMyPlants();
            }
        });
        createNotificationChannelWatering();
        createNotificationChannelFertilizing();
    }

    /**
     * Takes user to add plant activity
     */
    private void addPlant(){
        Intent addPlant = new Intent(Home.this, AddPlant.class);
        startActivity(addPlant);
    }

    /**
     * Takes user to my plant activity
     */
    private void toMyPlants(){
        Intent toMyPlants = new Intent(Home.this, MyPlants.class);
        startActivity(toMyPlants);
    }

    /**
     * Switches to settings activity
     */
    private void toSettings(){
        Intent toSettings = new Intent(Home.this, Settings.class);
        startActivity(toSettings);
    }

    /**
     * Creates notification channel for watering notifications
     */
    private void createNotificationChannelWatering() {

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
     * Creates notification channel for watering notifications
     */
    private void createNotificationChannelFertilizing() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence charSequence = "Fertilizing Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("FertilizeAlarm", charSequence, importance);
            channel.setDescription("Alarm for fertilizing");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Adds plants in that need to be watered today or before
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void needWater(List<Plant> plants){
        plants.addAll(databaseHelper.toBeWatered());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void needFertilizer(List<Plant> plants){
        plants.addAll(databaseHelper.toBeFertilized());
    }


}
