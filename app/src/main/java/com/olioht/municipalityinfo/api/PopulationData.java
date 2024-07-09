package com.olioht.municipalityinfo.api;


import java.util.Map;

public class PopulationData {
    private Map<Integer, Integer> populationChange;
    private int currentPopulation;
    private int currentYear;

    public PopulationData(Map<Integer, Integer> populationChange) {
        this.populationChange = populationChange;
        this.currentPopulation = (int) populationChange.values().toArray()[populationChange.size()-1];
        this.currentYear = (int) populationChange.keySet().toArray()[populationChange.size()-1];
        System.out.println("Current population: " + currentPopulation);
        System.out.println("Current year: " + currentYear);


    }

    public int getYear() {
        return currentYear;
    }

    public int getPopulation() {
        return currentPopulation;
    }

    public Map<Integer, Integer> getPopulationChange() {
        return populationChange;
    }

}
