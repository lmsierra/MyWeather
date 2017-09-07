package lmsierra.com.myweather.model;

import com.google.gson.Gson;

public class City {

    private String name;
    private double lat;
    private double lng;
    private String countryName;
    private long population;

    private BBox bbox;

    public City(String name, double lat, double lng, String countryName, long population, BBox bbox) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.countryName = countryName;
        this.population = population;
        this.bbox = bbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getEast() {
        return bbox.getEast();
    }

    public void setEast(double east) {
        this.bbox.setEast(east);
    }

    public double getWest() {
        return this.bbox.getWest();
    }

    public void setWest(double west) {
        this.bbox.setWest(west);
    }

    public double getNorth() {
        return this.bbox.getNorth();
    }

    public void setNorth(double north) {
        this.bbox.setNorth(north);
    }

    public double getSouth() {
        return this.bbox.getSouth();
    }

    public void setSouth(double south) {
        this.bbox.setSouth(south);
    }

    public BBox getBbox() {
        return bbox;
    }

    public void setBbox(BBox bbox) {
        this.bbox = bbox;
    }

    public class BBox {

        private double east;
        private double west;
        private double north;
        private double south;

        public BBox(double east, double west, double north, double south) {
            this.east = east;
            this.west = west;
            this.north = north;
            this.south = south;
        }

        public double getEast() {
            return east;
        }

        public void setEast(double east) {
            this.east = east;
        }

        public double getWest() {
            return west;
        }

        public void setWest(double west) {
            this.west = west;
        }

        public double getNorth() {
            return north;
        }

        public void setNorth(double north) {
            this.north = north;
        }

        public double getSouth() {
            return south;
        }

        public void setSouth(double south) {
            this.south = south;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
