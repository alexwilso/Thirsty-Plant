package com.example.thirstyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thirstyplant.R;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MyPlants extends AppCompatActivity {
    DatabaseHelper plantdatabasehelper;
    ListView plantList;
    ArrayAdapter myPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
        plantList = findViewById(R.id.displayPlants);
        plantdatabasehelper = new DatabaseHelper(MyPlants.this);
        showAllPlants();

        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plant plant = (Plant) parent.getItemAtPosition(position);
                plantdatabasehelper.deletePlant(plant);
                showAllPlants();
            }
        });
    }

    private void showAllPlants() {
        myPlants = new ArrayAdapter<Plant>(MyPlants.this, android.R.layout.simple_expandable_list_item_1, plantdatabasehelper.getAllPlants());
        plantList.setAdapter(myPlants);



//        for (int x = 0; x < plantdatabasehelper.getAllPlants().size(); x++){
//            String plantPath = plantArray.get(x).getPlantName() + "_" + plantArray.get(x).getNickName();
//            plantPicture(plantPath, plantArray.get(x).getPlantName());
//        }
//        plantList.setAdapter(myPlants);
    }


    /**
     * Adds picture to grid, if no picture, adds plant picture in drawable
     */
    private void plantPicture(String pathName, String name) {
        try {
            File file = new File(getString(R.string.plantPath), pathName + ".jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ImageView plantPhoto = new ImageView(MyPlants.this);
            TextView plantName = new TextView(MyPlants.this);
            plantPhoto.setImageBitmap(bitmap);
            plantName.setText(name);
            plantList.addView(plantPhoto);
            plantList.addView(plantName);
            plantPhoto.setScaleY(5);
            plantPhoto.setScaleX(5);
//            ImageView imageView = findViewById(R.id.imageView4);
//            imageView.setImageBitmap(bitmap);
//            imageView.setScaleY(5);
//            imageView.setScaleX(5);
        }
        catch (FileNotFoundException error){
            error.printStackTrace();
        } finally {
            ImageView plantPhotos = new ImageView(MyPlants.this);
            plantPhotos.setImageResource(R.drawable.plant);
        }
    }
}