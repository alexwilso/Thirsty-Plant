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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.thirstyplant.R;
import com.example.thirstyplant.Receivers.FertilizeReceiver;
import com.example.thirstyplant.Receivers.WaterReceiver;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class FertilizeSchedule extends AppCompatActivity {
    private EditText fertilizeDate, fertilizeTime, fertilizeFrequency;
    Button setSchedule;
    Calendar calendar;
    JSONObject createPlant = new JSONObject();
    int notificationId = 200;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilize);
        fertilizeDate = findViewById(R.id.dateTextFertlize);
        fertilizeTime = findViewById(R.id.timeTextFertlize);
        fertilizeFrequency = findViewById(R.id.fertSchedule);
        setSchedule = findViewById(R.id.button2);
        calendar = Calendar.getInstance();
        try {
            createPlant = new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("createPlant")));
            createNotificationChannel();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fertilizeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setDate(fertilizeDate);
            }
        });

        fertilizeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setTime(fertilizeTime);
            }
        });

        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    id = (int) System.currentTimeMillis();
                    addPlant(createPlant, v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Checks if user entering a date in the past
     */
    private boolean inPast(int year, int month, int day){
        Calendar now = Calendar.getInstance();
        int curYear = Calendar.YEAR,  curMonth = Calendar.MONTH, curDay = Calendar.DAY_OF_MONTH;

        if (year < now.get(curYear)){
            Toast.makeText(FertilizeSchedule.this, "You can't fertilize plants in the past", Toast.LENGTH_SHORT).show();
            return false; }
        else if (year == now.get(curYear) && month < now.get(curMonth)){
            Toast.makeText(FertilizeSchedule.this, "You can't fertilize plants in the past", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (year == now.get(curYear) && month ==
                now.get(curMonth) && day < now.get(curDay)){
            Toast.makeText(FertilizeSchedule.this, "You can't fertilize plants in the past", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     /**
     * Uses datepickerdialog to set desired date. Sets date in editText
     */
    private void setDate(final EditText editText){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(FertilizeSchedule.this, new DatePickerDialog.OnDateSetListener() {
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
     * Throws error if text boxes left empty
     */
    private boolean missingData(){
        if (fertilizeDate.getText().toString().isEmpty()){
            fertilizeDate.setError("Please enter a date");
            fertilizeDate.requestFocus();
            return true;
        }
        else if (fertilizeTime.getText().toString().isEmpty()){
            fertilizeTime.setError("Please enter a time");
            fertilizeTime.requestFocus();
            return true;
        }
        else if (fertilizeFrequency.getText().toString().isEmpty()){
            fertilizeFrequency.setError("Please enter a frequency");
            fertilizeFrequency.requestFocus();
        }
        return false;
    }

    /**
     * Uses timepickerdialog to set desired time. Sets time in editText
     */
    private void setTime(final EditText editText){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(FertilizeSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = (hourOfDay<10?("0"+hourOfDay):(hourOfDay)) + ":" + (minute<10?("0"+minute):(minute));
//                String time = hourOfDay + ":" + minute;
                editText.setText(time);
            }
        }, hour, minute, false);
        timePickerDialog.show();
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
     * Adds plant info to plant table
     */
    private void addPlant(JSONObject myPlant, View view) throws JSONException {
        Plant plant;
        if (!missingData()){
            try {
                createPlant.put("nextFertilizeDate", fertilizeDate.getText().toString());
                createPlant.put("nextFertilizeTime", fertilizeTime.getText().toString());
                createPlant.put("fertilizeFrequency", fertilizeFrequency.getText().toString());
                plant = new Plant(-1, myPlant.getString("Name"), myPlant.getString("NickName"),
                        myPlant.getString("Location"), myPlant.getString("Date"),
                        myPlant.getString("Instruction"), myPlant.getString("Path"),
                        myPlant.getString("nextWaterDate"), myPlant.getString("nextWaterTimer"),
                        myPlant.getString("waterFrequency"), myPlant.getString("nextFertilizeDate"),
                        myPlant.getString("nextFertilizeTime"), myPlant.getString("fertilizeFrequency"), id,
                        false, false);
            } catch (Exception e){
                plant = new Plant(-1, "Error", "Error", "Error",
                        "Error", "Error", "Error", "Eror",
                        "Error", "Error", "Error", "Error",
                        "Error", 1, false, false);
                Toast.makeText(FertilizeSchedule.this, "Error creating plant", Toast.LENGTH_LONG).show();
            }
            DatabaseHelper databaseHelper = new DatabaseHelper(FertilizeSchedule.this);
            boolean success = databaseHelper.addPlant(plant);
            if (success) {
                toNext(view);
            }
        }
    }

    /**
     * Sets time and date for alarm
     */
    public Calendar setTimeDate(){
        // Splits date into integers
        String[] arrOfString = fertilizeDate.getText().toString().split("-");
        int year = Integer.parseInt(arrOfString[0]);
        int month = Integer.parseInt(arrOfString[1]);
        int day = Integer.parseInt(arrOfString[2]);

        // Splits time into integers
        String[] timeToInt = fertilizeTime.getText().toString().split(":");
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
            CharSequence charSequence = "Fertilizing Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("FertilizeAlarm", charSequence, importance);
            channel.setDescription("Alarm for fertilizing");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates alarm at time chosen by user
     */
    public void createAlarm(View view) throws JSONException {
        Intent intent = new Intent(FertilizeSchedule.this, FertilizeReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("toFertilize", "Name: " + createPlant.getString("Name") + " Location: " + createPlant.getString("Location"));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(FertilizeSchedule.this,
                id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar alarmTime = setTimeDate();
        long alarmStartTime = alarmTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
    }

    /**
     * Prevents user from going back to add water timer screen
     */

    @Override
    public void onBackPressed() {
        Toast.makeText(FertilizeSchedule.this, "Oops messed up? You can delete this plant later", Toast.LENGTH_SHORT).show();
    }

    /**
     * Takes user to home screen
     */
    private void toNext(View view) throws JSONException {
        createAlarm(view);
        Intent toHome = new Intent(FertilizeSchedule.this, Home.class);
        startActivity(toHome);
    }
}