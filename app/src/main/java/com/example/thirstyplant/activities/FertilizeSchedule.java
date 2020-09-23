package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.thirstyplant.R;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.FertilizeTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class FertilizeSchedule extends AppCompatActivity {
    private EditText fertilizeDate, fertilizeTime, fertilizeFrequency;
    Button setSchedule;
    Calendar calendar;
    JSONObject createPlant = new JSONObject();

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
                    addTimer();
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
                    editText.setText(date);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Throws error if text boxes left empty
     */
    private boolean missingDate(){
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
     * Adds timer info to watertimer table
     */
    private void addTimer() throws JSONException {
        createPlant.put("nextFertilizeDate", fertilizeDate.getText().toString());
        createPlant.put("nextFertilizeTime", fertilizeTime.getText().toString());
        createPlant.put("fertilizeFrequency", fertilizeFrequency.getText().toString());
        FertilizeTimer fertilizeTimer;
        if (!missingDate()){
            try {
                fertilizeTimer = new FertilizeTimer(-1, fertilizeDate.getText().toString(), fertilizeTime.getText().toString(),
                        Integer.parseInt(fertilizeFrequency.getText().toString()));
            } catch (Exception e){
                fertilizeTimer = new FertilizeTimer(-1, "error", "error", 0);
                Toast.makeText(FertilizeSchedule.this, "Error making timer", Toast.LENGTH_LONG).show();
            }
            DatabaseHelper databaseHelper = new DatabaseHelper(FertilizeSchedule.this);
            boolean success = databaseHelper.addFetrtizeTimer(fertilizeTimer);
            if (success){
                toNext();
            }
        }

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
    private void toNext(){
        System.out.println(createPlant);
        Intent toHome = new Intent(FertilizeSchedule.this, Home.class);
        startActivity(toHome);
    }
}