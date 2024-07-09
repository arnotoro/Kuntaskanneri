package com.olioht.municipalityinfo.api;

public class PoliticalData {
    private double employmentRate;
    private double employmentSelfSuffiency;


    public PoliticalData(double employmentRate, double employmentSelfSuffiency) {
        this.employmentRate = employmentRate;
        this.employmentSelfSuffiency = employmentSelfSuffiency;
    }

    public double getEmploymentRate() {
        return employmentRate;
    }

    public double getEmploymentSelfSuffiency() {
        return employmentSelfSuffiency;
    }
}
