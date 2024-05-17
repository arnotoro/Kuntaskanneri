package com.olioht.municipalityinfo.api;

public class MunicipalityData {
    private int year;
    private int population;
    public MunicipalityData(int y, int p) {
        year = y;
        population = p;
    }

    public int getYear() {
        return year;
    }

    public int getPopulation() {
        return population;
    }
}
