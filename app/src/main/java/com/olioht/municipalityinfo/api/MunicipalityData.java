package com.olioht.municipalityinfo.api;

public class MunicipalityData {
    private String municipality;
    private String municipalityCode;
    private PopulationData populationData;
    private PoliticalData politicalData;

    public MunicipalityData(String municipality, String municipalityCode, PopulationData populationData, PoliticalData politicalData) {
        this.municipality = municipality;
        this.municipalityCode = municipalityCode;
        this.populationData = populationData;
        this.politicalData = politicalData;
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

}
