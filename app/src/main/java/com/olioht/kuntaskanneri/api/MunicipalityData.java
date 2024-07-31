package com.olioht.kuntaskanneri.api;

public class MunicipalityData {
    private String municipality;
    private String municipalityCode;
    private PopulationData populationData;
    private PoliticalData politicalData;
    private TrafficData trafficData;

    public MunicipalityData(String municipality, String municipalityCode, PopulationData populationData, PoliticalData politicalData, TrafficData trafficData) {
        this.municipality = municipality;
        this.municipalityCode = municipalityCode;
        this.populationData = populationData;
        this.politicalData = politicalData;
        this.trafficData = trafficData;
    }

    public String getMunicipality() {
        return municipality;
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

}
