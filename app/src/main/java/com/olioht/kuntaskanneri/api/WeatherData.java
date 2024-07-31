package com.olioht.kuntaskanneri.api;

public class WeatherData {
    private int currentTemperature;
    private int currentTemperatureFeelsLike;
    private int currentHumidity;
    private int currentWindSpeed;
    private int currentPressure;
    private double rainAmount;
    private String currentWeather;
    private String weatherIconId;

    public WeatherData(int currentTemperature, int currentTemperatureFeelsLike, int currentHumidity, int currentWindSpeed, int currentPressure, double rainAmount, String currentWeather, String weatherIconId) {
        this.currentTemperature = currentTemperature;
        this.currentTemperatureFeelsLike = currentTemperatureFeelsLike;
        this.currentHumidity = currentHumidity;
        this.currentWindSpeed = currentWindSpeed;
        this.currentPressure = currentPressure;
        this.rainAmount = rainAmount;
        this.currentWeather = currentWeather;
        this.weatherIconId = weatherIconId;

    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public int getCurrentWindSpeed() {
        return currentWindSpeed;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public String getWeatherIconId() {
        return weatherIconId;
    }

    public int getCurrentPressure() {
        return currentPressure;
    }

    public double getCurrentRainAmountLastHour() {
        return rainAmount;
    }




}
