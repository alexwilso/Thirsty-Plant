package com.example.thirstyplant.model;

import android.icu.util.LocaleData;

import java.time.LocalDate;

public class Plant {
    private int id;
    private String plantName;
    private String nickName;
    private String location;
    private String dateAcquired;
    private String careInstructions;
    private String photoSource;
    private boolean watered;
    private boolean fertilized;

    public Plant(int id, String plantName, String nickName, String location, String dateAcquired, String careInstructions, String photoSource, boolean watered, boolean fertilized) {
        this.id = id;
        this.plantName = plantName;
        this.nickName = nickName;
        this.location = location;
        this.dateAcquired = dateAcquired;
        this.careInstructions = careInstructions;
        this.photoSource = photoSource;
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
                ", watered=" + watered +
                ", fertilized=" + fertilized +
                '}';
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
}
