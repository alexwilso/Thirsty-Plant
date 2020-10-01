package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.thirstyplant.R;
import com.example.thirstyplant.Receivers.WaterReceiver;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class WaterSchedule extends AppCompatActivity {
    private EditText waterDate, waterTime, waterFrequency;
    private CheckBox yes;
    Button setSchedule;
    Calendar calendar;
    DatabaseHelper databaseHelper = new DatabaseHelper(WaterSchedule.this);
    JSONObject createPlant = new JSONObject();
    int notificationId = 100;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        // References to controls on layout
        waterDate = findViewById(R.id.dateTextWater);
        waterTime = findViewById(R.id.timeTextWater);
        waterFrequency = findViewById(R.id.waterSchedule);
        yes = findViewById(R.id.checkBoxYes);
        setSchedule = findViewById(R.id.setSchedule);
        calendar = Calendar.getInstance();
        createNotificationChannel();

        try {
            createPlant = new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("createPlant")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        waterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setDate(waterDate);
            }
        });

        waterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setTime(waterTime);
            }
        });

        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    toNext(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Throws error if text boxes left empty
     */
    private boolean missingData(){
        if (waterDate.getText().toString().isEmpty()){
            waterDate.setError("Please enter a date");
            waterDate.requestFocus();
            return true;
        }
        else if (waterTime.getText().toString().isEmpty()){
            waterTime.setError("Please enter a time");
            waterTime.requestFocus();
            return true;
        }
        else if (waterFrequency.getText().toString().isEmpty()){
            waterFrequency.setError("Please enter a frequency");
            waterFrequency.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * Checks if user entering a date in the past
     */
    private boolean inPast(int year, int month, int day){
        Calendar now = Calendar.getInstance();
        int curYear = Calendar.YEAR,  curMonth = Calendar.MONTH, curDay = Calendar.DAY_OF_MONTH;

        if (year < now.get(curYear)){
            Toast.makeText(WaterSchedule.this, "You can't water plants in the past", Toast.LENGTH_SHORT).show();
            return false; }
        else if (year == now.get(curYear) && month < now.get(curMonth)){
            Toast.makeText(WaterSchedule.this, "You can't water plants in the past", Toast.LENGTH_SHORT).show();
            return false;
            }
        else if (year == now.get(curYear) && month ==
                now.get(curMonth) && day < now.get(curDay)){
            Toast.makeText(WaterSchedule.this, "You can't water plants in the past", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Uses datepickerdialog to set desired date. Sets date in editText
     */
    private void setDate(final EditText editText){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(WaterSchedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (inPast(year, month, dayOfMonth)) {
                    month = month + 1;
                    String date = year + "-" + (month<10?("0"+month):(month)) + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
//                    String date = year + "-" + month + "-" + dayOfMonth;
                    editText.setText(date);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    /**
     * Uses timepickerdialog to set desired time. Sets time in editText
     */
    private void setTime(final EditText editText){
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(WaterSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                System.out.println(hourOfDay);
                String time = (hourOfDay<10?("0"+hourOfDay):(hourOfDay)) + ":" + (minute<10?("0"+minute):(minute));
//                String time = hourOfDay + ":" + minute;
                editText.setText(time);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    /**
     * Adds Plant to table
     */
    private boolean addPlant(JSONObject myPlant, View view) throws JSONException {
        Plant plant;
        if (!missingData()) {
            try {
                createPlant.put("nextWaterDate", waterDate.getText().toString());
                createPlant.put("nextWaterTimer", waterTime.getText().toString());
                createPlant.put("waterFrequency", waterFrequency.getText().toString());
                plant = new Plant(-1, myPlant.getString("Name"), myPlant.getString("NickName"),
                        myPlant.getString("Location"), myPlant.getString("Date"),
                        myPlant.getString("Instruction"), myPlant.getString("Path"),
                        myPlant.getString("nextWaterDate"), myPlant.getString("nextWaterTimer"),
                        myPlant.getString("waterFrequency"), myPlant.getString("nextFertilizeDate"),
                        myPlant.getString("nextFertilizeTime"), myPlant.getString("fertilizeFrequency"), id,
                        false, false);
            } catch (Exception e) {
                plant = new Plant(-1, "Error", "Error", "Error",
                        "Error", "Error", "Error", "Eror",
                        "Error", "Error", "Error", "Error",
                        "Error", false, false);
                Toast.makeText(WaterSchedule.this, "Error creating plant", Toast.LENGTH_LONG).show();
            }
            DatabaseHelper databaseHelper = new DatabaseHelper(WaterSchedule.this);
            boolean success = databaseHelper.addPlant(plant);
            if (success) {
                return true;
            }
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Sets time and date for alarm
     */
    public Calendar setTimeDate(){
        // Splits date into integers
        String[] arrOfString = waterDate.getText().toString().split("-");
        int year = Integer.parseInt(arrOfString[0]);
        int month = Integer.parseInt(arrOfString[1]);
        int day = Integer.parseInt(arrOfString[2]);

        // Splits time into integers
        String[] timeToInt = waterTime.getText().toString().split(":");
        int hour = Integer.parseInt(timeToInt[0]);
        int minute = Integer.parseInt(timeToInt[1]);

        // Sets calender time to time chosen by user
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(year, month -1, day, hour, minute, 0);
//
        return alarmTime;
    }

    /**
     * Creates notification channel for watering notifications
     */
    private void createNotificationChannel() {

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
     * Creates alarm at time chosen by user
     */
    public void createAlarm(View view) throws JSONException {
        Intent intent = new Intent(WaterSchedule.this, WaterReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("toWater", "Name: " + createPlant.getString("Name") + " Location: " + createPlant.getString("Location"));
        // Look at flag here
        PendingIntent pendingIntent = PendingIntent.getBroadcast(WaterSchedule.this,
                id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar alarmTime = setTimeDate();
        long alarmStartTime = alarmTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
    }

    /**
     * Takes user to next fertilize activity if yes is checked and to home activity if not
     */
    private void toNext(View view) throws JSONException {
        if (yes.isChecked()){
            createPlant.put("nextWaterDate", waterDate.getText().toString());
            createPlant.put("nextWaterTimer", waterTime.getText().toString());
            createPlant.put("waterFrequency", waterFrequency.getText().toString());
            createAlarm(view);
            Intent toFertilize = new Intent(WaterSchedule.this, FertilizeSchedule.class);
            toFertilize.putExtra("createPlant", createPlant.toString());
            startActivity(toFertilize);
        }
        else{
            createPlant.put("nextFertilizeDate", "N/A");
            createPlant.put("nextFertilizeTime", "N/A");
            createPlant.put("fertilizeFrequency", "N/A");
            id = (int) System.currentTimeMillis();
            if (addPlant(createPlant, view)){
                createAlarm(view);
                Intent noFertilize = new Intent(WaterSchedule.this, Home.class);
                startActivity(noFertilize);
            }
            else {
                Toast.makeText(WaterSchedule.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Prevents keyboard from opening
     */
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Prevents user from going back to add plant screen
     */

    @Override
    public void onBackPressed() {
    }
}