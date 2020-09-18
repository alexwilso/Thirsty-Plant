package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.thirstyplant.R;
import com.example.thirstyplant.io.DataBaseHelper;
import com.example.thirstyplant.model.Plant;

import java.util.List;

public class MyPlants extends AppCompatActivity {
    DataBaseHelper dataBaseHelper;
    ListView plantList;
    ArrayAdapter myPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        dataBaseHelper = new DataBaseHelper(MyPlants.this);
        plantList = findViewById(R.id.myPlantList);
        showAllPlants();
    }

    private void showAllPlants() {
        myPlants = new ArrayAdapter<Plant>(MyPlants.this, android.R.layout.simple_list_item_1,
                dataBaseHelper.getAllPlants());
        plantList.setAdapter(myPlants);
    }
}