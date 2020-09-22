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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.thirstyplant.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WaterSchedule extends AppCompatActivity {
    private EditText waterDate, waterTime, waterSchedule;
    private CheckBox yes;
    Button setSchedule;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        // References to controls on layout
        waterDate = findViewById(R.id.dateTextWater);
        waterTime = findViewById(R.id.timeTextWater);
        waterSchedule= findViewById(R.id.waterSchedule);
        yes = findViewById(R.id.checkBoxYes);
        setSchedule = findViewById(R.id.setSchedule);
        calendar = Calendar.getInstance();


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
                toNext();
            }
        });
/**
 * Uses datepickerdialog to set desired date. Sets date in editText
 */
    }


    private void setDate(final EditText editText){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(WaterSchedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + (month<10?("0"+month):(month)) + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                editText.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

/**
 * Uses timepickerdialog to set desired time. Sets time in editText
 */
    private void setTime(final EditText editText){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(WaterSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText(String.format(hourOfDay + ":" + minute));
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
     * Takes user to next fertilize activity if yes is checked and to home activity if not
     */
    private void toNext(){
        if (yes.isChecked()){
            Intent toFertilize = new Intent(WaterSchedule.this, FertilizeSchedule.class);
            startActivity(toFertilize);
        }
        else{
            Intent noFertilize = new Intent(WaterSchedule.this, Home.class);
            startActivity(noFertilize);
        }
    }
}