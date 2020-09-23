package com.example.thirstyplant.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class FertilizeTimer extends Timer{

    public FertilizeTimer(int id, String nextDateToDo, String nextTimeToDo, int howOften) {
        super(id, nextDateToDo, nextTimeToDo, howOften);
    }
}