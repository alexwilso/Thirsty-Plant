package com.example.thirstyplant.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.thirstyplant.model.FertilizeTimer;
import com.example.thirstyplant.model.Plant;
import com.example.thirstyplant.model.WaterTimer;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String PLANT_TABLE = "MY_PLANTS";
    public static final String COLUMN_PLANT_NAME = "PLANT_NAME";
    public static final String COLUMN_PLANT_NICKNAME = "PLANT_NICKNAME";
    public static final String COLUMN_PLANT_LOCATION = "PLANT_LOCATION";
    public static final String COLUMN_DATE_ACQUIRED = "DATE_ACQUIRED";
    public static final String COLUMN_CARE_INSTRUCTIONS = "CARE_INSTRUCTIONS";
    public static final String COLUMN_PHOTO_PATH = "PHOTO_PATH";
    public static final String COLUMN_WATERED = "WATERED";
    public static final String COLUMN_FERTILIZED = "FERTILIZED";
    public static final String COLUMN_ID = "ID";
    public static final String DATE_OF_NEXT_WATER = "DATE_OF_NEXT_WATER";
    public static final String TIME_OF_NEXT_WATER = "TIME_OF_NEXT_WATER";
    public static final String WATER_FREQUENCY = "HOW_OFTEN_WATER";
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
    public boolean addPlant(Plant plant) {
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
        return insert != -1;
    }

    /**
     * Returns all plants in database
     */
    public List<Plant> getAllPlants() {
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
                boolean plantWatered = cursor.getInt(13) == 1 ? true : false;
                boolean plantFertilized = cursor.getInt(14) == 1 ? true : false;
                Plant plant = new Plant(plantId, plantName, plantNickName, plantLocation, plantDate,
                        plantCare, photoPath, dateWater, timeWater, frequencyWater, dateFertilize,
                        timeFertilize, fertilizeFrequency, plantWatered, plantFertilized);
                returnList.add(plant);
            } while (cursor.moveToNext());
        } else {
            // Nothing is in list,
        }
        cursor.close();
        database.close();
        getInfo(returnList);
        return returnList;

    }

    /**
     * Returns all plants who are overdo to be watered
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Plant> toBeWatered(){
        List<Plant> toBeWatered = getAllPlants();
        DateFormat f = new SimpleDateFormat("yyyy-mm-dd");
        for (int x = 0; x < toBeWatered.size(); x++){
            Date water = f.parse(toBeWatered.get(x).getNextWaterDate(), new ParsePosition(0));
            Date today = f.parse(LocalDate.now().toString(), new ParsePosition(0));
            if (!(water.compareTo(today) <= 0)){
                toBeWatered.remove(x);
            }
        }

        return toBeWatered;
    }

    /**
     * Returns all plants who are overdo to be fertilzied
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Plant> toBeFertilized(){
        List<Plant> toBeFertilized = getAllPlants();
        DateFormat f = new SimpleDateFormat("yyyy-mm-dd");
        for (int x = 0; x < toBeFertilized.size(); x++) {
            if (toBeFertilized.get(x).getNextfertilizeDate().equals("N/A")) {
                toBeFertilized.remove(x);
            } else {
                Date fertilize = f.parse(toBeFertilized.get(x).getNextfertilizeDate(), new ParsePosition(0));
                Date today = f.parse(LocalDate.now().toString(), new ParsePosition(0));
                System.out.println(fertilize.compareTo(today));
                if (!(fertilize.compareTo(today) <= 0)) {
                    toBeFertilized.remove(x);
                }
            }
        }
        return toBeFertilized;
    }

    public void getInfo(List<Plant> plants){
        for (int x = 0; x < plants.size();x++){
            System.out.println(plants.get(x).getPlantName());
        }
    }


    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, PLANT_TABLE);
        db.close();
        return count;
    }

    /**
     * Deletes plants in database
     */
    public boolean deletePlant(int num) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryString = "DELETE FROM " + PLANT_TABLE + " WHERE " + COLUMN_ID + " = " + num;
        Cursor cursor = database.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }
}
