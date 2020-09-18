package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.thirstyplant.R;

import java.util.Calendar;

public class WaterFertilize extends AppCompatActivity {
    private EditText waterDate, fertilizeDate;
    private EditText waterTime, fertilizeTime;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fertilize);
        // References to controls on layout
        waterDate = findViewById(R.id.dateTextWater);
        fertilizeDate = findViewById(R.id.dateTextFertlize);
        waterTime = findViewById(R.id.timeTextWater);
        fertilizeTime = findViewById(R.id.timeTextFertlize);
        calendar = Calendar.getInstance();


        waterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setDate(waterDate);
            }
        });

        fertilizeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setDate(fertilizeDate);
            }
        });

        waterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setTime(waterTime);
            }
        });

        fertilizeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                setTime(fertilizeTime);
            }
        });
    }

/**
 * Uses datepickerdialog to set desired date. Sets date in editText
 */
    private void setDate(final EditText editText){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(WaterFertilize.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(WaterFertilize.this, new TimePickerDialog.OnTimeSetListener() {
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
}