package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirstyplant.R;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private Button logOutButton, addPlantButton, myPlants;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // References to controls on layout
        logOutButton = findViewById(R.id.logOutButton);
        addPlantButton = findViewById(R.id.addPlant);
        myPlants = findViewById(R.id.myPlants);
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

}
