package com.example.thirstyplant.model;

import android.icu.util.Freezable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Plant implements Serializable {
    private int id;
    private String plantName;
    private String nickName;
    private String location;
    private String dateAcquired;
    private String careInstructions;
    private String photoSource;
    private String nextWaterDate;
    private String nextWaterTimer;
    private String waterFequency;
    private String nextfertilizeDate;
    private String getNextfertilizeTime;
    private String fertilizeFrequency;
    private int notification_id;
    private boolean watered;
    private boolean fertilized;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Plant(int id, String plantName, String nickName, String location, String dateAcquired, String careInstructions, String photoSource, String nextWaterDate, String nextWaterTimer, String waterFequency, String nextfertilizeDate, String getNextfertilizeTime, String fertilizeFrequency, int notification_id, boolean watered, boolean fertilized) {
        this.id = id;
        this.plantName = plantName;
        this.nickName = nickName;
        this.location = location;
        this.dateAcquired = dateAcquired;
        this.careInstructions = careInstructions;
        this.photoSource = photoSource;
        this.nextWaterDate = nextWaterDate;
        this.nextWaterTimer = nextWaterTimer;
        this.waterFequency = waterFequency;
        this.nextfertilizeDate = nextfertilizeDate;
        this.getNextfertilizeTime = getNextfertilizeTime;
        this.fertilizeFrequency = fertilizeFrequency;
        this.notification_id = notification_id;
        this.watered = watered;
        this.fertilized = fertilized;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "id=" + id +
                ", plantName='" + plantName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", location='" + location + '\'' +
                ", dateAcquired='" + dateAcquired + '\'' +
                ", careInstructions='" + careInstructions + '\'' +
                ", photoSource='" + photoSource + '\'' +
                ", nextWaterDate='" + nextWaterDate + '\'' +
                ", nextWaterTimer='" + nextWaterTimer + '\'' +
                ", waterFequency='" + waterFequency + '\'' +
                ", nextfertilizeDate='" + nextfertilizeDate + '\'' +
                ", getNextfertilizeTime='" + getNextfertilizeTime + '\'' +
                ", fertilizeFrequency='" + fertilizeFrequency + '\'' +
                ", watered=" + watered +
                ", fertilized=" + fertilized +
                '}';
    }

    public String getNextWaterDate() {
        return nextWaterDate;
    }

    public String getNextWaterTimer() {
        return nextWaterTimer;
    }

    public long getWaterFequency() {
        return Long.parseLong(waterFequency);
    }

    public String getNextfertilizeDate() {
        return nextfertilizeDate;
    }

    public String getGetNextfertilizeTime() {
        return getNextfertilizeTime;
    }

    public String getFertilizeFrequency() {
        return fertilizeFrequency;
    }
    public long howOftenFertilize(){
        return Long.parseLong(getFertilizeFrequency());
    }

    public void setNextWaterDate(String nextWaterDate) {
        this.nextWaterDate = nextWaterDate;
    }

    public void setNextWaterTimer(String nextWaterTimer) {
        this.nextWaterTimer = nextWaterTimer;
    }

    public void setWaterFequency(String waterFequency) {
        this.waterFequency = waterFequency;
    }

    public void setNextfertilizeDate(String nextfertilizeDate) {
        this.nextfertilizeDate = nextfertilizeDate;
    }

    public void setGetNextfertilizeTime(String getNextfertilizeTime) {
        this.getNextfertilizeTime = getNextfertilizeTime;
    }

    public void setFertilizeFrequency(String fertilizeFrequency) {
        this.fertilizeFrequency = fertilizeFrequency;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLocation() {
        return location;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public boolean isWatered() {
        return watered;
    }

    public boolean isFertilized() {
        return fertilized;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public String getPhotoSource() {
        return photoSource;
    }

    public void setPhotoSource(String photoSource) {
        this.photoSource = photoSource;
    }

    public String getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(String dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public void setWatered(boolean watered) {
        this.watered = watered;
    }

    public void setFertilized(boolean fertilized) {
        this.fertilized = fertilized;
    }

    public void waterPlant() throws Exception {
        if (watered){
            throw new Exception("Plant does not need to be watered");
        } else {
            setWatered(true);
        }
    }

    public void fertilizePlant() throws Exception {
        if(fertilized){
            throw new Exception("Plant does not need to be fertilized");
        } else {
            setFertilized(true);
        }
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
        setNextWaterDate(convertDatetoString(convertStringToDate(getNextWaterDate()).plusDays(getWaterFequency())));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fertilized() throws Exception {
        if (getNextfertilizeDate().equals("N/A")){
            throw new Exception("Plant does not need to be fertilized");
        }
        else {
            setNextfertilizeDate(convertDatetoString(convertStringToDate(getNextfertilizeDate()).plusDays(howOftenFertilize())));
        }
    }

    /**
     * returns long value of milliseconds between today and time of next care
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long timeUntilCare(String date) {
        // Changes today's date to string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String stringToday = localDateTime.format(dateTimeFormatter);

        // Sets format for dates
        Date careDate = null;
        Date todayDate = null;

        try{
            careDate = format.parse(date);
            todayDate = format.parse(stringToday);
            assert careDate != null;
            long diff = careDate.getTime() - todayDate.getTime();
            if (diff < 0){
                return 300000;
            }
            else {
                return diff;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 30000;
    }
}
