package com.wilson.thirstyplant.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.receivers.WaterReceiver;
import com.wilson.thirstyplant.io.DatabaseHelper;
import com.wilson.thirstyplant.model.Plant;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Objects;

public class WaterSchedule extends AppCompatActivity {
    public static final String NEXT_WATER_DATE = "nextWaterDate";
    public static final String NEXT_WATER_TIMER = "nextWaterTimer";
    public static final String WATER_FREQUENCY = "waterFrequency";
    public static final String NEXT_FERTILIZE_DATE = "nextFertilizeDate";
    public static final String NEXT_FERTILIZE_TIME = "nextFertilizeTime";
    public static final String FERTILIZE_FREQUENCY = "fertilizeFrequency";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String TO_WATER = "toWater";
    public static final String IN_THE_PAST = "You can't water plants in the past";
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


        try {
            createPlant = new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("createPlant")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        waterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(waterDate);
            }
        });

        waterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(waterTime);
            }
        });

        setSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
     * Throws error if text boxes left activity_empty
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
            Toast.makeText(WaterSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return false; }
        else if (year == now.get(curYear) && month < now.get(curMonth)){
            Toast.makeText(WaterSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return false;
            }
        else if (year == now.get(curYear) && month ==
                now.get(curMonth) && day < now.get(curDay)){
            Toast.makeText(WaterSchedule.this, IN_THE_PAST, Toast.LENGTH_SHORT).show();
            return true; // Change this back to false
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
                editText.setText(time);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    /**
     * Adds Plant to table
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean addPlant(JSONObject myPlant, View view) throws JSONException {
        Plant plant;
        if (!missingData()) {
            try {
                createPlant.put(NEXT_WATER_DATE, waterDate.getText().toString());
                createPlant.put(NEXT_WATER_TIMER, waterTime.getText().toString());
                createPlant.put(WATER_FREQUENCY, waterFrequency.getText().toString());
                plant = new Plant(-1, myPlant.getString("Name"), myPlant.getString("NickName"),
                        myPlant.getString("Location"), myPlant.getString("Date"),
                        myPlant.getString("Instruction"), myPlant.getString("Path"),
                        myPlant.getString(NEXT_WATER_DATE), myPlant.getString(NEXT_WATER_TIMER),
                        myPlant.getString(WATER_FREQUENCY), myPlant.getString(NEXT_FERTILIZE_DATE),
                        myPlant.getString(NEXT_FERTILIZE_TIME), myPlant.getString("fertilizeFrequency"), id,
                        false, false);
            } catch (Exception e) {
                plant = new Plant(-1, "Error", "Error", "Error",
                        "Error", "Error", "Error", "Eror",
                        "Error", "Error", "Error", "Error",
                        "Error", 1, false, false);
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
     * */
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
     * Creates alarm at time chosen by user
     */
    public void createAlarm(View view) throws JSONException {
        Intent intent = new Intent(getApplicationContext(), WaterReceiver.class);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra(TO_WATER, "Name: " + createPlant.getString("Name") + " Location: " + createPlant.getString("Location"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar alarmTime = setTimeDate();
        long alarmStartTime = alarmTime.getTimeInMillis();
//
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);

    }

    /**
     * Takes user to next fertilize activity if yes is checked and to home activity if not
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toNext(View view) throws JSONException {
        if (yes.isChecked()){
            createPlant.put(NEXT_WATER_DATE, waterDate.getText().toString());
            createPlant.put(NEXT_WATER_TIMER, waterTime.getText().toString());
            createPlant.put(WATER_FREQUENCY, waterFrequency.getText().toString());
            createAlarm(view);
            Intent toFertilize = new Intent(WaterSchedule.this, FertilizeSchedule.class);
            toFertilize.putExtra("createPlant", createPlant.toString());
            startActivity(toFertilize);
        }
        else{
            createPlant.put(NEXT_FERTILIZE_DATE, "N/A");
            createPlant.put(NEXT_FERTILIZE_TIME, "N/A");
            createPlant.put(FERTILIZE_FREQUENCY, "N/A");
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
     * Prevents user from going back to add plant screen
     */

    @Override
    public void onBackPressed() {
    }
}