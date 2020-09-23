package com.example.thirstyplant.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class Timer {
    public static final int hoursInDay = 24;
    public static final int minutesInHour = 60;
    private int id;
    private String nextDateToDo;
    private String nextTimeToDo;
    private int howOften;

    public Timer(int id, String nextDateToDo, String nextTimeToDo, int howOften) {
        this.id = id;
        this.nextDateToDo = nextDateToDo;
        this.nextTimeToDo = nextTimeToDo;
        this.howOften = howOften;
    }

    public int getId() {
        return id;
    }

    public String getNextDateToDo() {
        return nextDateToDo;
    }

    public int getHowOften() {
        return howOften;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNextDateToDo(String nextDateToDo) {
        this.nextDateToDo = nextDateToDo;
    }

    public void setHowOften(int howOften) {
        this.howOften = howOften;
    }

    public String getNextTimeToDo() {
        return nextTimeToDo;
    }

    public void setNextTimeToDo(String nextTimeToDo) {
        this.nextTimeToDo = nextTimeToDo;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate convertStringToDate(String date){
        return LocalDate.parse(date);
    }

    public String convertDatetoString(LocalDate date){
        return date.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void wateredFertilized(){
        setNextDateToDo(convertDatetoString(convertStringToDate(nextDateToDo).plusDays(howOften)));
    }
}
