package com.mogli.coronavirustracker;

public class Location {

    private String cCountry;
    private String cState;
    private int cNumInfected;
    private int cNumDeaths;
    private int cNumRecovered;
    private int cDiffFromPrevDay;

    public Location(String country, String state, int infected, int d , int recovered,int diffFromPrevDay){
        cCountry = country;
        cState = state;
        cNumInfected = infected;
        cNumDeaths = d;
        cNumRecovered = recovered;
        cDiffFromPrevDay = diffFromPrevDay;
    }

    public int getcNumInfected() {
        return cNumInfected;
    }

    public String getcCountry() {
        return cCountry;
    }

    public String getcState() {
        return cState;
    }

    public int getcNumDeaths() {
        return cNumDeaths;
    }

    public int getcNumRecovered() {
        return cNumRecovered;
    }

    public int getcDiffFromPrevDay() {
        return cDiffFromPrevDay;
    }
}
