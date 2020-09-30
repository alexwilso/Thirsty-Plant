package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirstyplant.R;
import com.example.thirstyplant.adaptors.RecycleViewAdaptor;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;

import java.util.ArrayList;
import java.util.List;

public class MyPlants extends AppCompatActivity {
    DatabaseHelper plantDatabaseHelper;
//    ListView plantList;
    List<Plant> myPlantList;
    Button HomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        HomeScreen = findViewById(R.id.toHome);
        plantDatabaseHelper = new DatabaseHelper(MyPlants.this);
        myPlantList = new ArrayList<>();
        addPlants(myPlantList);
        RecyclerView myView = (RecyclerView) findViewById(R.id.recyclerPlants);
        RecycleViewAdaptor myAdaptor = new RecycleViewAdaptor(MyPlants.this, myPlantList);
        myView.setLayoutManager(new GridLayoutManager(MyPlants.this, 2));
        myView.setAdapter(myAdaptor);

        HomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHome();
            }
        });
    }

    /**
     * Adds plants in database to plant list
     */
    private void addPlants(List<Plant> plants){
        plants.addAll(plantDatabaseHelper.getAllPlants());
    }

    private void toHome(){
        Intent homescreen = new Intent(MyPlants.this, Home.class);
        startActivity(homescreen);
    }
}