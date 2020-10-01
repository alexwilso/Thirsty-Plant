package com.example.thirstyplant.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirstyplant.R;
import com.example.thirstyplant.model.Plant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class HomeAdaptor extends RecyclerView.Adapter<HomeAdaptor.MyViewHolder> {

    private Context Home;
    private List<Plant> plantList;
    private Boolean toWater;

    public HomeAdaptor(Context home, List<Plant> plantList, Boolean toWater) {
        Home = home;
        this.plantList = plantList;
        this.toWater = toWater;
    }

    public HomeAdaptor(Context home, List<Plant> plantList) {
        Home = home;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (toWater){
            LayoutInflater layoutInflater = LayoutInflater.from(Home);
            view = layoutInflater.inflate(R.layout.activity_to_water, parent, false);
            return new MyViewHolder(view);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(Home);
            view = layoutInflater.inflate(R.layout.activity_to_fertilize, parent, false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
            toDo = itemView.findViewById(R.id.toDoButton);
        }
    }
}
