package com.wilson.thirstyplant.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.notifications.FertilizeNotifications;
import com.wilson.thirstyplant.notifications.WaterNotifications;
import com.wilson.thirstyplant.receivers.FertilizeReceiver;
import com.wilson.thirstyplant.receivers.WaterReceiver;
import com.wilson.thirstyplant.io.DatabaseHelper;
import com.wilson.thirstyplant.model.Plant;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Objects;

import static com.wilson.thirstyplant.activities.FertilizeSchedule.TO_FERTILIZE;

public class DisplayPlant extends AppCompatActivity {
    TextView plantName, plantNickName, plantLocation, plantDate, plantWater, plantFertilize, plantPath, plantCare;
    ImageView plantPhoto;
    Button water, fertilize, delete, home;
    DatabaseHelper databaseHelper;
    AlarmManager alarmManager;
    Plant plant;
    int plantNum;
    int id;
    int notificationIdWater = 100;
    int notificationIdFertilize = 200;
    public static final String NOTIFICATION_ID = "notificationId";
    Intent intent;
    Context context;
    WaterNotifications waterNotifications;
    FertilizeNotifications fertilizeNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plant);
        setViews();
        plant = (Plant) getIntent().getSerializableExtra("Plant");
        setFields();
        assert plant != null;
        id = plant.getNotification_id();
        plantNum = plant.getId();
        context = getApplicationContext();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        databaseHelper = new DatabaseHelper(DisplayPlant.this);
        waterNotifications = new WaterNotifications(plant, context, alarmManager);
        fertilizeNotifications = new FertilizeNotifications(plant, context, alarmManager);
        home = findViewById(R.id.toDisplay);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlant();
            }
        });
        water.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    waterPlant();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        fertilize.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    fertilizePlant();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DisplayPlant.this, "Plant does not need to be fertilized", Toast.LENGTH_SHORT).show();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDisplay = new Intent(DisplayPlant.this, MyPlants.class);
                startActivity(toDisplay);
            }
        });

    }

    /**
     * Sets Views based on id
     */
    public void setViews(){
        plantPhoto = findViewById(R.id.displayPhoto);
        plantName = findViewById(R.id.displayName);
        plantNickName = findViewById(R.id.displayNickName);
        plantLocation = findViewById(R.id.displayLocation);
        plantDate = findViewById(R.id.displayDate);
        plantWater = findViewById(R.id.displayWater);
        plantFertilize = findViewById(R.id.displayFertilize);
        plantCare = findViewById(R.id.displayCare);
        delete = findViewById(R.id.deletePlant);
        water = findViewById(R.id.waterPlant);
        fertilize = findViewById(R.id.fertilizePlant);
    }

    public void setFields(){
        setPhoto();
        setName();
        setNickname();
        setLocation();
        setDate();
        setWater();
        setFertilize();
        setCare();
    }

    /**
     * Sets photo. If user hasn't taken photo, uses plant photo from drawable
     */
    public void setPhoto() {
        if (plant.getPhotoSource().equals("app/src/main/res/drawable/plant.png")) {
            plantPhoto.setImageResource(R.drawable.plant);
        } else {
            File file = new File(Objects.requireNonNull(plant.getPhotoSource()));
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                plantPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets plant names with string passed with intent
     */
    public void setName(){
        plantName.setText(plant.getPlantName());
    }

    /**
     * Sets plant names with string passed with intent
     */
    public void setNickname(){

        plantNickName.setText(plant.getNickName());
    }

    /**
     * Sets plant location with string passed with intent
     */
    public void setLocation(){

        plantLocation.setText(plant.getLocation());
    }

    /**
     * Sets date acquired with string passed with intent
     */
    public void setDate(){

        plantDate.setText(plant.getDateAcquired());
    }

    /**
     * Sets next water date with string passed with intent
     */
    public void setWater(){
        String time = "Date: " + plant.getNextWaterDate() +  "\n" + " Time: "
                + plant.getNextWaterTimer() + "\n" +
                " Every " + plant.getWaterFequency() + " days";
        plantWater.setText(time);
    }

    /**
     * Sets next fertilize date with string passed with intent
     */
    public void setFertilize(){
        String time = "Date: " + plant.getNextfertilizeDate() + "\n" + " Time: " +
                plant.getGetNextfertilizeTime() + "\n"+ " Every " + plant.getFertilizeFrequency() + " days";

        plantFertilize.setText(time);
    }

    /**
     * Sets care instructions with string passed with intent
     */
    public void setCare(){
        plantCare.setText(plant.getCareInstructions());
    }

    /**
     * Deletes plant from database and cancels alarms
     */
    public void deletePlant(){
        waterNotifications.deleteAlarm();
        deleteFertilizeAlarm();
        databaseHelper.deletePlant(plantNum);
        Intent toDisplay = new Intent(DisplayPlant.this, MyPlants.class);
        startActivity(toDisplay);
    }

    /**
     * Deletes watering alarm
     */
    public void deleteWaterAlarm(){
        Intent waterAlarm = new Intent(getApplicationContext(), WaterReceiver.class);
        waterAlarm.putExtra(NOTIFICATION_ID, notificationIdWater);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                id, waterAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

    /**
     * Deletes fertilizing alarm
     */
    public void deleteFertilizeAlarm(){
        Intent fertAlarm = new Intent(getApplicationContext(), FertilizeReceiver.class);
        fertAlarm.putExtra(NOTIFICATION_ID, notificationIdFertilize);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                id, fertAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Moves date of next water
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void waterPlant() throws JSONException {
        plant.watered();
        databaseHelper.waterPlant(plant);
        deleteWaterAlarm();
        waterNotifications.createAlarm();
        reloadPlant();
    }

    /**
     * Moves date of next fertilize
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fertilizePlant() throws Exception {
        plant.fertilized();
        databaseHelper.fertilizePlant(plant);
        deleteFertilizeAlarm();
        fertilizeNotifications.createAlarm();
        reloadPlant();
    }

    /**
     * Reloads display activity
     */
    public void reloadPlant(){
        Intent displayPlant = new Intent(this, DisplayPlant.class);
        displayPlant.putExtra("Plant", plant);
        startActivity(displayPlant);
    }

}