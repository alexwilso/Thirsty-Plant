package com.wilson.thirstyplant.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wilson.thirstyplant.R;
import com.wilson.thirstyplant.activities.DisplayPlant;
import com.wilson.thirstyplant.model.Plant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MyPlantAdaptor extends RecyclerView.Adapter<MyPlantAdaptor.MyViewHolder> {

    private Context myPlants;
    private List<Plant> plantList;


    public MyPlantAdaptor(Context myPlants, List<Plant> plantList) {
        this.myPlants = myPlants;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(myPlants);
        view = layoutInflater.inflate(R.layout.activity_plant_views, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
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
        holder.plantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayPlant = new Intent(myPlants, DisplayPlant.class);
                displayPlant.putExtra("Plant", plantList.get(position));
                myPlants.startActivity(displayPlant);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            plantName = itemView.findViewById(R.id.photoName);
            plantPhoto =  itemView.findViewById(R.id.plantPhoto);
            plantCard = itemView.findViewById(R.id.plantCard);
        }
    }
}
