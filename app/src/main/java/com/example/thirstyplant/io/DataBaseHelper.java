package com.example.thirstyplant.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.thirstyplant.model.Plant;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String PLANT_TABLE = "PLANT_TABLE";
    public static final String COLUMN_PLANT_NAME = "PLANT_NAME";
    public static final String COLUMN_PLANT_NICKNAME = "PLANT_NICKNAME";
    public static final String COLUMN_PLANT_LOCATION = "PLANT_LOCATION";
    public static final String COLUMN_DATE_ACQUIRED = "DATE_ACQUIRED";
    public static final String COLUMN_CARE_INSTRUCTIONS = "CARE_INSTRUCTIONS";
    public static final String COLUMN_WATERED = "WATERED";
    public static final String COLUMN_FERTILIZED = "FERTILIZED";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "plants", null, 1);
    }

    /**
     * Creates new database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + PLANT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLANT_NAME + " TEXT, " + COLUMN_PLANT_NICKNAME + " TEXT, " + COLUMN_PLANT_LOCATION
                + " TEXT, " + COLUMN_DATE_ACQUIRED + " TEXT, " + COLUMN_CARE_INSTRUCTIONS + " TEXT, "
                + COLUMN_WATERED + " BOOL, " + COLUMN_FERTILIZED + " BOOL)";
        db.execSQL(createTable);
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
                boolean plantWatered = cursor.getInt(6) == 1 ? true: false;
                boolean plantFertilized = cursor.getInt(7) == 1 ? true: false;
                Plant plant = new Plant(plantId, plantName, plantNickName, plantLocation, plantDate, plantCare, plantWatered, plantFertilized);
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
}
