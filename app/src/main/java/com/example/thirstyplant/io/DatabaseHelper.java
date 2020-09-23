package com.example.thirstyplant.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.thirstyplant.model.FertilizeTimer;
import com.example.thirstyplant.model.Plant;
import com.example.thirstyplant.model.WaterTimer;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String PLANT_TABLE = "MY_PLANTS";
    public static final String COLUMN_PLANT_NAME = "PLANT_NAME";
    public static final String COLUMN_PLANT_NICKNAME = "PLANT_NICKNAME";
    public static final String COLUMN_PLANT_LOCATION = "PLANT_LOCATION";
    public static final String COLUMN_DATE_ACQUIRED = "DATE_ACQUIRED";
    public static final String COLUMN_CARE_INSTRUCTIONS = "CARE_INSTRUCTIONS";
    public static  final String COLUMN_PHOTO_PATH = "PHOTO_PATH";
    public static final String COLUMN_WATERED = "WATERED";
    public static final String COLUMN_FERTILIZED = "FERTILIZED";
    public static final String COLUMN_ID = "ID";
    public static final String WATER_TABLE = "WATER_TIMERS";
    public static final String DATE_OF_NEXT_WATER = "DATE_OF_NEXT_WATER";
    public static final String TIME_OF_NEXT_WATER = "TIME_OF_NEXT_WATER";
    public static final String WATER_FREQUENCY = "HOW_OFTEN_WATER";
    public static final String FERTILIZE_TABLE = "FERTILIZE_TIMERS";
    public static final String DATE_OF_NEXT_FERTILIZE = "DATE_OF_NEXT_FERTILIZE";
    public static final String TIME_OF_NEXT_FERTILIZE = "TIME_OF_NEXT_FERTILIZE";
    public static final String FERTILIZE_FREQUENCY = "HOW_OFTEN_FERTILIZE";



    public DatabaseHelper(@Nullable Context context) {
        super(context, "MyPlants", null, 3);
    }

    /**
     * Creates new plant, water and fertilizer timer tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String plantTable = "CREATE TABLE " + PLANT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLANT_NAME + " TEXT, " + COLUMN_PLANT_NICKNAME + " TEXT, " + COLUMN_PLANT_LOCATION
                + " TEXT, " + COLUMN_DATE_ACQUIRED + " TEXT, " + COLUMN_CARE_INSTRUCTIONS + " TEXT, "
                + COLUMN_PHOTO_PATH + " TEXT, " + DATE_OF_NEXT_WATER + " TEXT, " + TIME_OF_NEXT_WATER + " TEXT, " +
                WATER_FREQUENCY + " TEXT, " + DATE_OF_NEXT_FERTILIZE + " TEXT, " + TIME_OF_NEXT_FERTILIZE + " TEXT, " +
                FERTILIZE_FREQUENCY + " TEXT, " + COLUMN_WATERED + " BOOL, " + COLUMN_FERTILIZED + " BOOL)";
        db.execSQL(plantTable);

        String waterTimers = "CREATE TABLE " + WATER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_OF_NEXT_WATER + " TEXT, " + TIME_OF_NEXT_WATER + " TEXT, " + WATER_FREQUENCY + " INTEGER)";
        db.execSQL(waterTimers);

        String fertilizeTimers = "CREATE TABLE " + FERTILIZE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_OF_NEXT_FERTILIZE + " TEXT, " + TIME_OF_NEXT_FERTILIZE + " TEXT, " + FERTILIZE_FREQUENCY + " INTEGER)";
        db.execSQL(fertilizeTimers);

    }

    /**
     * Prevents users app from crashing with change in database design
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Adds a new plant to database
     */
    public boolean addPlant(Plant plant){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLANT_NAME, plant.getPlantName());
        cv.put(COLUMN_PLANT_NICKNAME, plant.getNickName());
        cv.put(COLUMN_PLANT_LOCATION, plant.getLocation());
        cv.put(COLUMN_DATE_ACQUIRED, plant.getDateAcquired());
        cv.put(COLUMN_CARE_INSTRUCTIONS, plant.getCareInstructions());
        cv.put(COLUMN_PHOTO_PATH, plant.getPhotoSource());
        cv.put(DATE_OF_NEXT_WATER, plant.getNextWaterDate());
        cv.put(TIME_OF_NEXT_WATER, plant.getNextWaterTimer());
        cv.put(WATER_FREQUENCY, plant.getWaterFequency());
        cv.put(DATE_OF_NEXT_FERTILIZE, plant.getNextfertilizeDate());
        cv.put(TIME_OF_NEXT_FERTILIZE, plant.getGetNextfertilizeTime());
        cv.put(FERTILIZE_FREQUENCY, plant.getFertilizeFrequency());
        cv.put(COLUMN_WATERED, plant.isWatered());
        cv.put(COLUMN_FERTILIZED, plant.isFertilized());
        long insert = database.insert(PLANT_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Returns all plants in database
     */
    public List<Plant> getAllPlants(){
        List<Plant> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + PLANT_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(queryString, null);
        // if there are results, creates plant objects and puts them in return list
        if (cursor.moveToFirst()) {
            do {
                int plantId = cursor.getInt(0);
                String plantName = cursor.getString(1);
                String plantNickName = cursor.getString(2);
                String plantLocation = cursor.getString(3);
                String plantDate = cursor.getString(4);
                String plantCare = cursor.getString(5);
                String photoPath = cursor.getString(6);
                String dateWater = cursor.getString(7);
                String timeWater = cursor.getString(8);
                String frequencyWater = cursor.getString(9);
                String dateFertilize = cursor.getString(10);
                String timeFertilize = cursor.getString(11);
                String fertilizeFrequency = cursor.getString(12);
                boolean plantWatered = cursor.getInt(13) == 1 ? true: false;
                boolean plantFertilized = cursor.getInt(14) == 1 ? true: false;
                Plant plant = new Plant(plantId, plantName, plantNickName, plantLocation, plantDate,
                        plantCare, photoPath, dateWater, timeWater, frequencyWater, dateFertilize,
                        timeFertilize, fertilizeFrequency, plantWatered, plantFertilized);
                returnList.add(plant);
            } while (cursor.moveToNext());
        }
        else {
            // Nothing is in list,
        }
        cursor.close();
        database.close();
        return returnList;

    }

    /**
     * Deletes plants in database
     */
    public boolean deletePlant(Plant plant){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + PLANT_TABLE + " WHERE " + COLUMN_ID + " = " + plant.getId();
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    /**
     * Adds info for water timer
     */
    public boolean addWaterTimer(WaterTimer waterTimer) {
        SQLiteDatabase datebase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DATE_OF_NEXT_WATER, waterTimer.getNextDateToDo());
        cv.put(TIME_OF_NEXT_WATER, waterTimer.getNextTimeToDo());
        cv.put(WATER_FREQUENCY, waterTimer.getHowOften());
        long insert = datebase.insert(WATER_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Deletes Water timers in table
     */
    public boolean deleteWaterTimer(WaterTimer waterTimer){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + WATER_TABLE + " WHERE " + COLUMN_ID + " = " + waterTimer.getId();
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }

    /**
     * Adds info for Fertilize timer
     */
    public boolean addFetrtizeTimer(FertilizeTimer fertilizeTimer) {
        SQLiteDatabase datebase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DATE_OF_NEXT_FERTILIZE, fertilizeTimer.getNextDateToDo());
        cv.put(TIME_OF_NEXT_FERTILIZE, fertilizeTimer.getNextTimeToDo());
        cv.put(FERTILIZE_FREQUENCY, fertilizeTimer.getHowOften());
        long insert = datebase.insert(FERTILIZE_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Deletes Fertilize timers in table
     */
    public boolean deleteFertilizeTimer(WaterTimer waterTimer){
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FERTILIZE_TABLE + " WHERE " + COLUMN_ID + " = " + waterTimer.getId();
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}
