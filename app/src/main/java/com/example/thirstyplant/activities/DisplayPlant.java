package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thirstyplant.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class DisplayPlant extends AppCompatActivity {
    TextView plantName, plantNickName, plantLocation, plantDate, plantWater, plantFertilize, plantPath, plantCare;
    ImageView plantPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plant);
        plantPhoto = findViewById(R.id.displayPhoto);
        plantName = findViewById(R.id.displayName);
        plantNickName = findViewById(R.id.displayNickName);
        plantLocation = findViewById(R.id.displayLocation);
        plantDate = findViewById(R.id.displayDate);
        plantWater = findViewById(R.id.displayWater);
        plantFertilize = findViewById(R.id.displayFertilize);
        plantCare = findViewById(R.id.displayCare);
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
        if (getIntent().getStringExtra("Path").equals("app/src/main/res/drawable/plant.png")) {
            plantPhoto.setImageResource(R.drawable.plant);
        } else {
            File file = new File(Objects.requireNonNull(getIntent().getStringExtra("Path")));
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
        plantName.setText(getIntent().getStringExtra("Name"));
    }

    /**
     * Sets plant names with string passed with intent
     */
    public void setNickname(){
        plantNickName.setText(getIntent().getStringExtra("NickName"));
    }

    /**
     * Sets plant location with string passed with intent
     */
    public void setLocation(){
        plantLocation.setText(getIntent().getStringExtra("Location"));
    }

    /**
     * Sets date acquired with string passed with intent
     */
    public void setDate(){
        plantDate.setText(getIntent().getStringExtra("Date"));
    }

    /**
     * Sets next water date with string passed with intent
     */
    public void setWater(){
        plantWater.setText(getIntent().getStringExtra("Water"));
    }

    /**
     * Sets next fertilize date with string passed with intent
     */
    public void setFertilize(){
        plantFertilize.setText(getIntent().getStringExtra("Fertilize"));
    }

    /**
     * Sets care instructions with string passed with intent
     */
    public void setCare(){
        plantCare.setText(getIntent().getStringExtra("Care"));
    }

}