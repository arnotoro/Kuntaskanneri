package com.olioht.kuntaskanneri.api;

import java.util.ArrayList;

public class TrafficData {
    private String weatherCamId;
    private ArrayList<byte[]> weatherCamImage;
    private ArrayList<String> roadNames;
    private String camLocationName;

    public TrafficData(String weatherCamId, ArrayList<byte[]> weatherCamImage, ArrayList<String> roadNames, String camLocationName) {
        this.weatherCamId = weatherCamId;
        this.weatherCamImage = weatherCamImage;
        this.roadNames = roadNames;
        this.camLocationName = camLocationName;
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

    public ArrayList<String> getRoadNames() {
        return roadNames;
    }

    public String getCamLocationName() {
        return camLocationName;
    }

}
