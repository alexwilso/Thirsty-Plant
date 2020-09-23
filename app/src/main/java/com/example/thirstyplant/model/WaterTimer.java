package com.example.thirstyplant.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Calendar;

public class WaterTimer extends Timer{
    public WaterTimer(int id, String nextDateToDo, String nextTimeToDo, int howOften) {
        super(id, nextDateToDo, nextTimeToDo, howOften);
    }
}
