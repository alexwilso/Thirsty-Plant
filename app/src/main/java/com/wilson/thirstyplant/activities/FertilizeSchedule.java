package com.wilson.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.receivers.FertilizeReceiver;
import com.wilson.thirstyplant.io.DatabaseHelper;
import com.wilson.thirstyplant.model.Plant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class FertilizeSchedule extends AppCompatActivity {
    public static final String IN_THE_PAST = "You can't fertilize plants in the past";
    public static final String NEXT_FERTILIZE_DATE = "nextFertilizeDate";
    public static final String NEXT_FERTILIZE_TIME = "nextFertilizeTime";
    public static final String FERTILIZE_FREQUENCY = "fertilizeFrequency";
    public static final String WATER_TIMER = "nextWaterTimer";
    public static final String PATH = "Path";
    public static final String DATE = "Date";
    public static final String ERROR = "Error";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String TO_FERTILIZE = "toFertilize";
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fertilizeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(fertilizeDate);
            }
        });

        fertilizeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            Toast.makeText(FertilizeSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return false; }
        else if (year == now.get(curYear) && month < now.get(curMonth)){
            Toast.makeText(FertilizeSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (year == now.get(curYear) && month ==
                now.get(curMonth) && day < now.get(curDay)){
            Toast.makeText(FertilizeSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return true;
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
     * Throws error if text boxes left activity_empty
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
     * Adds plant info to plant table
     */
    private void addPlant(JSONObject myPlant, View view) throws JSONException {
        Plant plant;
        if (!missingData()){
            try {
                createPlant.put(NEXT_FERTILIZE_DATE, fertilizeDate.getText().toString());
                createPlant.put(NEXT_FERTILIZE_TIME, fertilizeTime.getText().toString());
                createPlant.put(FERTILIZE_FREQUENCY, fertilizeFrequency.getText().toString());
                plant = new Plant(-1, myPlant.getString("Name"), myPlant.getString("NickName"),
                        myPlant.getString("Location"), myPlant.getString(DATE),
                        myPlant.getString("Instruction"), myPlant.getString(PATH),
                        myPlant.getString("nextWaterDate"), myPlant.getString(WATER_TIMER),
                        myPlant.getString("waterFrequency"), myPlant.getString(NEXT_FERTILIZE_DATE),
                        myPlant.getString(NEXT_FERTILIZE_TIME), myPlant.getString(FERTILIZE_FREQUENCY), id,
                        false, false);
            } catch (Exception e){
                plant = new Plant(-1, ERROR, ERROR, ERROR,
                        ERROR, ERROR, ERROR, ERROR,
                        ERROR, ERROR, ERROR, ERROR,
                        ERROR, 1, false, false);
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
     * Creates alarm at time chosen by user
     */
    public void createAlarm(View view) throws JSONException {
        Intent intent = new Intent(getApplicationContext(), FertilizeReceiver.class);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TO_FERTILIZE, "Name: " + createPlant.getString("Name") + " Location: " + createPlant.getString("Location"));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
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