package com.example.thirstyplant.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirstyplant.R;
import com.example.thirstyplant.adaptors.HomeAdaptor;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private Button logOutButton, addPlantButton, myPlants;
    FirebaseAuth firebaseAuth;
    DatabaseHelper databaseHelper;
    List<Plant> toWaterPlants;
    List<Plant> toFertilizePlants;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // References to controls on layout
        logOutButton = findViewById(R.id.logOutButton);
        addPlantButton = findViewById(R.id.addPlant);
        myPlants = findViewById(R.id.myPlants);
        databaseHelper = new DatabaseHelper(Home.this);

        // Sets and fills recycle view with plants to be watered
        toWaterPlants = new ArrayList<>();
        needWater(toWaterPlants);
        RecyclerView waterView = findViewById(R.id.ToWater);
        HomeAdaptor waterAdaptor = new HomeAdaptor(Home.this, toWaterPlants, true);
        waterView.setLayoutManager(new GridLayoutManager(Home.this, 3));
        waterView.setAdapter(waterAdaptor);

        // Sets and fills recycle view with plants to be fertilized
        toFertilizePlants = new ArrayList<>();
        needFertilizer(toFertilizePlants);
        RecyclerView fertilizeView = findViewById(R.id.ToFertilize);
        HomeAdaptor homeAdaptor = new HomeAdaptor(Home.this, toFertilizePlants, false);
        fertilizeView.setLayoutManager(new GridLayoutManager(Home.this, 3));
        fertilizeView.setAdapter(homeAdaptor);


        firebaseAuth = FirebaseAuth.getInstance();
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
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
     * Uses FireBaseAuth to verify to log user in if all fields are met.
     */
    private void logOut(){
        firebaseAuth.signOut();
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
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
