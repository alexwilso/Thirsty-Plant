package com.example.thirstyplant.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Calendar;

public class WaterTimer {
    public static final int hoursInDay = 24;
    public static final int minutesInHour = 60;
    private int id;
    private String nextWater;
    private String waterAtTime;
    private int howOften;

    public WaterTimer(int id, String nextWater, String waterAtTime, int howOften) {
        this.id = id;
        this.nextWater = nextWater;
        this.waterAtTime = waterAtTime;
        this.howOften = howOften;
    }

    public int getId() {
        return id;
    }

    public String getNextWater() {
        return nextWater;
    }

    public int getHowOften() {
        return howOften;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNextWater(String nextWater) {
        this.nextWater = nextWater;
    }

    public void setHowOften(int howOften) {
        this.howOften = howOften;
    }

    public String getWaterAtTime() {
        return waterAtTime;
    }

    public void setWaterAtTime(String waterAtTime) {
        this.waterAtTime = waterAtTime;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate convertStringToDate(String date){
        return LocalDate.parse(date);
    }

    public String convertDatetoString(LocalDate date){
        return date.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void watered(){
        setNextWater(convertDatetoString(convertStringToDate(nextWater).plusDays(howOften)));
    }
}
