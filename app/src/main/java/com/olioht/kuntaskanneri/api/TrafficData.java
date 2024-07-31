package com.olioht.kuntaskanneri.api;

import java.util.ArrayList;

public class TrafficData {
    private String weatherCamId;
    private ArrayList<byte[]> weatherCamImage;

    public TrafficData(String weatherCamId, ArrayList<byte[]> weatherCamImage) {
        this.weatherCamId = weatherCamId;
        this.weatherCamImage = weatherCamImage;
    }

    public String getWeatherCamId() {
        return weatherCamId;
    }

    public ArrayList<byte[]> getWeatherCamImages() {
        return weatherCamImage;
    }

    public int getWeatherCamCount() {
        return weatherCamImage.size();
    }

}
