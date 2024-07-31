package com.olioht.kuntaskanneri.api;

public class MunicipalityData {
    private String municipalityName;
    private String municipalityCode;
    private PopulationData populationData;
    private PoliticalData politicalData;
    private TrafficData trafficData;
    private WeatherData weatherData;

    public MunicipalityData(String municipality, String municipalityCode, PopulationData populationData, PoliticalData politicalData, TrafficData trafficData, WeatherData weatherData) {
        this.municipalityName = municipality;
        this.municipalityCode = municipalityCode;
        this.populationData = populationData;
        this.politicalData = politicalData;
        this.trafficData = trafficData;
        this.weatherData = weatherData;
    }

    public PopulationData getPopulationData() {
        return populationData;
    }

    public PoliticalData getPoliticalData() {
        return politicalData;
    }

    public TrafficData getTrafficData() {
        return trafficData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

}
