package lmsierra.com.myweather.model;

import com.google.gson.Gson;

import java.util.List;

public class Georeference {
    private int totalResultsCount;
    private List<City> geonames;

    public Georeference(int totalResultsCount, List<City> geonames) {
        this.totalResultsCount = totalResultsCount;
        this.geonames = geonames;
    }

    public int getTotalResultsCount() {
        return totalResultsCount;
    }

    public void setTotalResultsCount(int totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public List<City> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<City> geonames) {
        this.geonames = geonames;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
