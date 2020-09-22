package com.example.thirstyplant.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class FertilizeTimer {
    public static final int hoursInDay = 24;
    public static final int minutesInHour = 60;
    private int id;
    private String nextFertilize;
    private String fertilizeAtTime;
    private int howOften;

    public FertilizeTimer(int id, String nextFertilize, String waterAtTime, int howOften) {
        this.id = id;
        this.nextFertilize = nextFertilize;
        this.fertilizeAtTime = waterAtTime;
        this.howOften = howOften;
    }

    public int getId() {
        return id;
    }

    public String getNextFertilize() {
        return nextFertilize;
    }

    public int getHowOften() {
        return howOften;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNextFertilize(String nextFertilize) {
        this.nextFertilize = nextFertilize;
    }

    public void setHowOften(int howOften) {
        this.howOften = howOften;
    }

    public String getFertilizeAtTime() {
        return fertilizeAtTime;
    }

    public void setFertilizeAtTime(String fertilizeAtTime) {
        this.fertilizeAtTime = fertilizeAtTime;
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
        setNextFertilize(convertDatetoString(convertStringToDate(nextFertilize).plusDays(howOften)));
    }
}
