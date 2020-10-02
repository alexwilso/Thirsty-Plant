package com.example.thirstyplant.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirstyplant.R;
import com.example.thirstyplant.activities.Home;
import com.example.thirstyplant.io.DatabaseHelper;
import com.example.thirstyplant.model.Plant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FertilizeAdaptor extends RecyclerView.Adapter<FertilizeAdaptor.MyViewHolder>{


    private Context home;
    private List<Plant> plantList;
    DatabaseHelper databaseHelper;

    public FertilizeAdaptor(Context home, List<Plant> plantList) {
        this.home = home;
        this.plantList = plantList;
    }


    @NonNull
    @Override
    public FertilizeAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        databaseHelper = new DatabaseHelper(home);

            LayoutInflater layoutInflater = LayoutInflater.from(home);
            view = layoutInflater.inflate(R.layout.activity_to_fertilize, parent, false);
            return new FertilizeAdaptor.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FertilizeAdaptor.MyViewHolder holder, final int position) {
        holder.plantName.setText(plantList.get(position).getPlantName());
        if (plantList.get(position).getPhotoSource().equals("app/src/main/res/drawable/plant.png")){
            holder.plantPhoto.setImageResource(R.drawable.plant);
        }
        else {
            File file = new File(plantList.get(position).getPhotoSource());
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                holder.plantPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        holder.toDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    plantList.get(position).fertilized();
                    databaseHelper.fertilizePlant(plantList.get(position));
                    Intent toHome = new Intent(home, Home.class);
                    home.startActivity(toHome);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView plantName;
        ImageView plantPhoto;
        CardView plantCard;
        Button toDo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            plantName = itemView.findViewById(R.id.toDoName);
            plantPhoto = itemView.findViewById(R.id.toDoPhoto);
            plantCard =  itemView.findViewById(R.id.toDoCard);
            toDo = itemView.findViewById(R.id.ToDoFertilize);
        }
    }
}