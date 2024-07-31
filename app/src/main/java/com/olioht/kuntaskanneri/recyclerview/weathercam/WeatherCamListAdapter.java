package com.olioht.kuntaskanneri.recyclerview.weathercam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olioht.kuntaskanneri.MunicipalityPage;
import com.olioht.kuntaskanneri.R;
import com.olioht.kuntaskanneri.api.TrafficData;
import com.olioht.kuntaskanneri.recyclerview.searched.SearchedViewHolder;

import java.util.List;

public class WeatherCamListAdapter extends RecyclerView.Adapter<WeatherCamViewHolder> {
    private Context context;
    private List<byte[]> images;
    private List<String> roadNames;

    public WeatherCamListAdapter(Context context, TrafficData trafficData) {
        this.context = context;
        this.images = trafficData.getWeatherCamImages();
        this.roadNames = trafficData.getRoadNames();
    }

    @NonNull
    @Override
    public WeatherCamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherCamViewHolder(LayoutInflater.from(context).inflate(R.layout.weathercam_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherCamViewHolder holder, int position) {
        Log.d("WeatherCamListAdapter", "onBindViewHolder: " + position);
        byte[] image = images.get(position);
        String roadName = roadNames.get(position);

        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.weatherCamImage.setImageBitmap(bm);
        holder.roadName.setText(roadName);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
