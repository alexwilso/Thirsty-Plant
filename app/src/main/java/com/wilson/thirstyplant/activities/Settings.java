package com.wilson.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.services.ForegroundService;

public class Settings extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button logOut, toHome;
    SwitchCompat service;
    Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        firebaseAuth = FirebaseAuth.getInstance();
        logOut = findViewById(R.id.logOutButton);
        toHome = findViewById(R.id.toHome);
        service = findViewById(R.id.serviceSwitch);
        serviceIntent = new Intent(this, ForegroundService.class);

        service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startService();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Settings.this, Home.class);
                startActivity(home);
            }
        });
    }


    /**
     * Uses FireBaseAuth to verify to log user in if all fields are met.
     */
    private void logOut(){
        firebaseAuth.signOut();
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * If checked, foreground service started.
     */

    private void startService(){
        if (service.isChecked()){
            Toast.makeText(this, "Notifications will show while app is closed", Toast.LENGTH_LONG).show();
            ContextCompat.startForegroundService(this, serviceIntent);
        } else if (!service.isChecked()){
            Toast.makeText(this, "Notifications will not show while app is closed", Toast.LENGTH_LONG).show();
            stopService(serviceIntent);
        }
    }

}