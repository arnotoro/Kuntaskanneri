package com.olioht.kuntaskanneri.api;


import java.util.Map;

public class PopulationData {
    private Map<Integer, Integer> populationChange;
    private int currentPopulation;
    private int currentYear;

    public PopulationData(Map<Integer, Integer> populationChange) {
        this.populationChange = populationChange;

        // Latest entry in the map is the current population and year
        this.currentPopulation = (int) populationChange.values().toArray()[populationChange.size()-1];
        this.currentYear = (int) populationChange.keySet().toArray()[populationChange.size()-1];
    }

    public int getPopulation() {
        return currentPopulation;
    }

    public Map<Integer, Integer> getPopulationChange() {
        return populationChange;
    }

}
