package com.wilson.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.adaptors.MyPlantAdaptor;
import com.wilson.thirstyplant.io.DatabaseHelper;
import com.wilson.thirstyplant.model.Plant;
import java.util.ArrayList;
import java.util.List;

public class MyPlants extends AppCompatActivity {
    DatabaseHelper plantDatabaseHelper;
    List<Plant> myPlantList;
    Button HomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        HomeScreen = findViewById(R.id.toDisplay);
        plantDatabaseHelper = new DatabaseHelper(MyPlants.this);
        myPlantList = new ArrayList<>();
        addPlants(myPlantList);

        RecyclerView myView = findViewById(R.id.recyclerPlants);
        MyPlantAdaptor myAdaptor = new MyPlantAdaptor(MyPlants.this, myPlantList);
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


    /**
     * Takes user back to home screen
     */
    private void toHome(){
        Intent homescreen = new Intent(MyPlants.this, Home.class);
        startActivity(homescreen);
    }
}