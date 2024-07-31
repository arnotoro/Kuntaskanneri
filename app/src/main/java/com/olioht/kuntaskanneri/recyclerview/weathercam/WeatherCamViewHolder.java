package com.olioht.kuntaskanneri.recyclerview.weathercam;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.kuntaskanneri.R;

public class WeatherCamViewHolder extends RecyclerView.ViewHolder {
    ImageView weatherCamImage;

    public WeatherCamViewHolder(@NonNull View itemView) {
        super(itemView);
        weatherCamImage = itemView.findViewById(R.id.weatherCamImage);

    }
}
